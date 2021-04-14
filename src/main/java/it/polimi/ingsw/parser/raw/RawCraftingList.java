package it.polimi.ingsw.parser.raw;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.parser.RawList;

import java.util.List;

public class RawCraftingList implements RawList<RawBaseCrafting> {
    @SerializedName(value = "base", alternate = "base_crafting")
    private List<RawBaseCrafting> baseCrafting;

    @Override
    public List<RawBaseCrafting> getList() {
        return baseCrafting;
    }
}
