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


    MapperFactory mappingFactory;
    Mapper portMapping;

    PluginManager pluginManager;
    private String source;

    public AcoEngine() {
        options = new HashMap<>();

        mappingFactory = new MapperFactory();
        pluginManager = new PluginManager();
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
        if(portMapping == null) return;

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

    @Override
    public String getOutput() {
        return source;
    }

    public List<Plugin> getAllPlugins() {
        return pluginManager.getAllPlugins();
    }
}
