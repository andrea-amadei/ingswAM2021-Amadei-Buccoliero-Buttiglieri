package it.polimi.ingsw.model.storage;

import it.polimi.ingsw.model.GameParameters;

import java.util.ArrayList;
import java.util.List;

/**
 * The comprehensive storage of a player. It contains a chest, the hand, a market basket and a cupboard.
 * The chest stores all resources crafted in the production.
 * The hand is a temporary container used by the player to tidy his/her cupboard.
 * The market basket is the container in which resources are collected from the market
 * The cupboard contains the base shelves and possibly the leader shelves
 */
public class Storage {
    private final BaseStorage chest;
    private final BaseStorage hand;
    private final BaseStorage marketBasket;
    private Cupboard cupboard;

    /**
     * A new empty storage is created. The cupboard is initialized with the parameters stated in GameParameters class
     */
    public Storage(){
        chest = new BaseStorage();
        hand = new BaseStorage();
        marketBasket = new BaseStorage();
        List<Shelf> baseShelves = new ArrayList<>();
        for(int i = 0; i < GameParameters.BASE_CUPBOARD_SHELF_NAMES.size(); i++){
            baseShelves.add(new Shelf(GameParameters.BASE_CUPBOARD_SHELF_NAMES.get(i),
                                      GameParameters.BASE_CUPBOARD_SHELF_TYPES.get(i),
                                      GameParameters.BASE_CUPBOARD_SHELF_SIZES.get(i)));
        }
        cupboard = new BaseCupboard(baseShelves);

    }

    /**
     * Returns the chest of this storage
     * @return the hand of this storage
     */
    public BaseStorage getChest() {
        return chest;
    }

    /**
     * Returns the hand of this storage
     * @return the hand of this storage
     */
    public BaseStorage getHand() {
        return hand;
    }

    /**
     * Returns the market basket of this storage
     * @return the market basket of this storage
     */
    public BaseStorage getMarketBasket() {
        return marketBasket;
    }

    /**
     * Returns the cupboard of this storage
     * @return the cupboard of this storage
     */
    public Cupboard getCupboard() {
        return cupboard;
    }

    /**
     * Decorates the cupboard of the player
     * @param decorator the new decorated cupboard
     * @throws NullPointerException if decorator is null
     */
    public void decorate(LeaderDecorator decorator){
        if(decorator == null)
            throw new NullPointerException();
        this.cupboard = decorator;
    }

}
