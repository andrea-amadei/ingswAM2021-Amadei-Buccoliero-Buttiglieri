package it.polimi.ingsw.clientproto.updates;

import it.polimi.ingsw.clientproto.model.ClientModel;

public class ChangeCurrentPlayerUpdate implements Update{

    private final String newPlayer;

    public ChangeCurrentPlayerUpdate(String newPlayer){
        this.newPlayer = newPlayer;
        checkFormat();
    }

    @Override
    public void apply(ClientModel client) {

    }

    @Override
    public void checkFormat() {
        if(newPlayer == null)
            throw new NullPointerException("pointer to new player is null");
    }

    public String getNewPlayer() {
        return newPlayer;
    }
}
