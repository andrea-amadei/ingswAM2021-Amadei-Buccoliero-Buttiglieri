package it.polimi.ingsw.parser;

import java.util.List;

public interface OrderedRawList<R extends UniqueRawObject<?>> {
    List<R> getList();
}
