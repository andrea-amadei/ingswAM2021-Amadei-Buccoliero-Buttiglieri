package it.polimi.ingsw.server.clienthandling.setupactions;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.server.clienthandling.ClientHandler;
import it.polimi.ingsw.server.clienthandling.ClientNetworkObject;

import java.util.List;

public interface SetupAction extends ClientNetworkObject {
    void execute(ClientHandler clientHandler);

    void checkFormat();
}
