package it.polimi.ingsw.client.updates;

import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.client.model.ClientPlayer;

public class DiscardLeaderCardUpdate implements Update{

    private final int id;
    private final String player;

    public DiscardLeaderCardUpdate(int id, String player){
        this.id = id;
        this.player = player;
        checkFormat();
    }

    @Override
    public void apply(ClientModel client) {
        ClientPlayer clientPlayer = client.getPlayerByName(player);
        clientPlayer.getLeaderCards().removeLeaderCardById(id);
    }

    @Override
    public void checkFormat() {
        if(id < 0)
            throw new IllegalArgumentException("id cannot be negative");
        if(player == null)
            throw new NullPointerException("Pointer to player is null");
    }

    public int getId() {
        return id;
    }

    public String getPlayer() {
        return player;
    }
}
