package it.polimi.ingsw.client.updates;

import it.polimi.ingsw.client.model.ClientModel;

public class AddPointsUpdate implements Update{

    private final int amount;
    private final String player;

    public AddPointsUpdate(int amount, String player){
        this.amount = amount;
        this.player = player;
        checkFormat();
    }

    @Override
    public void apply(ClientModel client) {

    }

    @Override
    public void checkFormat() {
        if(amount <= 0)
            throw new IllegalArgumentException("amount of victory points to add cannot be negative or zero");
        if(player == null)
            throw new NullPointerException("Pointer to player is null");
    }

    public int getAmount() {
        return amount;
    }

    public String getPlayer() {
        return player;
    }
}
