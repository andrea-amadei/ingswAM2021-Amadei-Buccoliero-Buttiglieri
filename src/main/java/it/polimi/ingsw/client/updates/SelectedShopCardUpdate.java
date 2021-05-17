package it.polimi.ingsw.client.updates;

import it.polimi.ingsw.client.model.ClientModel;

public class SelectedShopCardUpdate implements Update{

    private final int x;
    private final int y;
    private final String player;

    public SelectedShopCardUpdate(int x, int y, String player){
        this.x = x;
        this.y = y;
        this.player = player;
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
        if(player == null)
            throw new NullPointerException("Pointer to player is null");
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getPlayer() {
        return player;
    }
}
