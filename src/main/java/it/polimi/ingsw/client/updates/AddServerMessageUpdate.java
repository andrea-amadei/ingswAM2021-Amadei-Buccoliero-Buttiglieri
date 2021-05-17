package it.polimi.ingsw.client.updates;

import it.polimi.ingsw.client.model.ClientModel;

public class AddServerMessageUpdate implements Update{

    private final String text;

    public AddServerMessageUpdate(String text){
        this.text = text;
        checkFormat();
    }
    @Override
    public void apply(ClientModel client) {
        client.getPersonalData().addServerMessage(text);
        client.getOutputHandler().update();
    }

    @Override
    public void checkFormat() {
        if(text == null)
            throw new NullPointerException("Null message form server");
    }
}
