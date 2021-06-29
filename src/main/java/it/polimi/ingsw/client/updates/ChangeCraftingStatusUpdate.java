package it.polimi.ingsw.client.updates;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.client.clientmodel.ClientModel;
import it.polimi.ingsw.client.clientmodel.ClientPlayer;
import it.polimi.ingsw.server.model.production.Production;

public class ChangeCraftingStatusUpdate implements Update{

    private final String player;

    @SerializedName(value = "is_ready", alternate = "isReady")
    private final Boolean isReady;

    private final Integer index;

    @SerializedName(value = "crafting_type", alternate = "craftingType")
    private final Production.CraftingType craftingType;

    public ChangeCraftingStatusUpdate(String player, Boolean isReady, Integer index, Production.CraftingType craftingType) {
        this.player = player;
        this.isReady = isReady;
        this.index = index;
        this.craftingType = craftingType;
        checkFormat();
    }

    @Override
    public void apply(ClientModel client) {
        ClientPlayer clientPlayer = client.getPlayerByName(player);
        clientPlayer.getProduction().setCraftingStatus(craftingType, index, isReady);
    }

    @Override
    public void checkFormat() {
        if(player == null || isReady == null || index == null || craftingType == null)
            throw new NullPointerException("Some field is missing");
        if(index < 0)
            throw new IllegalArgumentException("Index must be non negative");
    }
}
