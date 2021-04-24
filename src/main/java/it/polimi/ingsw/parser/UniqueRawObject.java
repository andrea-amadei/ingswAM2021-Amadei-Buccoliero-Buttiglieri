package it.polimi.ingsw.parser;

public interface UniqueRawObject<O extends SerializableObject<?>> extends RawObject<O> {
    String getStringId();

}
