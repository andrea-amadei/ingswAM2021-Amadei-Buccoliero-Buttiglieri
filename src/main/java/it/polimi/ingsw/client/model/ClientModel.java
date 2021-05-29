package it.polimi.ingsw.client.model;

import it.polimi.ingsw.client.OutputHandler;
import it.polimi.ingsw.client.observables.Listener;
import it.polimi.ingsw.client.observables.Observable;
import it.polimi.ingsw.parser.raw.RawCraftingCard;
import it.polimi.ingsw.parser.raw.RawLeaderCard;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ClientModel implements Observable<ClientModel> {

    private final List<Listener<ClientModel>> listeners;
    private OutputHandler outputHandler;

    private final PersonalData personalData;
    private List<ClientPlayer> players;
    private ClientPlayer currentPlayer;
    private ClientShop shop;
    private ClientMarket market;
    private List<RawLeaderCard> leaderCards;
    private List<RawCraftingCard> craftingCards;

    public ClientModel(){
        this.personalData = new PersonalData();
        listeners = new ArrayList<>();
    }

    //test for branch fork

    public ClientModel(OutputHandler outputHandler){
        this();
        this.outputHandler = outputHandler;

    }

    public void setLeaderCards(List<RawLeaderCard> leaderCards) {
        this.leaderCards = leaderCards;
    }

    public void setCraftingCards(List<RawCraftingCard> craftingCards) {
        this.craftingCards = craftingCards;
    }

    public void setPlayers(List<ClientPlayer> players) {
        this.players = players;
    }

    public void setCurrentPlayer(ClientPlayer nextCurrentPlayer){
        this.currentPlayer = nextCurrentPlayer;
        update();
    }

    public void setMarket(ClientMarket market) {
        this.market = market;
    }

    public void setShop(ClientShop shop) {
        this.shop = shop;
    }

    public PersonalData getPersonalData(){
        return personalData;
    }

    public List<ClientPlayer> getPlayers() {
        return players;
    }

    public ClientPlayer getPlayerByName(String username){
        if(username == null)
            throw new NullPointerException();

        return players.stream()
                      .filter(p -> p.getUsername().equals(username))
                      .findFirst()
                      .orElseThrow(()->new NoSuchElementException("no player \"" + username + "\""));
    }

    public ClientPlayer getCurrentPlayer() {
        return currentPlayer;
    }


    public ClientShop getShop() {
        return shop;
    }

    public ClientMarket getMarket() {
        return market;
    }

    public List<RawLeaderCard> getLeaderCards() {
        return leaderCards;
    }

    public RawLeaderCard getLeaderCardById(int id){
        return leaderCards.stream()
                          .filter(c -> c.getId() == id)
                          .findFirst()
                          .orElseThrow(() -> new NoSuchElementException("No leader card with id " + id));
    }

    public List<RawCraftingCard> getCraftingCards() {
        return craftingCards;
    }

    public RawCraftingCard getCraftingCardById(int id){
        return craftingCards.stream()
                            .filter(c -> c.getId() == id)
                            .findFirst()
                            .orElseThrow(()-> new NoSuchElementException("No crafting card with id " + id));
    }

    public OutputHandler getOutputHandler(){
        return this.outputHandler;
    }

    @Override
    public void addListener(Listener<ClientModel> listener) {
        listeners.add(listener);
    }

    @Override
    public void update() {
        for(Listener<ClientModel> l : listeners)
            l.update(this);
    }
}
