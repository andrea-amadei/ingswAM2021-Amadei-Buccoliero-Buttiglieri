package it.polimi.ingsw.model;

import it.polimi.ingsw.model.holder.ConversionHolder;
import it.polimi.ingsw.model.holder.DiscountHolder;
import it.polimi.ingsw.model.holder.FaithHolder;
import it.polimi.ingsw.model.holder.FlagHolder;
import it.polimi.ingsw.model.leader.LeaderCard;
import it.polimi.ingsw.model.production.Production;
import it.polimi.ingsw.model.storage.Storage;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * The board of the player. It contains everything owned by the player
 */
public class Board {
    private final Storage storage;
    private final List<LeaderCard> leaderCards;
    private final DiscountHolder discountHolder;
    private final FlagHolder flagHolder;
    private final Production production;
    private final FaithHolder faithHolder;
    private final ConversionHolder conversionHolder;

    /**
     * Creates a new empty board. It contains a storage, the leader cards, a discount holder, a flag holder, a faith
     * holder, a conversion holder and a production.
     */
    public Board(){
        this.storage = new Storage();
        this.leaderCards = new ArrayList<>();
        this.discountHolder = new DiscountHolder();
        this.flagHolder = new FlagHolder();
        this.production = new Production();
        this.faithHolder = new FaithHolder();
        this.conversionHolder = new ConversionHolder();
    }

    /**
     * @return the player storage
     */
    public Storage getStorage() {
        return storage;
    }

    /**
     * @return the leader cards
     */
    public List<LeaderCard> getLeaderCards() {
        return leaderCards;
    }

    /**
     * @param id the ID of the leader card being searched for
     * @return the leader card
     */
    public LeaderCard getLeaderCardByID(int id){
        if(id <= 0)
            throw new IllegalArgumentException("Non existent ID number");
        return leaderCards.stream().filter(x->x.getId() == id).findFirst().orElseThrow(NoSuchElementException::new);
    }

    /**
     * @return the discount holder
     */
    public DiscountHolder getDiscountHolder() {
        return discountHolder;
    }

    /**
     * @return the flag holder
     */
    public FlagHolder getFlagHolder() {
        return flagHolder;
    }

    /**
     * @return the production
     */
    public Production getProduction() {
        return production;
    }

    /**
     * @return the faith holder
     */
    public FaithHolder getFaithHolder() {
        return faithHolder;
    }

    /**
     * @return the conversion holder
     */
    public ConversionHolder getConversionHolder(){
        return conversionHolder;
    }

    /**
     * Adds a new leader card to the board
     * @param card the card to be added
     * @throws NullPointerException if card is null
     */
    public void addLeaderCard(LeaderCard card){
        if(card == null)
            throw new NullPointerException();
        leaderCards.add(card);
    }
}
