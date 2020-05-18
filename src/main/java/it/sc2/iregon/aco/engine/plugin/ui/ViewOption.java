package it.sc2.iregon.aco.engine.plugin.ui;

public class ViewOption {
    private final boolean enableAsDefault;

    public ViewOption(boolean enableAsDefault) {
        this.enableAsDefault = enableAsDefault;
    }

    public boolean isEnableAsDefault() {
        return enableAsDefault;
    }
}
