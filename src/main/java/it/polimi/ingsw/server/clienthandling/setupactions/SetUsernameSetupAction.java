package it.polimi.ingsw.server.clienthandling.setupactions;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.server.clienthandling.ClientHandler;

import java.util.List;

public class SetUsernameSetupAction implements SetupAction{

    private final String username;

    public SetUsernameSetupAction(String username){
        this.username = username;
        checkFormat();
    }


    @Override
    public void execute(ClientHandler clientHandler) {
        clientHandler.setUsername(username);
    }

    @Override
    public void checkFormat() {
        if(username == null)
            throw new NullPointerException("Invalid format");
    }
}
