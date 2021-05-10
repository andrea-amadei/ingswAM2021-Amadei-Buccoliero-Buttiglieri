package it.polimi.ingsw.server.clienthandling;

import it.polimi.ingsw.server.Logger;
import it.polimi.ingsw.server.MatchesManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable{

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    private MatchesManager matchesManager;

    public ClientHandler(Socket clientSocket, MatchesManager matchesManager){
        this.clientSocket = clientSocket;
        this.matchesManager = matchesManager;
        if(clientSocket.isConnected()) {

            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public void run() {
        String line;
        while (true) {
            try {
                if ((line = in.readLine()) == null) break;
                Logger.log("Client sent: " + line);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
