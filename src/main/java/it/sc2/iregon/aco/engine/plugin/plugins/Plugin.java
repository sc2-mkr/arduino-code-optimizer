package it.sc2.iregon.aco.engine.plugin.plugins;

import it.sc2.iregon.aco.config.chip.mappers.Mapper;

// https://stackoverflow.com/questions/465099/best-way-to-build-a-plugin-system-with-java
public interface Plugin {
    String getPluginName();
    String getPluginDescription();
    void load(String source, Mapper pinMapping);
    String run();
    ViewOption getViewOption();
}
