package it.polimi.ingsw.server.parser.raw;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.server.parser.RawList;

import java.util.List;

public class RawCraftingCardList implements RawList<RawCraftingCard> {
    @SerializedName(value = "shop", alternate = "cards")
    private List<RawCraftingCard> shop;

    @Override
    public List<RawCraftingCard> getList() {
        return shop;
    }
}
