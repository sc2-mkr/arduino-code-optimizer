package it.sc2.iregon.aco.engine.plugin.ui;

public class ViewOption {

    // Position in the plugins list in main windows
    private final int listPosition;
    private final boolean enableAsDefault;

    public ViewOption(int listPosition, boolean enableAsDefault) {
        this.listPosition = listPosition;
        this.enableAsDefault = enableAsDefault;
    }

    public int getListPosition() {
        return listPosition;
    }

    public boolean isEnableAsDefault() {
        return enableAsDefault;
    }
}
