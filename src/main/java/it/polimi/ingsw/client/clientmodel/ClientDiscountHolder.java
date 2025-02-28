package it.polimi.ingsw.client.clientmodel;

import it.polimi.ingsw.client.observables.Listener;
import it.polimi.ingsw.client.observables.Observable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientDiscountHolder implements Observable<ClientDiscountHolder> {

    private final List<Listener<ClientDiscountHolder>> listeners;

    private final Map<String, Integer> discounts;

    public ClientDiscountHolder(){
        listeners = new ArrayList<>();
        discounts = new HashMap<>();
    }

    public synchronized void addDiscount(String resourceType, Integer discount){
        resourceType = resourceType.toLowerCase();
        discounts.putIfAbsent(resourceType, 0);
        discounts.put(resourceType, discounts.get(resourceType) + discount);
        update();
    }

    public synchronized Map<String, Integer> getDiscounts() {
        return new HashMap<>(discounts);
    }

    @Override
    public synchronized void addListener(Listener<ClientDiscountHolder> listener) {
        listeners.add(listener);
    }

    @Override
    public synchronized void update() {
        for(Listener<ClientDiscountHolder> l : listeners)
            l.update(this);
    }
}
