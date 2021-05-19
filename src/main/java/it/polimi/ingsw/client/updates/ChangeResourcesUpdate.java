package it.polimi.ingsw.client.updates;

import it.polimi.ingsw.client.model.ClientBaseStorage;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.client.model.ClientPlayer;
import it.polimi.ingsw.client.model.ClientShelf;
import it.polimi.ingsw.parser.raw.RawStorage;

import java.util.NoSuchElementException;

public class ChangeResourcesUpdate implements Update{

    private final RawStorage delta;
    private final String player;

    public ChangeResourcesUpdate(RawStorage delta, String player){
        this.delta = delta;
        this.player = player;
        checkFormat();
    }

    @Override
    public void apply(ClientModel client) {
        ClientPlayer clientPlayer = client.getPlayerByName(player);

        ClientBaseStorage baseTarget;
        try{
            baseTarget = clientPlayer.getBaseStorageById(delta.getId());
            baseTarget.changeResources(delta);
        }catch(NoSuchElementException e){
            ClientShelf shelfTarget;
            shelfTarget = clientPlayer.getClientShelfById(delta.getId());
            shelfTarget.changeResources(delta);
        }
    }

    @Override
    public void checkFormat() {
        if(delta == null)
            throw new NullPointerException("Pointer to delta is null");
        if(player == null)
            throw new NullPointerException("Pointer to player is null");
    }

    public RawStorage getDelta() {
        return delta;
    }

    public String getPlayer() {
        return player;
    }
}
