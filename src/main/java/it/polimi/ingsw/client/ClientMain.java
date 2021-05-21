package it.polimi.ingsw.client;

import it.polimi.ingsw.client.cli.CliBuilder;
import it.polimi.ingsw.client.cli.framework.CliFramework;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.exceptions.UnableToDrawElementException;

import java.util.ArrayList;

public class ClientMain {

    public static void main(String[] args) throws UnableToDrawElementException {
        ClientModel client = new ClientModel();
        CliFramework framework = new CliFramework(true);

        CliBuilder.createStartFrame(framework, client);

        ServerHandler serverHandler = new ServerHandler(6789, client, framework);
        serverHandler.start();

        InputReader inputReader = new InputReader(serverHandler, framework);
        inputReader.start();

        framework.renderActiveFrame();
    }
}
