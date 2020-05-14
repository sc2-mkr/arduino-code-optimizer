package it.sc2.iregon.aco.engine;

import java.io.IOException;
import java.io.InputStream;

public interface Engine {

    void load(InputStream source) throws IOException;

    void optimize();

    String getOutput();
}
