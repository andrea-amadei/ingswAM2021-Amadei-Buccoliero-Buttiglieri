package it.polimi.ingsw.client.updates;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.model.production.Production;

public class SelectedCraftingUpdate implements Update{

    @SerializedName(value = "crafting_type", alternate = "craftingType")
    private final Production.CraftingType craftingType;
    private final int index;
    private final String player;

    public SelectedCraftingUpdate(Production.CraftingType craftingType, int index, String player){
        this.craftingType = craftingType;
        this.index = index;
        this.player = player;
        checkFormat();
    }

    @Override
    public void apply(ClientModel client) {

    }

    @Override
    public void checkFormat() {
        if(craftingType == null)
            throw new NullPointerException("Pointer to crafting type is null");
        if(index < 0)
            throw new IllegalArgumentException("Index cannot be negative");
        if(player == null)
            throw new NullPointerException("Pointer to player is null");
    }

    public Production.CraftingType getCraftingType() {
        return craftingType;
    }

    public int getIndex() {
        return index;
    }

    public String getPlayer() {
        return player;
    }
}
