package it.sc2.iregon.aco.engine;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public interface Engine {

    void load(InputStream source) throws IOException;

    void optimize();

    String getOutput();
}
