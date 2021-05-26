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

    public PersonalData(){
        username = "Unknown";
        gameName = "Unknown";
        serverMessages = new ArrayList<>();
        possibleActions = new HashSet<>();
        serverErrors = new ArrayList<>();
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

    public void addError(String error){
        serverErrors.add(error);
        update();
    }

    public void setPossibleActions(Set<PossibleActions> possibleActions){
        this.possibleActions = possibleActions;
        update();
    }

    public void setErrorConfirmed(boolean errorConfirmed) {
        this.errorConfirmed = errorConfirmed;
        update();
    }

    public String getUsername() {
        return username;
    }

    public String getGameName() {
        return gameName;
    }

    public Set<PossibleActions> getPossibleActions() {
        return possibleActions;
    }

    public List<String> getServerMessages() {
        return serverMessages;
    }

    public List<String> getServerErrors() {
        return serverErrors;
    }

    public boolean isErrorConfirmed() {
        return errorConfirmed;
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
