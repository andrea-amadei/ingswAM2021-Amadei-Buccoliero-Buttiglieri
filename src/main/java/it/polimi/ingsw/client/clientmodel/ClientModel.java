package it.polimi.ingsw.client.clientmodel;

import it.polimi.ingsw.client.observables.Listener;
import it.polimi.ingsw.client.observables.Observable;
import it.polimi.ingsw.common.parser.raw.RawCraftingCard;
import it.polimi.ingsw.common.parser.raw.RawLeaderCard;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ClientModel implements Observable<ClientModel> {

    private final List<Listener<ClientModel>> listeners;

    private final PersonalData personalData;
    private List<ClientPlayer> players;
    private ClientPlayer currentPlayer;
    private ClientShop shop;
    private ClientMarket market;
    private List<RawLeaderCard> leaderCards;
    private List<RawCraftingCard> craftingCards;
    private final ClientEndGameResults endGameResults;

    public ClientModel(){
        this.personalData = new PersonalData();
        this.endGameResults = new ClientEndGameResults();
        listeners = new ArrayList<>();
    }

    public synchronized void setLeaderCards(List<RawLeaderCard> leaderCards) {
        this.leaderCards = leaderCards;
    }

    public synchronized void setCraftingCards(List<RawCraftingCard> craftingCards) {
        this.craftingCards = craftingCards;
    }

    public synchronized void setPlayers(List<ClientPlayer> players) {
        this.players = players;
    }

    public synchronized void setCurrentPlayer(ClientPlayer nextCurrentPlayer){
        this.currentPlayer = nextCurrentPlayer;
        update();
    }

    public synchronized void setMarket(ClientMarket market) {
        this.market = market;
    }

    public synchronized void setShop(ClientShop shop) {
        this.shop = shop;
    }

    public synchronized PersonalData getPersonalData(){
        return personalData;
    }

    public synchronized List<ClientPlayer> getPlayers() {
        return new ArrayList<>(players);
    }

    public synchronized ClientPlayer getPlayerByName(String username){
        if(username == null)
            throw new NullPointerException();

        return players.stream()
                      .filter(p -> p.getUsername().equals(username))
                      .findFirst()
                      .orElseThrow(()->new NoSuchElementException("no player \"" + username + "\""));
    }

    public synchronized ClientPlayer getCurrentPlayer() {
        return currentPlayer;
    }


    public synchronized ClientShop getShop() {
        return shop;
    }

    public synchronized ClientMarket getMarket() {
        return market;
    }

    public synchronized List<RawLeaderCard> getLeaderCards() {
        return new ArrayList<>(leaderCards);
    }

    public synchronized RawLeaderCard getLeaderCardById(int id){
        return leaderCards.stream()
                          .filter(c -> c.getId() == id)
                          .findFirst()
                          .orElseThrow(() -> new NoSuchElementException("No leader card with id " + id));
    }

    public synchronized List<RawCraftingCard> getCraftingCards() {
        return new ArrayList<>(craftingCards);
    }

    public synchronized RawCraftingCard getCraftingCardById(int id){
        return craftingCards.stream()
                            .filter(c -> c.getId() == id)
                            .findFirst()
                            .orElseThrow(()-> new NoSuchElementException("No crafting card with id " + id));
    }

    public ClientEndGameResults getEndGameResults() {
        return endGameResults;
    }

    @Override
    public synchronized void addListener(Listener<ClientModel> listener) {
        listeners.add(listener);
    }

    @Override
    public synchronized void update() {
        for(Listener<ClientModel> l : listeners)
            l.update(this);
    }
}
