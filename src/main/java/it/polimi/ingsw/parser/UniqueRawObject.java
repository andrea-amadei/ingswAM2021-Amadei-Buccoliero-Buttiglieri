package it.polimi.ingsw.parser;

public interface UniqueRawObject<O extends SerializedObject> extends RawObject<O> {
    int getId();
}
