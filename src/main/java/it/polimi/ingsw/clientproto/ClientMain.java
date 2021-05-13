package it.polimi.ingsw.clientproto;

import it.polimi.ingsw.clientproto.model.ClientModel;

import java.util.ArrayList;

public class ClientMain {

    public static void main(String[] args){
        OutputHandler outputHandler = new OutputHandler();
        ClientModel client = new ClientModel(outputHandler, new ArrayList<>());
        outputHandler.setModel(client);

        outputHandler.update();
        ServerHandler serverHandler = new ServerHandler(6789, client);
        serverHandler.start();

        InputReader inputReader = new InputReader(new CommandExecutor(client), serverHandler);
        inputReader.start();
    }
}
