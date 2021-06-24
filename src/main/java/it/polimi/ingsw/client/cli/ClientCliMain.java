package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.ClientDisconnectionManager;
import it.polimi.ingsw.client.InputReader;
import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.cli.framework.CliFramework;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.exceptions.UnableToDrawElementException;

import java.util.Scanner;

public class ClientCliMain {

    public static void main(String[] args) throws UnableToDrawElementException {
        System.out.println("Insert the host ip");
        Scanner scanner = new Scanner(System.in);
        String ip = scanner.nextLine();
        System.out.println("Insert the host port");
        int port = scanner.nextInt();

        ClientModel client = new ClientModel();
        CliFramework framework = new CliFramework(true);

        CliBuilder.createStartFrame(framework, client);

        ServerHandler serverHandler = new ServerHandler(ip, port, client, framework);
        serverHandler.start();

        ClientDisconnectionManager disconnectionManager = new ClientDisconnectionManager(5000, serverHandler);
        disconnectionManager.start();

        InputReader inputReader = new InputReader(serverHandler, framework);
        inputReader.start();

        framework.renderActiveFrame();
    }
}
