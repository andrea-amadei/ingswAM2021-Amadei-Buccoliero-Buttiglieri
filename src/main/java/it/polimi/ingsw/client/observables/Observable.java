package it.polimi.ingsw.client.observables;

public interface Observable <T>{
    void addListener(Listener<T> listener);
    void update();
}
