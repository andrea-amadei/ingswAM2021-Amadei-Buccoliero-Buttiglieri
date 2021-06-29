package it.polimi.ingsw.common.parser;

import it.polimi.ingsw.common.exceptions.IllegalRawConversionException;

public interface RawObject<O extends SerializableObject<?>> {
    O toObject() throws IllegalRawConversionException;
}
