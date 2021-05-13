package it.polimi.ingsw.server;

import it.polimi.ingsw.server.clienthandling.WelcomeHandler;

public class ServerMain {
    public static void main(String[] args){
        WelcomeHandler welcomeHandler = new WelcomeHandler(6789);
        welcomeHandler.startServer();
    }
}
