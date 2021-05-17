package it.polimi.ingsw.clientproto.updates;

import it.polimi.ingsw.clientproto.model.ClientModel;

public class AddFaithUpdate implements Update{

    private final int amount;
    private final String player;

    public AddFaithUpdate(int amount, String player){
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
            throw new IllegalArgumentException("amount of faith point to be added cannot be negative or zero");
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
