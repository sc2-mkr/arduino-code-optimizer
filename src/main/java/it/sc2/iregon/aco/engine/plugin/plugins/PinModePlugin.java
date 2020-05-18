package it.sc2.iregon.aco.engine.plugin.plugins;

import it.sc2.iregon.aco.config.chip.mappers.Mapper;
import it.sc2.iregon.aco.config.chip.structure.Pin;
import it.sc2.iregon.aco.engine.plugin.ui.ViewOption;
import it.sc2.iregon.aco.engine.structure.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PinModePlugin implements Plugin {

    // sketch constants
    private final List<Constant> constants;

    private String source;
    private Mapper pinMapper;

    private ViewOption viewOption;

    public PinModePlugin() {
        this.viewOption = new ViewOption(true);
        constants = new ArrayList<>();
    }

    @Override
    public String getPluginName() {
        return "Optimize pinMode()";
    }

    @Override
    public String getPluginDescription() {
        return "Replace pinMode function with relative port manipulation instruction";
    }

    @Override
    public void load(String source, Mapper pinMapper) {
        this.source = source;
        this.pinMapper = pinMapper;
    }

    @Override
    public String run() {
        findAllConstants();

        StringBuilder sb = new StringBuilder();

        final String regex = "pinMode\\(([^,]*),\\s*([^)]*)\\)";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(source);

        boolean result = matcher.find();

        if (result) {
            do {
                String logicalPinName = getLogicalPinName(matcher.group(1));
                Optional<Pin> pinToReplace = pinMapper.findPinByLogicalName(logicalPinName);
                if (pinToReplace.isPresent()) {
                    String replacement = "";
                    if (matcher.group(2).equals("OUTPUT"))
                        replacement = "DDR" +
                                pinToReplace.get().getPort() +
                                " |= (1<<DD" +
                                pinToReplace.get().getPort() +
                                pinToReplace.get().getPortIndex() +
                                ")";
                    System.out.println("Replacement: " + replacement);
                    matcher.appendReplacement(sb, replacement);
                } else {
                    // TODO: manage pin not found
                }
                result = matcher.find();
            } while (result);
            matcher.appendTail(sb);
        }
        return sb.toString();
    }

    private String getLogicalPinName(String logicalIndex) {
        try {
            Integer.parseInt(logicalIndex);
            return logicalIndex;
        } catch (NumberFormatException e) {
            Optional<Constant> res = constants.stream()
                    .filter(constant -> constant.getName().equals(logicalIndex))
                    .findFirst();
            if(res.isPresent()) return res.get().getValue();
            else return pinMapper.getConstantValue(logicalIndex);
        }
    }

    private void findAllConstants() {
        // Const variables
        final String constRegex = "const\\s+([^\\s]+)\\s+([^\\s]+)\\s+=\\s+([^;]+)";
        final Pattern constPattern = Pattern.compile(constRegex, Pattern.MULTILINE);
        final Matcher constMatcher = constPattern.matcher(source);

        while (constMatcher.find()) {
            constants.add(new Constant(constMatcher.group(2), constMatcher.group(3), Constant.Type.CONST));
        }

        // DEFINE
        final String defineRegex = "#define\\s*([^\\s]+)\\s*(.*)";
        final Pattern definePattern = Pattern.compile(defineRegex, Pattern.MULTILINE);
        final Matcher defineMatcher = definePattern.matcher(source);

        while (defineMatcher.find()) {
            constants.add(new Constant(defineMatcher.group(1), defineMatcher.group(2), Constant.Type.DEFINE));
        }
    }

    @Override
    public ViewOption getViewOption() {
        return viewOption;
    }

    @Override
    public ImpactLevelType getImpactType() {
        return ImpactLevelType.LOW;
    }
}
