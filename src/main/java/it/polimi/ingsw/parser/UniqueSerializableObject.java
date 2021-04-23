package it.polimi.ingsw.parser;

public interface UniqueSerializableObject<R extends UniqueRawObject<?>> extends SerializableObject<R> {
    String getStringId();
}
