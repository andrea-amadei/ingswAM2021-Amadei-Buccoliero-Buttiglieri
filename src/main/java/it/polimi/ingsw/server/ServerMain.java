package it.polimi.ingsw.server;

import it.polimi.ingsw.server.clienthandling.WelcomeHandler;

import java.io.IOException;

public class ServerMain {
    public static void main(String[] args){

        int port;
        try{
            if(args.length == 0)
                port = 6789;
            else if(args.length == 1)
                port = Integer.parseInt(args[0]);
            else{
                System.out.println("Wrong parameters format");
                return;
            }
        }catch(RuntimeException e){
            System.out.println("Wrong parameters format");
            return;
        }

        WelcomeHandler welcomeHandler;
        try {
            welcomeHandler = new WelcomeHandler(port);
        }catch(IOException e){
            System.out.println(e.getMessage());
            return;
        }
        Logger.log("Server started on port " + port + "!");

        welcomeHandler.startServer();
    }
}
