package it.polimi.ingsw.clientproto;

import it.polimi.ingsw.clientproto.model.ClientModel;

import java.util.Optional;

public class OutputHandler {
    private ClientModel clientModel;

    public void setModel(ClientModel clientModel){
        this.clientModel = clientModel;
    }

    public void update(){
        for(int i = 0; i < 15; i++)
            System.out.println();

        System.out.println("Username: " + Optional.ofNullable(clientModel.getUsername()).orElse("Unknown"));

        System.out.println("-----------------------------------");
        System.out.println("Messages from server");
        System.out.println("-----------------------------------");

        for(String s : clientModel.getServerMessages())
            System.out.println(s);

        for(int i = 0; i < 15; i++)
            System.out.println();
    }
}
