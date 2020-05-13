package it.sc2.iregon.aco.engine.plugin;

import com.jfoenix.controls.JFXCheckBox;
import it.sc2.iregon.aco.config.Mapping;
import it.sc2.iregon.aco.engine.plugin.exceptions.PluginNotFoundException;
import it.sc2.iregon.aco.engine.plugin.plugins.Plugin;
import javafx.scene.paint.Paint;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PluginManager {

    private final String defaultPluginPrefix = "it.sc2.iregon.aco.engine.plugin.plugins";
    Reflections reflections;
    List<Plugin> plugins;

    public PluginManager() {
        reflections = new Reflections(defaultPluginPrefix);

        Set<Class<? extends Plugin>> genericClassPlugins = reflections.getSubTypesOf(Plugin.class);
        plugins = new ArrayList<>();
        genericClassPlugins.forEach(genericClassPlugin -> {
            try {
                Constructor constructor = genericClassPlugin.getNestHost().getConstructor();
                Plugin pluginInst = (Plugin) constructor.newInstance();
                plugins.add(pluginInst);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        });
    }

    public int getPluginsNum() {
        return plugins.size();
    }

    public List<Plugin> getAllPlugins() {
        return plugins;
    }

    public String runPlugin(String name, String source, Mapping pinMapping) throws PluginNotFoundException {
        Plugin pluginToRun = plugins.stream()
                .filter(plugin -> plugin.getPluginName().equals(name))
                .findFirst()
                .orElse(null);

        if(pluginToRun == null) throw new PluginNotFoundException(name);

        pluginToRun.load(source, pinMapping);
        return pluginToRun.run();
    }

}
