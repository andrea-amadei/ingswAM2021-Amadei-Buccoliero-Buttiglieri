package it.polimi.ingsw.server.clienthandling;

import it.polimi.ingsw.server.Logger;
import it.polimi.ingsw.server.ServerManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WelcomeHandler {
    private ServerSocket serverSocket;
    private final ServerManager serverManager;

    public WelcomeHandler(int port){
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        serverManager = new ServerManager();

    }

    public void startServer() {
        while (true) {
            Socket clientSocket;

            try {
                clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket, serverManager)).start();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }

            Logger.log("A new socket has been accepted");

        }

    }
}
