package it.sc2.iregon.aco.engine.plugin.plugins;

import it.sc2.iregon.aco.config.chip.mappers.Mapper;
import it.sc2.iregon.aco.engine.plugin.ui.ViewOption;

public interface Plugin {

    /**
     * Impact that the plugin may have on code
     * <b>HIGH</b>: the plugin invalidate the portability of the code
     * <b>MEDIUM</b>: the plugin may invalidate the portability of the code
     * <b>LOW</b>: the plugin not invalidate the portability of the code
     */
    enum ImpactLevelType { HIGH, MEDIUM, LOW }

    String getPluginName();

    String getPluginDescription();

    void load(String source, Mapper pinMapper);

    String run();

    ViewOption getViewOption();

    ImpactLevelType getImpactType();
}
