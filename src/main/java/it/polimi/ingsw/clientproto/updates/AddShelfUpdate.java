package it.polimi.ingsw.clientproto.updates;

import it.polimi.ingsw.clientproto.model.ClientModel;

public class AddShelfUpdate implements Update{

    private final String id;
    private final String resource;
    private final int index;
    private final String player;

    public AddShelfUpdate(String id, String resource, int index, String player){
        this.id = id;
        this.resource = resource;
        this.index = index;
        this.player = player;
        checkFormat();
    }

    @Override
    public void apply(ClientModel client) {

    }

    @Override
    public void checkFormat() {
        if(id == null)
            throw new NullPointerException("pointer to id is null");
        if(resource == null)
            throw new NullPointerException("pointer to resource is null");
        if(index < 0)
            throw new IllegalArgumentException("index cannot be negative");
        if(player == null)
            throw new NullPointerException("Pointer to player is null");
    }

    public String getId() {
        return id;
    }

    public String getResource() {
        return resource;
    }

    public int getIndex() {
        return index;
    }

    public String getPlayer() {
        return player;
    }
}
