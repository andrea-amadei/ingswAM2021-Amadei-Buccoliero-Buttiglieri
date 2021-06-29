package it.polimi.ingsw.server.model.global;

import it.polimi.ingsw.server.model.faithpath.FaithPath;
import it.polimi.ingsw.server.model.leader.LeaderCard;
import it.polimi.ingsw.server.model.lorenzo.Token;
import it.polimi.ingsw.server.model.market.Market;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The model of the game. It contains the list of players, the market, the shop and the faith path
 */
public class GameModel {
    private final List<Player> players;
    private final Market market;
    private final Shop shop;
    private final FaithPath faithPath;
    private final List<LeaderCard> leaderCards;
    private Deque<Token> lorenzoTokens;

    /**
     * This constructor is used by the builder. The builder builds each component first and then
     * creates the game model
     */
    public GameModel(List<Player> players, Market market, Shop shop, FaithPath faithPath, List<LeaderCard> leaderCards, Deque<Token> lorenzoTokens){
        this.players = players;
        this.market = market;
        this.shop = shop;
        this.faithPath = faithPath;
        this.leaderCards = leaderCards;
        this.lorenzoTokens = lorenzoTokens;
    }

    /**
     * Shuffles all the tokens
     */
    public void shuffleTokens(){
        List<Token> tokenList = new ArrayList<>(lorenzoTokens);
        Collections.shuffle(tokenList);
        lorenzoTokens = new ArrayDeque<>(tokenList);
    }

    /**
     * Puts the top token in the last place
     * @throws NoSuchElementException if the token list is empty
     */
    public void swapToken(){
        Token topToken = lorenzoTokens.removeFirst();
        lorenzoTokens.addLast(topToken);
    }

    /**
     * Returns the lorenzo tokens
     * @return the lorenzo tokens
     */
    public Deque<Token> getLorenzoTokens() {
        return lorenzoTokens;
    }

    /**
     * Returns the list of players
     * @return the list of players
     */
    public List<Player> getPlayers (){ return players;}

    /**
     * Returns a list with all player's names
     * @return a list with all player's names
     */
    public List<String> getPlayerNames() {
        return getPlayers().stream().map(Player::getUsername).collect(Collectors.toList());
    }

    /**
     * Gets the player with the specified username
     * @param username the username of the player
     * @return the player with the given username
     * @throws NullPointerException if username is null
     * @throws NoSuchElementException if there is no player with such username
     */
    public Player getPlayerById(String username){
        if(username == null)
            throw new NullPointerException();
        return getPlayers().stream()
                           .filter(p -> p.getUsername().equals(username))
                           .findAny()
                           .orElseThrow(() -> new NoSuchElementException("No player '" + username + "' found"));
    }

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

    /**
     * returns the leaderCards
     * @return the leaderCards
     */
    public List<LeaderCard> getLeaderCards() {return leaderCards;}

    /**
     * Used for test only
     */
    public void setTopToken(Token t){
        lorenzoTokens.addFirst(t);
    }
}
