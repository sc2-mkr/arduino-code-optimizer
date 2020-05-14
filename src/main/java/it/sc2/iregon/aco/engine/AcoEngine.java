package it.sc2.iregon.aco.engine;

import it.sc2.iregon.aco.config.chip.mappers.Mapper;
import it.sc2.iregon.aco.config.MapperFactory;
import it.sc2.iregon.aco.engine.plugin.PluginManager;
import it.sc2.iregon.aco.engine.plugin.exceptions.PluginNotFoundException;
import it.sc2.iregon.aco.engine.plugin.plugins.Plugin;
import it.sc2.iregon.aco.engine.structure.Constant;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AcoEngine implements Engine {

    // Arduino port mapping
    MapperFactory mappingFactory;
    Mapper portMapping;
    private String source;
    private final Map<String, Boolean> options;
    PluginManager pluginManager;


    // Execution variables
    private int startSetupIndex = -1;
    private int endSetupIndex = -1;
    private int startLoopIndex = -1;
    private int endLoopIndex = -1;

    // Sketch variables and constants
    private final List<Constant> constants;

    public AcoEngine() {
        options = new HashMap<>();

        // TODO: add a menu for mapper selection
        mappingFactory = new MapperFactory();
        pluginManager = new PluginManager();

        constants = new ArrayList<>();
    }

    @Override
    public void load(InputStream source) throws IOException {
        this.source = new String(source.readAllBytes());
        source.close();
    }

    public void setOption(String option, boolean state) {
        System.out.println("Option: " + option + " " + state);
        this.options.put(option, state);
    }

    public void setOptions(List<String> options, boolean state) {
        options.forEach(s -> this.options.put(s, state));
    }

    public void setChip(String chip) {
        portMapping = mappingFactory.getMapping(chip);
    }

    @Override
    public void optimize() {
        this.options.forEach((pluginName, isActive) -> {
            System.out.println(pluginName + " " + isActive);
            if(isActive) {
                try {
                    source = pluginManager.runPlugin(pluginName, source, portMapping);
                } catch (PluginNotFoundException e) {
                    e.printStackTrace(); // TODO: manage PluginNotFoundException
                }
            }
        });
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


//    private void removeSetupAndLoop() {
//        String setup = source.substring(startSetupIndex, endSetupIndex);
//        String loop = source.substring(startLoopIndex, endLoopIndex);
//    }

//    private void setSetupIndexes() {
//        final String setupRegex = "void setup\\(\\)\\s*\\{";
//        Matcher setupMatcher = getRegexMatcher(setupRegex);
//
//        if (setupMatcher.find()) {
//            startSetupIndex = setupMatcher.end() + 1; // Remove first bracket
//
//            int childIndex = 0;
//            char[] remainingCharsArray = source.substring(startSetupIndex).toCharArray();
//            for (int i = 0; i < remainingCharsArray.length; i++) {
//                if (remainingCharsArray[i] == '{') childIndex++;
//                else if (remainingCharsArray[i] == '}') {
//                    childIndex--;
//                    if (childIndex < 0) {
//                        endSetupIndex = (i + startSetupIndex) - 1; // Remove last bracket
//                        break;
//                    }
//                }
//            }
//            // TODO: manage setup end not fount
//        } else {
//            // TODO: manage setup start not fount
//        }
//    }
//
//    private Matcher getRegexMatcher(String regex) {
//        final Pattern setupPattern = Pattern.compile(regex, Pattern.DOTALL);
//        return setupPattern.matcher(source);
//    }
//
//    private void setLoopIndexes() {
//        final String loopRegex = "void loop\\(\\)\\s*\\{";
//        Matcher loopMatcher = getRegexMatcher(loopRegex);
//
//        if (loopMatcher.find()) {
//            startSetupIndex = loopMatcher.end() + 1; // Remove first bracket
//
//            int childIndex = 0;
//            char[] remainingCharsArray = source.substring(startSetupIndex).toCharArray();
//            for (int i = 0; i < remainingCharsArray.length; i++) {
//                if (remainingCharsArray[i] == '{') childIndex++;
//                else if (remainingCharsArray[i] == '}') {
//                    childIndex--;
//                    if (childIndex < 0) {
//                        endSetupIndex = (i + startSetupIndex) - 1; // Remove last bracket
//                        break;
//                    }
//                }
//            }
//            // TODO: manage setup end not fount
//        } else {
//            // TODO: manage setup start not fount
//        }
//    }

    @Override
    public String getOutput() {
        return source;
    }

    public List<Plugin> getAllPlugins() {
        return pluginManager.getAllPlugins();
    }
}
