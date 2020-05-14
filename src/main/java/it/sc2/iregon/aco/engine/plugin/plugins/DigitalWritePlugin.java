package it.sc2.iregon.aco.engine.plugin.plugins;

import it.sc2.iregon.aco.config.chip.mappers.Mapper;
import it.sc2.iregon.aco.config.chip.structure.Pin;
import it.sc2.iregon.aco.engine.structure.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DigitalWritePlugin implements Plugin {

    // sketch constants
    private final List<Constant> constants;

    ViewOption viewOption;

    private String source;
    private Mapper pinMapping;

    public DigitalWritePlugin() {
        this.viewOption = new ViewOption(true);
        constants = new ArrayList<>();
    }

    @Override
    public String getPluginName() {
        return "Optimize digitalWrite()";
    }

    @Override
    public String getPluginDescription() {
        return "Replace digitalWrite function with relative port manipulation instruction";
    }

    @Override
    public void load(String source, Mapper pinMapping) {
        this.source = source;
        this.pinMapping = pinMapping;
    }

    @Override
    public String run() {
        findAllConstants();

        StringBuilder sb = new StringBuilder();

        final String regex = "digitalWrite\\(([^,]+),\\s*([^\\)]+)\\)";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(source);

        boolean result = matcher.find();

        if (result) {
            do {
                String logicalPinName = getLogicalPinName(matcher.group(1));
                Optional<Pin> pinToReplace = pinMapping.findPinByLogicalName(logicalPinName);
                if (pinToReplace.isPresent()) {
                    String replacement = "PORT" +
                            pinToReplace.get().getPort();
                    if (matcher.group(2).equals("HIGH"))
                        replacement += " |= " +
                                getPortSettingBits(
                                        pinToReplace.get().getPortIndex(),
                                        "0",
                                        "1");
                    else if (matcher.group(2).equals("LOW"))
                        replacement += " &= " +
                                getPortSettingBits(
                                        pinToReplace.get().getPortIndex(),
                                        "1",
                                        "0");
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
            return constants.stream()
                    .filter(constant -> constant.getName().equals(logicalIndex))
                    .findFirst().get().getValue();
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

    private String getPortSettingBits(String selectedBitIndex, String emptyValue, String fillValue) {
        int portIndexInt = Integer.parseInt(selectedBitIndex);
        StringBuilder retValue = new StringBuilder();

        for (int i = 0; i < (8 - portIndexInt) - 1; i++) {
            retValue.append(emptyValue);
        }
        retValue.append(fillValue);
        for (int i = (8 - portIndexInt); i < 8; i++) {
            retValue.append(emptyValue);
        }
        return retValue.toString();
    }

    @Override
    public ViewOption getViewOption() {
        return viewOption;
    }
}
