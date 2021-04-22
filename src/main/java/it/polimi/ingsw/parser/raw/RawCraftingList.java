package it.polimi.ingsw.parser.raw;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.parser.RawList;

import java.util.List;

public class RawCraftingList implements RawList<RawCrafting> {
    @SerializedName(value = "base", alternate = "base_crafting")
    private List<RawCrafting> baseCrafting;

    @Override
    public List<RawCrafting> getList() {
        return baseCrafting;
    }
}
