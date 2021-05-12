package it.polimi.ingsw.clientproto.updates;

import it.polimi.ingsw.clientproto.model.ClientModel;

public class SetUsernameUpdate implements Update{

    private final String username;

    public SetUsernameUpdate(String username){
        this.username = username;
        checkFormat();
    }
    @Override
    public void apply(ClientModel client) {
        client.setUsername(username);
    }

    @Override
    public void checkFormat() {
        if(username == null)
            throw new NullPointerException();
    }
}
