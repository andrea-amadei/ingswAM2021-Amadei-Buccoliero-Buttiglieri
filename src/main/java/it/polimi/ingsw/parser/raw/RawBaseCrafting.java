package it.polimi.ingsw.parser.raw;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.model.production.Crafting;
import it.polimi.ingsw.parser.UniqueRawObject;

public class RawBaseCrafting extends RawCrafting implements UniqueRawObject<Crafting> {
    @SerializedName("id")
    private int id;

    @Override
    public int getId() {
        return id;
    }
}
