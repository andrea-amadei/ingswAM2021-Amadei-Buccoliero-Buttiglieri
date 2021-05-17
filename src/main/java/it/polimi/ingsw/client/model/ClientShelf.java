package it.polimi.ingsw.client.model;

import it.polimi.ingsw.client.observables.Listener;
import it.polimi.ingsw.client.observables.Observable;
import it.polimi.ingsw.parser.raw.RawStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientShelf implements Observable<ClientShelf> {

    private final List<Listener<ClientShelf>> listeners;

    private RawStorage rawStorage;
    private final String acceptedType;
    private final Map<String, Integer> selectedResources;

    public ClientShelf(String id, String acceptedType){
        this.listeners = new ArrayList<>();
        this.rawStorage = new RawStorage(id, new HashMap<>());
        this.acceptedType = acceptedType;
        this.selectedResources = new HashMap<>();
    }

    public ClientShelf(String id, String acceptedType, RawStorage rawStorage){
        this(id, acceptedType);
        this.rawStorage = rawStorage;
    }

    public void changeResources(RawStorage delta){
        rawStorage = rawStorage.sum(delta);
        update();
    }

    public void selectResources(String resourceType, Integer amount){
        resourceType = resourceType.toLowerCase();
        selectedResources.putIfAbsent(resourceType, 0);
        selectedResources.put(resourceType, selectedResources.get(resourceType) + amount);
        update();
    }

    public RawStorage getStorage() {
        return rawStorage;
    }

    public String getAcceptedType() {
        return acceptedType;
    }

    public Map<String, Integer> getSelectedResources() {
        return selectedResources;
    }

    @Override
    public void addListener(Listener<ClientShelf> listener) {
        listeners.add(listener);
    }

    @Override
    public void update() {
        for(Listener<ClientShelf> l : listeners)
            l.update(this);
    }
}
