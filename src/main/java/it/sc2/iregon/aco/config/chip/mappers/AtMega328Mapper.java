package it.sc2.iregon.aco.config.chip.mappers;

import it.sc2.iregon.aco.config.chip.structure.Pin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AtMega328Mapper implements Mapper {

    private List<Pin> pins = new ArrayList<Pin>();

    public AtMega328Mapper() {
        // Add all pins
        addPin("0", "2", "D", "0"); // RX
        addPin("1", "3", "D", "1"); // TX
        addPin("2", "4", "D", "2"); // D2
        addPin("3", "5", "D", "3"); // D3
        addPin("4", "6", "D", "4"); // D4
        addPin("5", "11", "D", "5"); // D5
        addPin("6", "12", "D", "6"); // D6
        addPin("7", "13", "D", "7"); // D7
        addPin("8", "14", "B", "0"); // D8
        addPin("9", "15", "B", "1"); // D9
        addPin("10", "16", "B", "2"); // D10
        addPin("11", "17", "B", "3"); // D11
        addPin("12", "18", "B", "4"); // D12
        addPin("13", "19", "B", "5"); // D13
    }

    @Override
    public void addPin(String logicIndex, String chipIndex, String port, String portIndex) {
        pins.add(new Pin(logicIndex, chipIndex, port, portIndex));
    }

    @Override
    public void removePin(String logicIndex) {
        for(Pin pin : pins) {
            if(pin.getLogicIndex().equals(logicIndex)) {
                pins.remove(pin);
                break;
            }
        }
    }

    @Override
    public Optional<Pin> findPinByLogicalName(String logicIndex) {
        for(Pin pin : pins) {
            if(pin.getLogicIndex().equals(logicIndex)) {
                return Optional.of(pin);
            }
        }
        return Optional.empty();
    }

    @Override
    public String getMapName() {
        return "ATmega8/168/328";
    }
}
