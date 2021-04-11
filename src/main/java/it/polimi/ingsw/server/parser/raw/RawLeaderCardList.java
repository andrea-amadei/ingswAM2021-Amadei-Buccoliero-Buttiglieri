package it.polimi.ingsw.server.parser.raw;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.server.parser.RawList;

import java.util.List;

public class RawLeaderCardList implements RawList<RawLeaderCard> {
    @SerializedName(value = "list", alternate = "cards")
    private List<RawLeaderCard> list;

    @Override
    public List<RawLeaderCard> getList() {
        return list;
    }
}
