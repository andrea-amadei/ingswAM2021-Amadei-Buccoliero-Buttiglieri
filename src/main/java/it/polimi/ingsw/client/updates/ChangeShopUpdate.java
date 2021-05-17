package it.polimi.ingsw.client.updates;

import it.polimi.ingsw.client.model.ClientModel;

public class ChangeShopUpdate implements Update{

    private final int x;
    private final int y;
    private final int id;

    public ChangeShopUpdate(int x, int y, int id){
        this.x = x;
        this.y = y;
        this.id = id;
        checkFormat();
    }

    @Override
    public void apply(ClientModel client) {

    }

    @Override
    public void checkFormat() {
        if(x < 0)
            throw new IllegalArgumentException("x coordinate cannot be negative");
        if(y < 0)
            throw new IllegalArgumentException("y coordinate cannot be negative");
        if(id < 0)
            throw new IllegalArgumentException("id cannot be negative");
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getId() {
        return id;
    }
}
