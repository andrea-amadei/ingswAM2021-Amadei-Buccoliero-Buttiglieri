package it.polimi.ingsw.server.clienthandling;

import it.polimi.ingsw.server.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WelcomeHandler {
    private ServerSocket serverSocket;

    @SuppressWarnings("InfiniteLoopStatement")
    public WelcomeHandler(int port){

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(true){
            Socket clientSocket;

            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Logger.log("A new socket has been accepted");



        }
    }
}
