package it.sc2.iregon.aco.config;

import java.util.ArrayList;

public class MappingFactory {

    ArrayList<Mapping> mappers = new ArrayList<>();

    private static MappingFactory instance;

    public static MappingFactory getInstance() {
        if(instance == null) instance = new MappingFactory();
        return instance;
    }

    public MappingFactory() {
        mappers.add(new AtMega328Mapping());
    }

    public ArrayList<Mapping> getAllMapping() {
        return mappers;
    }

    /**
     * Get mapping of a chip
     * @param chip chip name <br>
     *             Chip supported:
     *             <ul>
     *             <li>ATmega8/168/328</li>
     *             </ul>
     * @return chip port mapping
     */
    public Mapping getMapping(String chip) {
        return mappers.stream()
                .filter(mapping -> mapping.getMapName().equals(chip))
                .findFirst()
                .orElse(null);
    }
}
