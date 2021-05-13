package it.polimi.ingsw.clientproto.updates;

import it.polimi.ingsw.clientproto.model.ClientModel;

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
