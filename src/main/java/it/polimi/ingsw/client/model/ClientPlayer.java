package it.polimi.ingsw.client.model;

import it.polimi.ingsw.client.observables.Listener;
import it.polimi.ingsw.client.observables.Observable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class ClientPlayer implements Observable<ClientPlayer> {

    private final List<Listener<ClientPlayer>> listeners;

    private final ClientBaseStorage chest;
    private final ClientBaseStorage hand;
    private final ClientBaseStorage marketBasket;

    private final List<ClientShelf> cupboard;
    private final List<ClientShelf> leaderShelves;

    private final ClientProduction production;

    private final ClientFlagHolder flagHolder;

    private final ClientDiscountHolder discountHolder;

    private final ClientLeaderCards leaderCards;

    private final ClientFaithPath faithPath;

    private final String username;

    private int victoryPoints;

    public ClientPlayer(String username, ClientBaseStorage chest, ClientBaseStorage hand, ClientBaseStorage marketBasket, List<ClientShelf> cupboard,
                        List<ClientShelf> leaderShelves, ClientProduction production, ClientFlagHolder flagHolder, ClientDiscountHolder discountHolder,
                        ClientLeaderCards leaderCards, ClientFaithPath faithPath){

        this.username = username;
        this.victoryPoints = 0;
        this.chest = chest;
        this.hand = hand;
        this.marketBasket = marketBasket;
        this.cupboard = cupboard;
        this.leaderShelves = leaderShelves;
        this.production = production;
        this.flagHolder = flagHolder;
        this.discountHolder = discountHolder;
        this.leaderCards = leaderCards;
        this.faithPath = faithPath;

        listeners = new ArrayList<>();
    }

    public synchronized void addBaseShelf(ClientShelf shelf){
        cupboard.add(shelf);
        update();
    }

    public synchronized void addLeaderShelf(ClientShelf shelf){
        leaderShelves.add(shelf);
        update();
    }

    public synchronized void addVictoryPoints(int amount){
        victoryPoints += amount;
        update();
    }

    public synchronized String getUsername() {
        return username;
    }

    public synchronized int getVictoryPoints() {
        return victoryPoints;
    }

    public synchronized List<Listener<ClientPlayer>> getListeners() {
        return new ArrayList<>(listeners);
    }

    public synchronized ClientBaseStorage getChest() {
        return chest;
    }

    public synchronized ClientBaseStorage getHand() {
        return hand;
    }

    public synchronized ClientBaseStorage getMarketBasket() {
        return marketBasket;
    }

    public synchronized List<ClientShelf> getCupboard() {
        return new ArrayList<>(cupboard);
    }

    public synchronized List<ClientShelf> getLeaderShelves() {
        return new ArrayList<>(leaderShelves);
    }

    public synchronized ClientBaseStorage getBaseStorageById(String id){
        if(id == null)
            throw new NullPointerException();

        List<ClientBaseStorage> baseStorages = new ArrayList<>(Arrays.asList(chest, hand, marketBasket));

        return baseStorages.stream()
                           .filter(b -> b.getStorage().getId().equals(id))
                           .findFirst()
                           .orElseThrow(() -> new NoSuchElementException("There is no ClientBaseStorage with id \"" + id + "\""));
    }

    public synchronized ClientShelf getClientShelfById(String id){
        if(id == null)
            throw new NullPointerException();

        List<ClientShelf> shelves = new ArrayList<>();
        shelves.addAll(cupboard);
        shelves.addAll(leaderShelves);

        return shelves.stream()
                .filter(s -> s.getStorage().getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("There is no ClientShelf with id \"" + id + "\""));
    }

    public synchronized ClientProduction getProduction() {
        return production;
    }

    public synchronized ClientFlagHolder getFlagHolder() {
        return flagHolder;
    }

    public synchronized ClientDiscountHolder getDiscountHolder() {
        return discountHolder;
    }

    public synchronized ClientLeaderCards getLeaderCards() {
        return leaderCards;
    }

    public synchronized ClientFaithPath getFaithPath() {
        return faithPath;
    }


    @Override
    public synchronized void addListener(Listener<ClientPlayer> listener) {
        listeners.add(listener);
    }

    @Override
    public synchronized void update() {
        for(Listener<ClientPlayer> l : listeners)
            l.update(this);
    }
}
