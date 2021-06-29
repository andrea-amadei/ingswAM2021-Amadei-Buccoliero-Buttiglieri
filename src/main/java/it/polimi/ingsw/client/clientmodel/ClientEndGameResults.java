package it.polimi.ingsw.client.clientmodel;

import it.polimi.ingsw.client.observables.Listener;
import it.polimi.ingsw.client.observables.Observable;

import java.util.ArrayList;
import java.util.List;

public class ClientEndGameResults implements Observable<ClientEndGameResults> {

    private final List<Listener<ClientEndGameResults>> listeners;
    private boolean isGameEnded;
    private boolean isGameCrashed;

    private boolean hasLorenzoWon;
    private List<String> usernames;
    private List<Integer> points;

    public ClientEndGameResults(){
        isGameEnded = false;
        isGameCrashed = false;
        hasLorenzoWon = false;
        usernames = new ArrayList<>();
        points = new ArrayList<>();

        listeners = new ArrayList<>();
    }

    public synchronized void endGame(boolean hasLorenzoWon, List<String> usernames, List<Integer> points){
        isGameEnded = true;
        this.hasLorenzoWon = hasLorenzoWon;
        this.usernames = usernames;
        this.points = points;

        update();
    }

    public synchronized void crashGame(){
        if(!isGameEnded) {
            isGameCrashed = true;
            update();
        }
    }

    @Override
    public synchronized void addListener(Listener<ClientEndGameResults> listener) {
        listeners.add(listener);
    }

    @Override
    public synchronized void update() {
        for(Listener<ClientEndGameResults> l : listeners)
            l.update(this);
    }

    public synchronized boolean isGameEnded() {
        return isGameEnded;
    }

    public synchronized boolean isHasLorenzoWon() {
        return hasLorenzoWon;
    }

    public synchronized List<String> getUsernames() {
        return usernames;
    }

    public synchronized List<Integer> getPoints() {
        return points;
    }

    public synchronized boolean isGameCrashed(){
        return isGameCrashed;
    }
}
