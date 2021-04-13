package it.polimi.ingsw.server.parser;

public interface UniqueRawObject<O extends SerializedObject> extends RawObject<O> {
    int getId();
}
