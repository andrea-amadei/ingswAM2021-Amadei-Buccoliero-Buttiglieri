package it.polimi.ingsw.client.updates;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.client.model.ClientBaseStorage;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.client.model.ClientPlayer;
import it.polimi.ingsw.client.model.ClientShelf;

import java.util.NoSuchElementException;

public class SelectedResourceUpdate implements Update{

    @SerializedName(value = "container_id", alternate = "containerId")
    private final String containerId;
    private final String resource;
    private final int amount;
    private final String player;

    public SelectedResourceUpdate(String containerId, String resource, int amount, String player){
        this.containerId = containerId;
        this.resource = resource;
        this.amount = amount;
        this.player = player;
        checkFormat();
    }

    @Override
    public void apply(ClientModel client) {
        ClientPlayer clientPlayer = client.getPlayerByName(player);

        ClientBaseStorage baseTarget;
        try{
            baseTarget = clientPlayer.getBaseStorageById(containerId);
            baseTarget.selectResources(resource, amount);
        }catch(NoSuchElementException e){
            ClientShelf shelfTarget;
            shelfTarget = clientPlayer.getClientShelfById(containerId);
            shelfTarget.selectResources(resource, amount);
        }
    }

    @Override
    public void checkFormat() {
        if(containerId == null)
            throw new NullPointerException("Pointer to containerId is null");
        if(resource == null)
            throw new NullPointerException("Pointer to resource is null");
        if(amount <= 0)
            throw new IllegalArgumentException("Amount of selected resources cannot be negative or zero");
        if(player == null)
            throw new NullPointerException("Pointer to player is null");
    }


    public String getContainerId() {
        return containerId;
    }

    public String getResource() {
        return resource;
    }

    public int getAmount() {
        return amount;
    }

    public String getPlayer() {
        return player;
    }
}
