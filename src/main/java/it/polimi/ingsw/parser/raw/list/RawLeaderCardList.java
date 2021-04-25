package it.polimi.ingsw.parser.raw.list;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.parser.OrderedRawList;
import it.polimi.ingsw.parser.RawList;
import it.polimi.ingsw.parser.raw.RawLeaderCard;

import java.util.List;

public class RawLeaderCardList implements OrderedRawList<RawLeaderCard> {
    @SerializedName(value = "list", alternate = "cards")
    private List<RawLeaderCard> list;

    @Override
    public List<RawLeaderCard> getList() {
        return list;
    }
}
