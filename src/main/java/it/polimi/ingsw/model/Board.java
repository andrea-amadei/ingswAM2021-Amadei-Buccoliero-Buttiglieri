package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.IllegalActionException;
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
import java.util.stream.Collectors;

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
     * This constructor should be used for test/mock creation only. The builder uses the other one
     */
    public Board(){
        this(new Storage(), new Production(), new FaithHolder());
    }

    /**
     * Creates a new empty board. It contains a storage, the leader cards, a discount holder, a flag holder, a faith
     * holder, a conversion holder and a production.
     * This constructor is used by the builder
     * @param storage the storage of this board
     * @param production the production of this board
     * @param faithHolder the faith holder of this board
     */
    public Board(Storage storage, Production production, FaithHolder faithHolder){
        this.storage = storage;
        this.leaderCards = new ArrayList<>();
        this.discountHolder = new DiscountHolder();
        this.flagHolder = new FlagHolder();
        this.production = production;
        this.faithHolder = faithHolder;
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
        return leaderCards.stream().filter(x -> x.getId() == id).findFirst().orElseThrow(()->new NoSuchElementException("There is no leader with id " + id));
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

    /**
     * Method removes leader cards based on their indexes
     * @param indexes the list of indexes of leader cards to remove
     * @throws NullPointerException iff pointer to indexes is null
     * @throws IndexOutOfBoundsException iff indexes are out of bound
     */
    public void removeLeaderCardsByIndex(List<Integer> indexes){
        if(indexes == null)
            throw new NullPointerException();

        //checking if leaders' IDs are valid
        for(Integer i : indexes)
            if(i < 0 || i >= leaderCards.size())
                throw new IndexOutOfBoundsException("Leaders indexes must be between 0 and amount of owned leaders minus 1");

        //removing leader cards
        List<LeaderCard> leadersToRemove = indexes.stream().map(leaderCards::get).collect(Collectors.toList());
        leaderCards.removeAll(leadersToRemove);
    }
}
