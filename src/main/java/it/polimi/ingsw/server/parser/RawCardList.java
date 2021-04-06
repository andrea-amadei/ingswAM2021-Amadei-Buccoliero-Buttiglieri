package it.polimi.ingsw.server.parser;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RawCardList {
    @SerializedName(value = "list", alternate = "cards")
    private List<RawLeaderCard> list;

    public List<RawLeaderCard> getList() {
        return list;
    }
}
