package it.polimi.ingsw.model;

import it.polimi.ingsw.model.leader.LeaderCard;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.production.CraftingCard;
import it.polimi.ingsw.server.DummyBuilder;

import java.util.List;

/**
 * The model of the game. It contains the list of players, the market, the shop and the faith path
 */
public class GameModel {
    private final List<Player> players;
    private final Market market;
    private final Shop shop;
    private final FaithPath faithPath;
    private final List<LeaderCard> leaderCards;

    /**
     * Creates a new GameModel. Game components are created using the builder
     * @param players the list of the players of the game
     */
    public GameModel(List<Player> players){

        //TODO: Use the real Builder and not the Dummy one and check the correctness of players param
        this.players = players;
        market = new Market();
        shop = new Shop();
        for(CraftingCard card : DummyBuilder.buildCraftingCards())
            shop.addCard(card);
        faithPath = new FaithPath(DummyBuilder.buildFaithPathTiles());
        leaderCards = DummyBuilder.buildLeaderCards();
    }

    /**
     * Returns the list of players
     * @return the list of players
     */
    public List<Player> getPlayers (){ return players;}

    /**
     * Returns the market
     * @return the market
     */
    public Market getMarket() { return market;}

    /**
     * Returns the shop
     * @return the shop
     */
    public Shop getShop() {return shop;}

    /**
     * Returns the faithPath
     * @return the faithPath
     */
    public FaithPath getFaithPath() {return faithPath;}

    public List<LeaderCard> getLeaderCards() {return leaderCards;}
}
