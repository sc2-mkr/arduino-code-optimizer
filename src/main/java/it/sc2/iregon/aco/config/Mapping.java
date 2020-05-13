package it.sc2.iregon.aco.config;

import it.sc2.iregon.aco.config.structure.Pin;

import java.util.Optional;

public interface Mapping {
    void addPin(String logicIndex, String chipIndex, String port, String portIndex);
    void removePin(String logicIndex);
    Optional<Pin> findPinByLogicalName(String logicIndex);
    String getMapName();
}
