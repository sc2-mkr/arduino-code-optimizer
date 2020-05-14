package it.sc2.iregon.aco.engine.structure;

public class Constant {

    private Type type;
    private String name;
    private String value;

    public Constant(String name, String value, Type type) {
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public enum Type {
        CONST,
        DEFINE
    }
}
