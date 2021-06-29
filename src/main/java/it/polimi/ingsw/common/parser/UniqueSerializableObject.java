package it.polimi.ingsw.common.parser;

public interface UniqueSerializableObject<R extends UniqueRawObject<?>> extends SerializableObject<R> {
    String getStringId();
}
