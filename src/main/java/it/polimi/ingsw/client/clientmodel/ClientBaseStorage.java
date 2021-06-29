package it.polimi.ingsw.client.clientmodel;

import it.polimi.ingsw.client.observables.Listener;
import it.polimi.ingsw.client.observables.Observable;
import it.polimi.ingsw.common.parser.raw.RawStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientBaseStorage implements Observable<ClientBaseStorage> {

    private final List<Listener<ClientBaseStorage>> listeners;

    private RawStorage storage;
    private final Map<String, Integer> selectedResources;

    public ClientBaseStorage(RawStorage storage){
        this.storage = storage;
        this.selectedResources = new HashMap<>();

        listeners = new ArrayList<>();
    }

    public synchronized RawStorage getStorage() {
        return storage;
    }

    public synchronized Map<String, Integer> getSelectedResources() {
        return new HashMap<>(selectedResources);
    }

    public synchronized void changeResources(RawStorage delta){
        if(delta == null)
            throw new NullPointerException();
        storage = storage.sum(delta);
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

    @Override
    public synchronized void addListener(Listener<ClientBaseStorage> listener) {
        listeners.add(listener);
    }

    @Override
    public synchronized void update() {
        for(Listener<ClientBaseStorage> l : listeners)
            l.update(this);
    }
}
