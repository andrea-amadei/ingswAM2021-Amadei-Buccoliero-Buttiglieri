package it.polimi.ingsw.client;

import it.polimi.ingsw.common.exceptions.UnableToDrawElementException;
import it.polimi.ingsw.server.Logger;

import java.io.IOException;

public class ClientDisconnectionManager extends Thread{
    private final int timeout;
    private final ServerHandler serverHandler;

    public ClientDisconnectionManager(int timeout, ServerHandler serverHandler){
        this.timeout = timeout;
        this.serverHandler = serverHandler;
    }


    @Override
    public void run(){
        while(true){
            serverHandler.setPingReceived(false);
            try {
                sleep(timeout);
            } catch (InterruptedException e) {
                Logger.log("Disconnection manager interrupted (exc)");
            }

            if(!serverHandler.isPingReceived()) {
                try {
                    serverHandler.disconnect();
                } catch (IOException e) {
                    Logger.log("IOException in ClientDisconnectionManager while disconnecting the client");
                }finally{
                    serverHandler.getClient().getEndGameResults().crashGame();
                    if(serverHandler.getFramework() != null) {
                        try {
                            serverHandler.getFramework().renderActiveFrame();
                        } catch (UnableToDrawElementException e) {
                            e.printStackTrace();
                        }
                    }
                }

                break;
            }
        }
    }

}
