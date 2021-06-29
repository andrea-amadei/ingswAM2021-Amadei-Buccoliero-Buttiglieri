package it.polimi.ingsw.common.parser;

public interface SerializableObject<R extends RawObject<?>> {
    R toRaw();

    void printDebugInfo();
}
