package it.polimi.ingsw.client.updates;

import it.polimi.ingsw.client.clientmodel.ClientModel;
import it.polimi.ingsw.client.clientmodel.ClientPlayer;

public class AddDiscountUpdate implements Update{

    private final String resource;
    private final int discount;
    private final String player;

    public AddDiscountUpdate(String resource, int discount, String player){
        this.resource = resource;
        this.discount = discount;
        this.player = player;
        checkFormat();
    }

    @Override
    public void apply(ClientModel client) {
        ClientPlayer clientPlayer = client.getPlayerByName(player);
        clientPlayer.getDiscountHolder().addDiscount(resource, discount);
    }

    @Override
    public void checkFormat() {
        if(resource == null)
            throw new NullPointerException("pointer to resources is null");
        if(discount <= 0)
            throw new IllegalArgumentException("discount amount cannot be negative or zero");
        if(player == null)
            throw new NullPointerException("Pointer to player is null");
    }

    public String getResource() {
        return resource;
    }

    public int getDiscount() {
        return discount;
    }

    public String getPlayer() {
        return player;
    }
}
