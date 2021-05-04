package it.polimi.ingsw.model.storage;

import it.polimi.ingsw.exceptions.IllegalSelectionException;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.model.GameParameters;

import java.util.*;
import java.util.stream.Collectors;

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

    //the key is a resource container, the value is a map that represents the amount of resources for each resource type
    //selected from that container
    private Map<ResourceContainer, Map<ResourceSingle, Integer>> selectedResources;

    /**
     * A new empty storage is created. The cupboard is initialized with the parameters stated in GameParameters class
     */
    public Storage(){
        chest = new BaseStorage("chest");
        hand = new BaseStorage("hand");
        marketBasket = new BaseStorage("basket");
        List<Shelf> baseShelves = new ArrayList<>();
        for(int i = 0; i < GameParameters.BASE_CUPBOARD_SHELF_NAMES.size(); i++){
            baseShelves.add(new Shelf(GameParameters.BASE_CUPBOARD_SHELF_NAMES.get(i),
                                      GameParameters.BASE_CUPBOARD_SHELF_TYPES.get(i),
                                      GameParameters.BASE_CUPBOARD_SHELF_SIZES.get(i)));
        }
        cupboard = new BaseCupboard(baseShelves);
        selectedResources = new HashMap<>();

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
     * Returns a map of all the resources owned by the player (chest + cupboard).
     * Note: resources in the hand are not counted
     * @return a map of all the resources owned by the player (chest + cupboard).
     */
    public Map<ResourceSingle, Integer> getStoredResources(){
        List<ResourceContainer> containers = new ArrayList<>();
        containers.add(chest);
        containers.addAll(cupboard.getShelves());
        
        return containers.stream()
                         .flatMap(x -> x.getAllResources().entrySet().stream())
                         .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                 Integer::sum));
    }

    /**
     * Returns the cupboard of this storage
     * @return the cupboard of this storage
     */
    public Cupboard getCupboard() {
        return cupboard;
    }

    /**
     * Returns the ResourceContainer with the specified id. It only includes cupboard shelves and the chest,
     * @param id the id of the container
     * @return the ResourceContainer with the specified id. It only includes cupboard shelves and the chest
     * @throws NullPointerException if id is null
     * @throws NoSuchElementException if the id doesn't match with any of the resourceContainers
     */
    public ResourceContainer getSpendableResourceContainerById(String id){

        List<ResourceContainer> spendableContainers = new ArrayList<>(getCupboard().getShelves());
        spendableContainers.add(getChest());

        if(id == null)
            throw new NullPointerException();
        return spendableContainers.stream().filter(x -> x.getId().equalsIgnoreCase(id)).findFirst().orElseThrow(NoSuchElementException::new);
    }

    /**
     * Returns a map representing the selected resources
     * @return a map representing the selected resources
     */
    public Map<ResourceContainer, Map<ResourceSingle, Integer>> getSelection(){
        return selectedResources;
    }

    /**
     * Adds the specified resources and the specified ResourceContainer to the selection
     * @param from the ResourceContainer from which resources are selected
     * @param res the ResourceSingle selected
     * @param amount the amount of resources selected
     * @throws IllegalSelectionException if the container doesn't contain the selected resources
     * @throws NullPointerException if either from or res is null
     * @throws IllegalArgumentException if amount <= 0
     */
    public void addToSelection(ResourceContainer from, ResourceSingle res, int amount) throws IllegalSelectionException {
        if(from == null || res == null)
            throw new NullPointerException();
        if(amount <= 0)
            throw new IllegalArgumentException("The selected amount must be positive");

        int preInsertionValue = Optional.ofNullable(selectedResources.get(from))
                                        .flatMap(x->Optional.ofNullable(x.get(res)))
                                        .orElse(0);

        if(Optional.ofNullable(from.getAllResources().get(res)).orElse(0) < preInsertionValue + amount)
            throw new IllegalSelectionException("Too much resources selected for the container");

        int currentValue = selectedResources.computeIfAbsent(from, (k)->new HashMap<>())
                         .computeIfAbsent(res, (k)->0);
        selectedResources.get(from).put(res, currentValue + amount);
    }

    /**
     * Sets the selection to null
     */
    public void resetSelection(){
        selectedResources = null;
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
