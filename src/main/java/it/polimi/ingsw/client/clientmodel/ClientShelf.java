package it.polimi.ingsw.client.clientmodel;

import it.polimi.ingsw.client.observables.Listener;
import it.polimi.ingsw.client.observables.Observable;
import it.polimi.ingsw.common.parser.raw.RawStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientShelf implements Observable<ClientShelf> {

    private final List<Listener<ClientShelf>> listeners;

    private RawStorage rawStorage;
    private final String acceptedType;
    private final Map<String, Integer> selectedResources;
    private final int size;

    public ClientShelf(String id, String acceptedType, int size){
        this.listeners = new ArrayList<>();
        this.rawStorage = new RawStorage(id, new HashMap<>());
        this.acceptedType = acceptedType;
        this.selectedResources = new HashMap<>();
        this.size = size;
    }

    public ClientShelf(String id, String acceptedType, int size, RawStorage rawStorage){
        this(id, acceptedType, size);
        this.rawStorage = rawStorage;
    }

    public synchronized void changeResources(RawStorage delta){
        rawStorage = rawStorage.sum(delta);
        update();
    }

    public synchronized void selectResources(String resourceType, Integer amount){
        resourceType = resourceType.toLowerCase();
        selectedResources.putIfAbsent(resourceType, 0);
        selectedResources.put(resourceType, selectedResources.get(resourceType) + amount);
        update();
    }

    public synchronized void unselect(){
        selectedResources.clear();
        update();
    }

    public synchronized RawStorage getStorage() {
        return rawStorage;
    }

    public synchronized String getAcceptedType() {
        return acceptedType;
    }

    public synchronized int getSize() {
        return size;
    }

    public synchronized Map<String, Integer> getSelectedResources() {
        return selectedResources;
    }

    @Override
    public synchronized void addListener(Listener<ClientShelf> listener) {
        listeners.add(listener);
    }

    @Override
    public synchronized void update() {
        for(Listener<ClientShelf> l : listeners)
            l.update(this);
    }
}
