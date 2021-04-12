package it.polimi.ingsw.server.parser;

import it.polimi.ingsw.exceptions.IllegalRawConversionException;

public interface UniqueRawObject<O> {
    int getId();

    O toObject() throws IllegalRawConversionException;
}
