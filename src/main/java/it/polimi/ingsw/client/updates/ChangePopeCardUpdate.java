package it.polimi.ingsw.client.updates;

import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.client.model.ClientPlayer;
import it.polimi.ingsw.model.holder.FaithHolder;

public class ChangePopeCardUpdate implements Update{

    private final FaithHolder.CheckpointStatus status;
    private final int index;
    private final String player;

    public ChangePopeCardUpdate(FaithHolder.CheckpointStatus status, int index, String player){
        this.status = status;
        this.index = index;
        this.player = player;
        checkFormat();
    }

    @Override
    public void apply(ClientModel client) {
        ClientPlayer clientPlayer = client.getPlayerByName(player);
        clientPlayer.getFaithPath().changeCardStatus(index, status);
    }

    @Override
    public void checkFormat() {
        if(status == null)
            throw new NullPointerException("Pointer to status is null");
        if(index < 0)
            throw new IllegalArgumentException("Index cannot be negative");
        if(player == null)
            throw new NullPointerException("Pointer to player is null");
    }

    public FaithHolder.CheckpointStatus getStatus() {
        return status;
    }

    public int getIndex() {
        return index;
    }

    public String getPlayer() {
        return player;
    }
}
