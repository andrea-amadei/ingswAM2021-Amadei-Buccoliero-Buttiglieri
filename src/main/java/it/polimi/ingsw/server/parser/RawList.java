package it.polimi.ingsw.server.parser;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RawList<T> {
    @SerializedName(value = "list", alternate = {"cards"})
    private List<T> list;

    public List<T> getList() {
        return list;
    }
}
