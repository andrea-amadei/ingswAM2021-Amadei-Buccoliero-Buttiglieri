package it.polimi.ingsw.clientproto.observables;

public interface Listener<T>{
    void update(T t);
}
