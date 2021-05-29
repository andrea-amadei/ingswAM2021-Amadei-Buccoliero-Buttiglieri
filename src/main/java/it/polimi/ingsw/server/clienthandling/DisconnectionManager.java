package it.polimi.ingsw.server.clienthandling;

import it.polimi.ingsw.common.payload_components.groups.PingPayloadComponent;
import it.polimi.ingsw.server.Logger;
import it.polimi.ingsw.server.ServerManager;
import it.polimi.ingsw.utils.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DisconnectionManager extends Thread{

    private final int timeout;
    private final ServerManager serverManager;
    private List<Pair<String, ClientHandler>> connectedClients;
    private List<Boolean> ackList;

    public DisconnectionManager(ServerManager serverManager, int timeout){
        this.serverManager = serverManager;
        this.timeout = timeout;
    }

    @Override
    public void run(){
        while(true){
            connectedClients = serverManager.getConnectedClients();
            ackList = new ArrayList<>();
            for(Pair<String, ClientHandler> client : connectedClients){
                ackList.add(false);
                client.getSecond().sendPayload(new PingPayloadComponent());
                Logger.log("SENT PING TO: " + client.getFirst());
            }

            try {
                Thread.sleep(timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for(int i = 0; i < connectedClients.size(); i++){
                if(!ackList.get(i)){
                    try {
                        connectedClients.get(i).getSecond().disconnect();
                    } catch (IOException e) {
                        //TODO: think how to handle this exception
                        Logger.log("The client \"" + connectedClients.get(i).getFirst() + "\" launched an IO exception when ended");
                    }
                }
            }
        }
    }


    public synchronized void ack(String username){
        int index = connectedClients.stream().map(Pair::getFirst).collect(Collectors.toList()).indexOf(username);
        ackList.set(index, true);
    }
}
