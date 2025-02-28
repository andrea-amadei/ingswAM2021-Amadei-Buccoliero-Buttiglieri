package it.polimi.ingsw.client.updates;

import it.polimi.ingsw.client.clientmodel.ClientModel;
import it.polimi.ingsw.client.clientmodel.ClientPlayer;

public class ChangeCoveredLeaderCardUpdate implements Update{

    private final int delta;
    private final String player;

    public ChangeCoveredLeaderCardUpdate(int delta, String player){
        this.delta = delta;
        this.player = player;
        checkFormat();
    }

    @Override
    public void apply(ClientModel client) {
        ClientPlayer clientPlayer = client.getPlayerByName(player);
        clientPlayer.getLeaderCards().changeCoveredCardsNumber(delta);
    }

    @Override
    public void checkFormat() {
        if(delta == 0)
            throw new IllegalArgumentException("delta cannot be zero");
        if(player == null)
            throw new NullPointerException("Pointer to player is null");
    }

    public int getDelta() {
        return delta;
    }

    public String getPlayer() {
        return player;
    }
}
