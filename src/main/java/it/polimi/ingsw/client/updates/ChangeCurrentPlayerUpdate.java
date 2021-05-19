package it.polimi.ingsw.client.updates;

import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.client.model.ClientPlayer;

public class ChangeCurrentPlayerUpdate implements Update{

    private final String newPlayer;

    public ChangeCurrentPlayerUpdate(String newPlayer){
        this.newPlayer = newPlayer;
        checkFormat();
    }

    @Override
    public void apply(ClientModel client) {
        ClientPlayer clientPlayer = client.getPlayerByName(newPlayer);
        client.setCurrentPlayer(clientPlayer);
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
