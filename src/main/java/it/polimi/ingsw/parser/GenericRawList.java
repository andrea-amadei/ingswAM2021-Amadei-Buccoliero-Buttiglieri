package it.polimi.ingsw.parser;

import com.google.gson.annotations.SerializedName;
import jdk.jfr.Experimental;

import java.util.List;

@Deprecated
@Experimental
public class GenericRawList<R extends RawObject<?>> implements RawList<R> {
    @SerializedName("list")
    private List<R> list;

    @Override
    public List<R> getList() {
        return list;
    }
}
