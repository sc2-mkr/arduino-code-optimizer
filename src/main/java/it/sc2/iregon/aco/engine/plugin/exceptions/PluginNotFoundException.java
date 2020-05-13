package it.sc2.iregon.aco.engine.plugin.exceptions;

public class PluginNotFoundException extends Exception {

    public PluginNotFoundException(String pluginName) {
        super("Impossible to find plugin: '" + pluginName + "'");
    }
}
