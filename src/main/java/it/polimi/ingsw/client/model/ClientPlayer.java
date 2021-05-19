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

    public void addBaseShelf(ClientShelf shelf){
        cupboard.add(shelf);
        update();
    }

    public void addLeaderShelf(ClientShelf shelf){
        leaderShelves.add(shelf);
        update();
    }

    public void addVictoryPoints(int amount){
        victoryPoints += amount;
        update();
    }

    public String getUsername() {
        return username;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public List<Listener<ClientPlayer>> getListeners() {
        return listeners;
    }

    public ClientBaseStorage getChest() {
        return chest;
    }

    public ClientBaseStorage getHand() {
        return hand;
    }

    public ClientBaseStorage getMarketBasket() {
        return marketBasket;
    }

    public List<ClientShelf> getCupboard() {
        return cupboard;
    }

    public List<ClientShelf> getLeaderShelves() {
        return leaderShelves;
    }

    public ClientBaseStorage getBaseStorageById(String id){
        if(id == null)
            throw new NullPointerException();

        List<ClientBaseStorage> baseStorages = new ArrayList<>(Arrays.asList(chest, hand, marketBasket));

        return baseStorages.stream()
                           .filter(b -> b.getStorage().getId().equals(id))
                           .findFirst()
                           .orElseThrow(() -> new NoSuchElementException("There is no ClientBaseStorage with id \"" + id + "\""));
    }

    public ClientShelf getClientShelfById(String id){
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

    public ClientProduction getProduction() {
        return production;
    }

    public ClientFlagHolder getFlagHolder() {
        return flagHolder;
    }

    public ClientDiscountHolder getDiscountHolder() {
        return discountHolder;
    }

    public ClientLeaderCards getLeaderCards() {
        return leaderCards;
    }

    public ClientFaithPath getFaithPath() {
        return faithPath;
    }


    @Override
    public void addListener(Listener<ClientPlayer> listener) {
        listeners.add(listener);
    }

    @Override
    public void update() {
        for(Listener<ClientPlayer> l : listeners)
            l.update(this);
    }
}
