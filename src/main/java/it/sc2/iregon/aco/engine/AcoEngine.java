package it.sc2.iregon.aco.engine;

import it.sc2.iregon.aco.config.MapperFactory;
import it.sc2.iregon.aco.config.chip.mappers.Mapper;
import it.sc2.iregon.aco.engine.plugin.PluginManager;
import it.sc2.iregon.aco.engine.plugin.exceptions.PluginNotFoundException;
import it.sc2.iregon.aco.engine.plugin.plugins.Plugin;
import it.sc2.iregon.aco.engine.structure.Constant;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AcoEngine implements Engine {

    private final Map<String, Boolean> options;

    // Arduino port mapping
    MapperFactory mappingFactory;
    Mapper portMapping;
    PluginManager pluginManager;
    private String source;

    // Execution variables
//    private final int startSetupIndex = -1;
//    private final int endSetupIndex = -1;
//    private final int startLoopIndex = -1;
//    private final int endLoopIndex = -1;

    public AcoEngine() {
        options = new HashMap<>();

        // TODO: add a menu for mapper selection
        mappingFactory = new MapperFactory();
        pluginManager = new PluginManager();

//        constants = new ArrayList<>();
    }

    @Override
    public void load(InputStream source) throws IOException {
        this.source = new String(source.readAllBytes());
        source.close();
    }

    public void setOption(String option, boolean state) {
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
            if (isActive) {
                try {
                    source = pluginManager.runPlugin(pluginName, source, portMapping);
                } catch (PluginNotFoundException e) {
                    e.printStackTrace(); // TODO: manage PluginNotFoundException
                }
            }
        });
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
