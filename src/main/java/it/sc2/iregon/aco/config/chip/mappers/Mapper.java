package it.sc2.iregon.aco.config.chip.mappers;

import it.sc2.iregon.aco.config.chip.structure.Pin;

import java.util.Optional;

public interface Mapper {
    void addPin(String logicIndex, String chipIndex, String port, String portIndex);
    void removePin(String logicIndex);
    Optional<Pin> findPinByLogicalName(String logicIndex);
    String getMapName();
}
