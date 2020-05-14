package it.sc2.iregon.aco.engine.plugin.plugins;

import it.sc2.iregon.aco.config.chip.mappers.Mapper;

public interface Plugin {
    String getPluginName();

    String getPluginDescription();

    void load(String source, Mapper pinMapping);

    String run();

    ViewOption getViewOption();
}
