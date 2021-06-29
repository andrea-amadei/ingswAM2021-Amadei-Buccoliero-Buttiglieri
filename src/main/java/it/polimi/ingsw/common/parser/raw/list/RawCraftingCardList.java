package it.polimi.ingsw.common.parser.raw.list;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.common.parser.OrderedRawList;
import it.polimi.ingsw.common.parser.raw.RawCraftingCard;

import java.util.List;

public class RawCraftingCardList implements OrderedRawList<RawCraftingCard> {
    @SerializedName(value = "shop", alternate = "cards")
    private List<RawCraftingCard> shop;

    @Override
    public List<RawCraftingCard> getList() {
        return shop;
    }
}
