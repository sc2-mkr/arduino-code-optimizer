package it.sc2.iregon.aco.config;

import it.sc2.iregon.aco.config.chip.mappers.Mapper;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MapperFactory {

    private final String defaultPluginPrefix = "it.sc2.iregon.aco.config.chip.mappers";
    List<Mapper> mappers;
    Reflections reflections;

    public MapperFactory() {
        reflections = new Reflections(defaultPluginPrefix);

        Set<Class<? extends Mapper>> genericClassPlugins = reflections.getSubTypesOf(Mapper.class);
        mappers = new ArrayList<>();
        genericClassPlugins.forEach(genericClassPlugin -> {
            try {
                Constructor constructor = genericClassPlugin.getNestHost().getConstructor();
                Mapper mapperInst = (Mapper) constructor.newInstance();
                mappers.add(mapperInst);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        });
    }

    public List<Mapper> getAllMapping() {
        return mappers;
    }

    /**
     * Get mapping of a chip
     *
     * @param chip chip name <br>
     *             Chip supported:
     *             <ul>
     *             <li>ATmega8/168/328</li>
     *             </ul>
     * @return chip port mapping
     */
    public Mapper getMapping(String chip) {
        return mappers.stream()
                .filter(mapping -> mapping.getMapName().equals(chip))
                .findFirst()
                .orElse(null);
    }
}
