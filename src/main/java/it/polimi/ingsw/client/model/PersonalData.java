package it.polimi.ingsw.client.model;

import it.polimi.ingsw.client.observables.Listener;
import it.polimi.ingsw.client.observables.Observable;
import it.polimi.ingsw.common.payload_components.groups.PossibleActions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PersonalData implements Observable<PersonalData> {
    List<Listener<PersonalData>> listeners = new ArrayList<>();

    private String username;
    private String gameName;
    private final List<String> serverMessages;
    private Set<PossibleActions> possibleActions;

    public PersonalData(){
        username = "Unknown";
        gameName = "Unknown";
        serverMessages = new ArrayList<>();
        possibleActions = new HashSet<>();
    }

    public void setUsername(String username){
        this.username = username;
        update();
    }

    public void setGameName(String gameName){
        this.gameName = gameName;
        update();
    }

    public void addServerMessage(String message){
        serverMessages.add(message);
        update();
    }

    public void setPossibleActions(Set<PossibleActions> possibleActions){
        this.possibleActions = possibleActions;
        update();
    }

    public String getUsername() {
        return username;
    }

    public String getGameName() {
        return gameName;
    }

    public List<String> getServerMessages() {
        return serverMessages;
    }

    @Override
    public void addListener(Listener<PersonalData> listener) {
        listeners.add(listener);
    }

    @Override
    public void update() {
        for(Listener<PersonalData> l : listeners){
            l.update(this);
        }
    }
}
