package it.polimi.ingsw.client.model;

import it.polimi.ingsw.client.observables.Listener;
import it.polimi.ingsw.client.observables.Observable;
import it.polimi.ingsw.parser.raw.RawStorage;

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

    public RawStorage getStorage() {
        return storage;
    }

    public Map<String, Integer> getSelectedResources() {
        return selectedResources;
    }

    public void changeResources(RawStorage delta){
        if(delta == null)
            throw new NullPointerException();
        storage = storage.sum(delta);
        update();
    }

    public void selectResources(String resourceType, Integer amount){
        resourceType = resourceType.toLowerCase();
        selectedResources.putIfAbsent(resourceType, 0);
        selectedResources.put(resourceType, selectedResources.get(resourceType) + amount);

        update();
    }

    @Override
    public void addListener(Listener<ClientBaseStorage> listener) {
        listeners.add(listener);
    }

    @Override
    public void update() {
        for(Listener<ClientBaseStorage> l : listeners)
            l.update(this);
    }
}
