package it.polimi.ingsw.client.updates;

import it.polimi.ingsw.client.clientmodel.ClientModel;

public class AddLorenzoFaithUpdate implements Update{

    private final Integer amount;

    public AddLorenzoFaithUpdate(Integer amount){
        this.amount = amount;
        checkFormat();
    }

    @Override
    public void apply(ClientModel client) {
        client.getPlayers().get(0).getFaithPath().addLorenzoFaith(amount);
    }

    @Override
    public void checkFormat() {
        if(amount == null)
            throw new NullPointerException("Missing attribute 'amount'");
    }
}
