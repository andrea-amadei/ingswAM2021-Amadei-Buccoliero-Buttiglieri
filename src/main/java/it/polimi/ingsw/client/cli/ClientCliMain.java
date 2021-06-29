package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.ClientDisconnectionManager;
import it.polimi.ingsw.client.InputReader;
import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.cli.framework.CliFramework;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.exceptions.UnableToDrawElementException;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class ClientCliMain {

    public static void main(String[] args) throws UnableToDrawElementException {

        String host;
        int port;

        try {
            if (args.length == 0) {
                host = "localhost";
                port = 6789;
            } else if (args.length == 2){
                host = args[0];
                port = Integer.parseInt(args[1]);
            }
            else{
                System.out.println("Wrong parameters format");
                return;
            }
        }catch(RuntimeException e){
            System.out.println("Wrong parameters format");
            return;
        }

        ClientModel client = new ClientModel();
        CliFramework framework = new CliFramework(true);

        CliBuilder.createStartFrame(framework, client);

        ServerHandler serverHandler;
        try {
            serverHandler = new ServerHandler(host, port, client, framework);
        }catch(IOException e){
            System.out.println(e.getMessage());
            return;
        }catch(RuntimeException ex){
            System.out.println("I'm afraid your journey ends here, traveler");
            return;
        }
        serverHandler.start();

        ClientDisconnectionManager disconnectionManager = new ClientDisconnectionManager(5000, serverHandler);
        disconnectionManager.start();

        InputReader inputReader = new InputReader(serverHandler, framework);
        inputReader.start();

        framework.renderActiveFrame();
    }
}
