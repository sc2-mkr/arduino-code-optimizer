package it.sc2.iregon.aco.config.chip.mappers;

import java.util.*;

public class ArduinoUnoMapper extends Mapper {

    private final Map<String, String> constants = new HashMap<String, String>();

    public ArduinoUnoMapper() {
        super("Arduino UNO");

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

        // Add built-in constants
        constants.put("LED_BUILTIN", "13");
    }

    @Override
    public String getConstantValue(String constant) {
        return constants.get(constant);
    }
}
