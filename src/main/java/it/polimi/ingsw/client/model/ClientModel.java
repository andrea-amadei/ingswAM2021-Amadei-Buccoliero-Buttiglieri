package it.polimi.ingsw.client.model;

import it.polimi.ingsw.client.OutputHandler;

import java.util.List;

public class ClientModel {

    private final OutputHandler outputHandler;

    private final PersonalData personalData;
    private final List<ClientPlayer> players;

    public ClientModel(OutputHandler outputHandler, List<ClientPlayer> players){
        this.players = players;
        personalData = new PersonalData();
        this.outputHandler = outputHandler;
    }


    public PersonalData getPersonalData(){
        return personalData;
    }

    public OutputHandler getOutputHandler(){
        return this.outputHandler;
    }
}
