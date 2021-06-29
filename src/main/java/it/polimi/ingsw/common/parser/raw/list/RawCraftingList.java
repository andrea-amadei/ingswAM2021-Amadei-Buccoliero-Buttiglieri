package it.polimi.ingsw.common.parser.raw.list;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.common.parser.RawList;
import it.polimi.ingsw.common.parser.raw.RawCrafting;

import java.util.List;

public class RawCraftingList implements RawList<RawCrafting> {
    @SerializedName(value = "base", alternate = "base_crafting")
    private List<RawCrafting> baseCrafting;

    @Override
    public List<RawCrafting> getList() {
        return baseCrafting;
    }
}
