package it.polimi.ingsw.clientproto.model;

import it.polimi.ingsw.clientproto.observables.Listener;
import it.polimi.ingsw.clientproto.observables.Observable;
import it.polimi.ingsw.parser.raw.RawStorage;

import java.util.ArrayList;
import java.util.List;

public class ClientBaseStorage implements Observable<ClientBaseStorage> {
    List<Listener<ClientBaseStorage>> listeners;

    private RawStorage storage;

    public ClientBaseStorage(RawStorage storage){
        this.storage = storage;

        listeners = new ArrayList<>();
    }

    public RawStorage getStorage() {
        return storage;
    }

    //public void changeResources(String resource, )

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
