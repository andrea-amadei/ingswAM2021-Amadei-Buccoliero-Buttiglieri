package it.polimi.ingsw.server.parser;

import it.polimi.ingsw.exceptions.IllegalRawConversionException;

public interface RawObject<O extends SerializedObject> {
    O convert() throws IllegalRawConversionException;
}
