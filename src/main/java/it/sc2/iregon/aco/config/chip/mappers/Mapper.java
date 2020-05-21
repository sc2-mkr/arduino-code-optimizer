package it.sc2.iregon.aco.config.chip.mappers;

import it.sc2.iregon.aco.config.chip.structure.Pin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class Mapper {

    private List<Pin> pins;

    private String mapName;

    public Mapper(String mapName) {
        this.mapName = mapName;
        this.pins = new ArrayList<Pin>();
    }

    public void addPin(String logicIndex, String chipIndex, String port, String portIndex) {
        pins.add(new Pin(logicIndex, chipIndex, port, portIndex));
    }

    public void removePin(String logicIndex) {
        for (Pin pin : pins) {
            if (pin.getLogicIndex().equals(logicIndex)) {
                pins.remove(pin);
                break;
            }
        }
    }

    public Optional<Pin> findPinByLogicalName(String logicIndex) {
        for (Pin pin : pins) {
            if (pin.getLogicIndex().equals(logicIndex)) {
                return Optional.of(pin);
            }
        }
        return Optional.empty();
    }

    public String getMapName() {
        return mapName;
    };

    public abstract String getConstantValue(String constant);
}
