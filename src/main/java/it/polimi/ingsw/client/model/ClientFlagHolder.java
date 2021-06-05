package it.polimi.ingsw.client.model;

import it.polimi.ingsw.client.observables.Listener;
import it.polimi.ingsw.client.observables.Observable;
import it.polimi.ingsw.parser.raw.RawLevelFlag;

import java.util.*;

public class ClientFlagHolder implements Observable<ClientFlagHolder> {

    private final List<Listener<ClientFlagHolder>> listeners;
    private final Set<RawLevelFlag> flags;

    public ClientFlagHolder(){
        flags = new HashSet<>();
        listeners = new ArrayList<>();
    }

    public synchronized Set<RawLevelFlag> getFlags() {
        return new HashSet<>(flags);
    }

    public synchronized void addFlag(RawLevelFlag flag){
        flags.add(flag);
        update();
    }

    @Override
    public synchronized void addListener(Listener<ClientFlagHolder> listener) {
        listeners.add(listener);
    }

    @Override
    public synchronized void update() {
        for(Listener<ClientFlagHolder> l : listeners)
            l.update(this);
    }
}
