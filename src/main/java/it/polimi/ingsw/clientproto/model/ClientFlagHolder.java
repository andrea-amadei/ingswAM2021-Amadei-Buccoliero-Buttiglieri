package it.polimi.ingsw.clientproto.model;

import it.polimi.ingsw.clientproto.observables.Listener;
import it.polimi.ingsw.clientproto.observables.Observable;
import it.polimi.ingsw.gamematerials.FlagColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientFlagHolder implements Observable<ClientFlagHolder> {

    private final List<Listener<ClientFlagHolder>> listeners;
    private final Map<FlagColor, Map<Integer, Integer>> flags;

    public ClientFlagHolder(){
        flags = new HashMap<>();
        listeners = new ArrayList<>();
    }

    public Map<FlagColor, Map<Integer, Integer>> getFlags() {
        return flags;
    }

    public void addFlag(FlagColor color, Integer level){
        flags.putIfAbsent(color, new HashMap<>());
        flags.get(color).putIfAbsent(level, 0);
        flags.get(color).put(level, flags.get(color).get(level) + 1);
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
