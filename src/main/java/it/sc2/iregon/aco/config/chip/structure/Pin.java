package it.sc2.iregon.aco.config.chip.structure;

public class Pin {
    private String logicIndex;
    private String chipIndex;
    private String port;
    private String portIndex;

    public Pin(String logicIndex, String chipIndex, String port, String portIndex) {
        this.logicIndex = logicIndex;
        this.chipIndex = chipIndex;
        this.port = port;
        this.portIndex = portIndex;
    }

    public String getLogicIndex() {
        return logicIndex;
    }

    public String getChipIndex() {
        return chipIndex;
    }

    public String getPort() {
        return port;
    }

    public String getPortIndex() {
        return portIndex;
    }
}
