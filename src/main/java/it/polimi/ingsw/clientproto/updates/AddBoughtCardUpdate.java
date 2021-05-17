package it.polimi.ingsw.clientproto.updates;

import it.polimi.ingsw.clientproto.model.ClientModel;

public class AddBoughtCardUpdate implements Update{

    private final int amount;
    private final String player;

    public AddBoughtCardUpdate(int amount, String player){
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
            throw new IllegalArgumentException("Amount of card to buy cannot be negative");
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
