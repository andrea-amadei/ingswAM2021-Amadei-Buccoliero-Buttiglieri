package it.polimi.ingsw.common.parser;

public interface UniqueRawObject<O extends SerializableObject<?>> extends RawObject<O> {
    String getStringId();

}
