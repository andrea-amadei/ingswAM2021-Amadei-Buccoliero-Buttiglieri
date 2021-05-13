package it.polimi.ingsw.clientproto.model;

import it.polimi.ingsw.clientproto.observables.Listener;
import it.polimi.ingsw.clientproto.observables.Observable;

import java.util.ArrayList;
import java.util.List;

public class PersonalData implements Observable<PersonalData> {
    List<Listener<PersonalData>> listeners = new ArrayList<>();

    private String username;
    private String gameName;
    private final List<String> serverMessages;

    public PersonalData(){
        username = "Unknown";
        gameName = "Unknown";
        serverMessages = new ArrayList<>();
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
