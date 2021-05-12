package it.polimi.ingsw.clientproto.model;

import it.polimi.ingsw.clientproto.OutputHandler;

import java.util.ArrayList;
import java.util.List;

public class ClientModel {

    private String username;
    private List<String> serverMessages;
    private OutputHandler outputHandler;

    public ClientModel(OutputHandler outputHandler){
        username = null;
        serverMessages = new ArrayList<>();
        this.outputHandler = outputHandler;
    }


    public void setUsername(String username){
        this.username = username;
        outputHandler.update();
    }

    public void addServerMessages(String message){
        serverMessages.add(message);
        outputHandler.update();
    }



    public String getUsername() {
        return username;
    }
    public List<String> getServerMessages() {
        return serverMessages;
    }
}
