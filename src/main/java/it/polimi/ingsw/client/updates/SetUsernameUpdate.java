package it.polimi.ingsw.client.updates;

import it.polimi.ingsw.client.clientmodel.ClientModel;

public class SetUsernameUpdate implements Update{

    private final String username;

    public SetUsernameUpdate(String username){
        this.username = username;
        checkFormat();
    }
    @Override
    public void apply(ClientModel client) {
        client.getPersonalData().setUsername(username);
    }

    @Override
    public void checkFormat() {
        if(username == null)
            throw new NullPointerException();
    }
}
