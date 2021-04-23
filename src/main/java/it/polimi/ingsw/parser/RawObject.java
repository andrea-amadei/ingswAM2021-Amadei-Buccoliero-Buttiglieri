package it.polimi.ingsw.parser;

import it.polimi.ingsw.exceptions.IllegalRawConversionException;

public interface RawObject<O extends SerializableObject<?>> {
    O convert() throws IllegalRawConversionException;
}
