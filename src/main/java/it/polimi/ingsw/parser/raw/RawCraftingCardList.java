package it.polimi.ingsw.parser.raw;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.parser.OrderedRawList;
import it.polimi.ingsw.parser.RawList;

import java.util.List;

public class RawCraftingCardList implements OrderedRawList<RawCraftingCard> {
    @SerializedName(value = "shop", alternate = "cards")
    private List<RawCraftingCard> shop;

    @Override
    public List<RawCraftingCard> getList() {
        return shop;
    }
}
