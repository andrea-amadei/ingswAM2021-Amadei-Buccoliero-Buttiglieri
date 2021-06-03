package it.polimi.ingsw.server.clienthandling.setupactions;

import it.polimi.ingsw.server.clienthandling.ClientHandler;

public class ReconnectSetupAction implements SetupAction{
    private final String username;

    public ReconnectSetupAction(String username){
        this.username = username;
        checkFormat();
    }
    @Override
    public void execute(ClientHandler clientHandler) {
        clientHandler.reconnect(username);
    }

    @Override
    public void checkFormat() {
        if(username == null)
            throw new NullPointerException("Missing username in the reconnect action");
    }
}
