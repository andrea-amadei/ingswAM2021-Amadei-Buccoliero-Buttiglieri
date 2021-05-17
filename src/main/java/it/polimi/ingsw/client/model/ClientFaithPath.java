package it.polimi.ingsw.client.model;

import it.polimi.ingsw.client.observables.Listener;
import it.polimi.ingsw.client.observables.Observable;
import it.polimi.ingsw.model.holder.FaithHolder;
import it.polimi.ingsw.parser.raw.RawFaithPathGroup;
import it.polimi.ingsw.parser.raw.RawFaithPathTile;

import java.util.ArrayList;
import java.util.List;

public class ClientFaithPath implements Observable<ClientFaithPath> {

    private final List<Listener<ClientFaithPath>> listeners;
    private final List<RawFaithPathTile> tiles;
    private final List<RawFaithPathGroup> faithGroups;
    private final List<FaithHolder.CheckpointStatus> checkpointStatus;

    private int faithPoints;

    public ClientFaithPath(List<RawFaithPathTile> tiles, List<RawFaithPathGroup> faithGroups){
        this.tiles = tiles;
        this.listeners = new ArrayList<>();
        this.faithGroups = faithGroups;
        this.checkpointStatus = new ArrayList<>();
        for(int i = 0; i < faithGroups.size(); i++)
            checkpointStatus.add(FaithHolder.CheckpointStatus.UNREACHED);

        faithPoints = 0;
    }

    public void addFaithPoints(int amount){
        faithPoints += amount;
        update();
    }

    public void changeCardStatus(int index, FaithHolder.CheckpointStatus status){
        checkpointStatus.set(index, status);
        update();
    }

    public List<RawFaithPathTile> getTiles() {
        return tiles;
    }

    public List<RawFaithPathGroup> getFaithGroups() {
        return faithGroups;
    }

    public List<FaithHolder.CheckpointStatus> getCheckpointStatus() {
        return checkpointStatus;
    }

    public int getFaithPoints() {
        return faithPoints;
    }

    @Override
    public void addListener(Listener<ClientFaithPath> listener) {
        listeners.add(listener);
    }

    @Override
    public void update() {
        for(Listener<ClientFaithPath> l : listeners)
            l.update(this);
    }
}
