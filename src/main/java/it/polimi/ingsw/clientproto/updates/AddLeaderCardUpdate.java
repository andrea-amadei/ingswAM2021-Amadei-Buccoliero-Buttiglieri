package it.polimi.ingsw.clientproto.updates;

import it.polimi.ingsw.clientproto.model.ClientModel;

public class AddLeaderCardUpdate implements Update{

    private final int id;
    private final String player;

    public AddLeaderCardUpdate(int id, String player){
        this.id = id;
        this.player = player;
        checkFormat();
    }

    @Override
    public void apply(ClientModel client) {

    }

    @Override
    public void checkFormat() {
        if(id < 0)
            throw new IllegalArgumentException("leader ID cannot be negative");
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
