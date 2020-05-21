package it.sc2.iregon.aco.engine.plugin.plugins;

import it.sc2.iregon.aco.config.chip.mappers.Mapper;
import it.sc2.iregon.aco.engine.plugin.ui.ViewOption;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SetupLoopPlugin implements Plugin{

    private String source;
    private Mapper pinMapper;

    private ViewOption viewOption;

    private int endMainIndex;

    private int startLoopIndex;
    private int startInnerLoopIndex;
    private int endLoopIndex;

    public SetupLoopPlugin() {
        this.viewOption = new ViewOption(1, false);
    }

    @Override
    public String getPluginName() {
        return "Replace setup() and loop() with main()";
    }

    @Override
    public String getPluginDescription() {
        return "Replace setup() and loop() function with main() function";
    }

    @Override
    public void load(String source, Mapper pinMapper) {
        this.source = source;
        this.pinMapper = pinMapper;
    }

    @Override
    public String run() {
        // Replace void setup() with int main()
        source = source.replaceFirst("void setup\\(\\)\\s*\\{", "int main() {");

        // Place before main() end while(true) with loop() inner code
        setLoopIndexes();

        String loop = source.substring(startLoopIndex, endLoopIndex);
        String loopInner = source.substring(startInnerLoopIndex, endLoopIndex + 1).trim(); // endLoopIndex+1 for include last bracket

        source = source.replace(loop, "");

        // Get main() end
        endMainIndex = getMainEnd();

        return source.substring(0, endMainIndex + 1) +
                "while(true) {\n"+
                loopInner +
                "}\n" +
                source.substring(endMainIndex + 1);
    }

    private int getMainEnd() {
        final String mainRegex = "int main\\(\\) \\{";
        Matcher mainMatcher = getRegexMatcher(mainRegex);

        if (mainMatcher.find()) {
            int childIndex = 0;
            char[] remainingCharsArray = source.substring(mainMatcher.end() + 1).toCharArray();
            for (int i = 0; i < remainingCharsArray.length; i++) {
                if (remainingCharsArray[i] == '{') childIndex++;
                else if (remainingCharsArray[i] == '}') {
                    childIndex--;
                    if (childIndex < 0) {
                        return i + mainMatcher.end();
                    }
                }
            }
            // TODO: manage main end not fount
        } else {
            // TODO: manage main start not fount
        }
        return -1;
    }

    private void setLoopIndexes() {
        final String loopRegex = "void loop\\(\\)\\s*\\{";
        Matcher loopMatcher = getRegexMatcher(loopRegex);

        if (loopMatcher.find()) {
            startInnerLoopIndex = loopMatcher.end() + 1; // Remove first bracket
            startLoopIndex = loopMatcher.start();


            int childIndex = 0;
            char[] remainingCharsArray = source.substring(startInnerLoopIndex).toCharArray();
            for (int i = 0; i < remainingCharsArray.length; i++) {
                if (remainingCharsArray[i] == '{') childIndex++;
                else if (remainingCharsArray[i] == '}') {
                    childIndex--;
                    if (childIndex == 0) {
                        endLoopIndex = i + startInnerLoopIndex - 1; // Remove last bracket
                        break;
                    }
                }
            }
            // TODO: manage setup end not fount
        } else {
            // TODO: manage setup start not fount
        }
    }

    private Matcher getRegexMatcher(String regex) {
        final Pattern setupPattern = Pattern.compile(regex, Pattern.DOTALL);
        return setupPattern.matcher(source);
    }

    @Override
    public ViewOption getViewOption() {
        return viewOption;
    }

    @Override
    public ImpactLevelType getImpactType() {
        return ImpactLevelType.HIGH;
    }
}
