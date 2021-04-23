package it.polimi.ingsw.parser;

public interface SerializableObject<R extends RawObject<?>> {
    R toRaw();

    void printDebugInfo();
}
