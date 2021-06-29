package it.polimi.ingsw.common.parser;

import java.util.List;

public interface RawList<R extends RawObject<?>> {
    List<R> getList();
}
