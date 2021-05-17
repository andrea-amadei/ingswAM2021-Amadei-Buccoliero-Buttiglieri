package it.polimi.ingsw.client.updates;

import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.parser.raw.RawStorage;

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
