package it.polimi.ingsw.clientproto.updates;

import it.polimi.ingsw.clientproto.model.ClientModel;

public class AddUpgradableCraftingUpdate implements Update{

    private final int id;
    private final int index;
    private final String player;

    public AddUpgradableCraftingUpdate(int id, int index, String player){
        this.id = id;
        this.index = index;
        this.player = player;
        checkFormat();
    }

    @Override
    public void apply(ClientModel client) {

    }

    @Override
    public void checkFormat() {
        if(id < 0)
            throw new IllegalArgumentException("id cannot be negative");
        if(index < 0)
            throw new IllegalArgumentException("index cannot be negative");
        if(player == null)
            throw new NullPointerException("Pointer to player is null");
    }

    public int getId() {
        return id;
    }

    public int getIndex() {
        return index;
    }

    public String getPlayer() {
        return player;
    }
}
