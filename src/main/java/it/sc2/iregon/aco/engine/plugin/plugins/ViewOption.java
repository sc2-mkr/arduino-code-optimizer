package it.sc2.iregon.aco.engine.plugin.plugins;

public class ViewOption {
    private final boolean enableAsDefult;

    public ViewOption(boolean enableAsDefult) {
        this.enableAsDefult = enableAsDefult;
    }

    public boolean isEnableAsDefult() {
        return enableAsDefult;
    }
}
