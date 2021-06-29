package it.polimi.ingsw.client.clientmodel;

import it.polimi.ingsw.client.observables.Listener;
import it.polimi.ingsw.client.observables.Observable;
import it.polimi.ingsw.server.model.holder.FaithHolder;
import it.polimi.ingsw.common.parser.raw.RawFaithPathGroup;
import it.polimi.ingsw.common.parser.raw.RawFaithPathTile;

import java.util.ArrayList;
import java.util.List;

public class ClientFaithPath implements Observable<ClientFaithPath> {

    private final List<Listener<ClientFaithPath>> listeners;
    private final List<RawFaithPathTile> tiles;
    private final List<RawFaithPathGroup> faithGroups;
    private final List<FaithHolder.CheckpointStatus> checkpointStatus;

    private int faithPoints;
    private int lorenzoFaith;

    public ClientFaithPath(List<RawFaithPathTile> tiles, List<RawFaithPathGroup> faithGroups){
        this.tiles = tiles;
        this.listeners = new ArrayList<>();
        this.faithGroups = faithGroups;
        this.checkpointStatus = new ArrayList<>();
        for(int i = 0; i < faithGroups.size(); i++)
            checkpointStatus.add(FaithHolder.CheckpointStatus.UNREACHED);

        faithPoints = 0;
        lorenzoFaith = 0;
    }

    public synchronized void addFaithPoints(int amount){
        faithPoints += amount;
        update();
    }

    public synchronized void addLorenzoFaith(int amount){
        lorenzoFaith += amount;
        update();
    }

    public synchronized void changeCardStatus(int index, FaithHolder.CheckpointStatus status){
        checkpointStatus.set(index, status);
        update();
    }

    public synchronized List<RawFaithPathTile> getTiles() {
        return new ArrayList<>(tiles);
    }

    public synchronized List<RawFaithPathGroup> getFaithGroups() {
        return new ArrayList<>(faithGroups);
    }

    public synchronized List<FaithHolder.CheckpointStatus> getCheckpointStatus() {
        return new ArrayList<>(checkpointStatus);
    }

    public synchronized int getFaithPoints() {
        return faithPoints;
    }

    public synchronized int getLorenzoFaith() {
        return lorenzoFaith;
    }

    @Override
    public synchronized void addListener(Listener<ClientFaithPath> listener) {
        listeners.add(listener);
    }

    @Override
    public synchronized void update() {
        for(Listener<ClientFaithPath> l : listeners)
            l.update(this);
    }
}
