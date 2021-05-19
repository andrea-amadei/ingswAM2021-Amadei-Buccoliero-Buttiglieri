package it.polimi.ingsw.client.updates;

import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.client.model.ClientPlayer;
import it.polimi.ingsw.client.model.ClientShelf;

public class AddShelfUpdate implements Update{

    private final String id;
    private final String resource;
    private final int size;
    private final String player;

    public AddShelfUpdate(String id, String resource, int size, String player){
        this.id = id;
        this.resource = resource;
        this.size = size;
        this.player = player;
        checkFormat();
    }

    @Override
    public void apply(ClientModel client) {
        ClientPlayer clientPlayer = client.getPlayerByName(player);
        clientPlayer.addLeaderShelf(new ClientShelf(id, resource, size));
    }

    @Override
    public void checkFormat() {
        if(id == null)
            throw new NullPointerException("pointer to id is null");
        if(resource == null)
            throw new NullPointerException("pointer to resource is null");
        if(size < 0)
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

    public int getSize() {
        return size;
    }

    public String getPlayer() {
        return player;
    }
}
