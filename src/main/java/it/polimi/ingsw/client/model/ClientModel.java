package it.polimi.ingsw.client.model;

import it.polimi.ingsw.client.OutputHandler;
import it.polimi.ingsw.parser.raw.RawCraftingCard;
import it.polimi.ingsw.parser.raw.RawLeaderCard;

import java.util.List;

public class ClientModel {

    private OutputHandler outputHandler;

    private final PersonalData personalData;
    private List<ClientPlayer> players;
    private ClientShop shop;
    private ClientMarket market;
    private List<RawLeaderCard> leaderCards;
    private List<RawCraftingCard> craftingCards;

    public ClientModel(OutputHandler outputHandler, List<ClientPlayer> players){
        this.players = players;
        personalData = new PersonalData();
        this.outputHandler = outputHandler;
    }

    public ClientModel(){
        this.personalData = new PersonalData();
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

    public ClientShop getShop() {
        return shop;
    }

    public ClientMarket getMarket() {
        return market;
    }

    public List<RawLeaderCard> getLeaderCards() {
        return leaderCards;
    }

    public List<RawCraftingCard> getCraftingCards() {
        return craftingCards;
    }

    public OutputHandler getOutputHandler(){
        return this.outputHandler;
    }
}
