package it.polimi.ingsw.clientproto.observables;

public interface Observable <T>{
    void addListener(Listener<T> listener);
    void update();
}
