package it.polimi.ingsw.client.updates;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.client.clientmodel.ClientModel;
import it.polimi.ingsw.client.clientmodel.ClientPlayer;
import it.polimi.ingsw.client.clientmodel.ClientProduction;
import it.polimi.ingsw.server.model.production.Production;
import it.polimi.ingsw.common.parser.raw.RawCrafting;

public class AddCraftingUpdate implements Update{

    private final RawCrafting crafting;
    @SerializedName(value = "crafting_type", alternate = "craftingType")
    private final Production.CraftingType craftingType;
    private final int index;
    private final String player;

    public AddCraftingUpdate(RawCrafting crafting, Production.CraftingType craftingType, int index, String player){
        this.crafting = crafting;
        this.craftingType = craftingType;
        this.index = index;
        this.player = player;
        checkFormat();
    }

    @Override
    public void apply(ClientModel client) {
        ClientPlayer clientPlayer = client.getPlayerByName(player);
        ClientProduction production = clientPlayer.getProduction();
        switch(craftingType){
            case BASE:
                production.addBaseCrafting(crafting);
                break;
            case LEADER:
                production.addLeaderCrafting(crafting);
                break;
            default:
                throw new IllegalArgumentException("Add crafting update received upgradable");
        }
    }

    @Override
    public void checkFormat() {
        if(crafting == null)
            throw new NullPointerException("Pointer to crafting is null");
        if(craftingType == null)
            throw new NullPointerException("Pointer to craftingType is null");
        if(index < 0)
            throw new IllegalArgumentException("Index cannot be negative");
        if(player == null)
            throw new NullPointerException("Pointer to player is null");
    }

    public RawCrafting getCrafting() {
        return crafting;
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
