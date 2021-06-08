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
    private final List<String> serverErrors;
    private Set<PossibleActions> possibleActions;
    private boolean errorConfirmed;
    private boolean gameStarted;

    public PersonalData(){
        username = "Unknown";
        gameName = "Unknown";
        serverMessages = new ArrayList<>();
        possibleActions = new HashSet<>();
        serverErrors = new ArrayList<>();
        gameStarted = false;
    }

    public synchronized void setUsername(String username){
        this.username = username;
        update();
    }

    public synchronized void setGameName(String gameName){
        this.gameName = gameName;
        update();
    }

    public synchronized void addServerMessage(String message){
        serverMessages.add(message);
        update();
    }

    public synchronized void addError(String error){
        serverErrors.add(error);
        update();
    }

    public synchronized void setPossibleActions(Set<PossibleActions> possibleActions){
        this.possibleActions = possibleActions;
        update();
    }

    public synchronized void setErrorConfirmed(boolean errorConfirmed) {
        this.errorConfirmed = errorConfirmed;
        update();
    }

    public synchronized void setGameStarted(){
        this.gameStarted = true;
        update();
    }

    public synchronized String getUsername() {
        return username;
    }

    public synchronized String getGameName() {
        return gameName;
    }

    public synchronized Set<PossibleActions> getPossibleActions() {
        return new HashSet<>(possibleActions);
    }

    public synchronized List<String> getServerMessages() {
        return new ArrayList<>(serverMessages);
    }

    public synchronized List<String> getServerErrors() {
        return new ArrayList<>(serverErrors);
    }

    public synchronized boolean isErrorConfirmed() {
        return errorConfirmed;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    @Override
    public synchronized void addListener(Listener<PersonalData> listener) {
        listeners.add(listener);
    }

    @Override
    public synchronized void update() {
        for(Listener<PersonalData> l : listeners){
            l.update(this);
        }
    }
}
