package it.polimi.ingsw.clientproto.model;

import it.polimi.ingsw.clientproto.observables.Listener;
import it.polimi.ingsw.clientproto.observables.Observable;
import it.polimi.ingsw.parser.raw.RawLevelFlag;

import java.util.*;

public class ClientFlagHolder implements Observable<ClientFlagHolder> {

    private final List<Listener<ClientFlagHolder>> listeners;
    private final Set<RawLevelFlag> flags;

    public ClientFlagHolder(){
        flags = new HashSet<>();
        listeners = new ArrayList<>();
    }

    public Set<RawLevelFlag> getFlags() {
        return flags;
    }

    public void addFlag(RawLevelFlag flag){
        flags.add(flag);
        update();
    }

    @Override
    public void addListener(Listener<ClientFlagHolder> listener) {
        listeners.add(listener);
    }

    @Override
    public void update() {
        for(Listener<ClientFlagHolder> l : listeners)
            l.update(this);
    }
}
