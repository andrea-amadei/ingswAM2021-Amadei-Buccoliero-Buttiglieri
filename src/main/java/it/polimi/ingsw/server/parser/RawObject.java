package it.polimi.ingsw.server.parser;

import it.polimi.ingsw.exceptions.IllegalRawConversionException;

public interface RawObject<O> {
    int getId();

    O toObject() throws IllegalRawConversionException;
}
