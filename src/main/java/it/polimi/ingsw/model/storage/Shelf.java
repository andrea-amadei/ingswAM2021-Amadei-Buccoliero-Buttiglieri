package it.polimi.ingsw.model.storage;

import it.polimi.ingsw.exceptions.UnsupportedShelfInsertionException;
import it.polimi.ingsw.exceptions.UnsupportedShelfRemovalException;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceType;



/**
 * The Shelf class is responsible for storing resources in the limited storage of the player (base storage and leader
 * storage). Every shelf has limitations in space (maxAmount), it can store a single type of resource at a time
 * (currentType) and it only allows resources of acceptedTypes type
 */

public class Shelf {
    private final String id;
    private final ResourceType acceptedTypes;
    private ResourceSingle currentType;
    private final int maxAmount;
    private int currentAmount;

    /**
     * Creates a new Shelf with the specified limitations.
     * currentType is null if currentAmount == 0, or it contains the currentType held if currentAmount > 0
     * @param id the identifier of the shelf
     * @param acceptedTypes the allowed types
     * @param maxAmount the max amount of resources that this shelf can hold
     * @throws IllegalArgumentException if null parameters or the maxAmount specified is less then or equals to 0
     */
    public Shelf(String id, ResourceType acceptedTypes, int maxAmount){
        if(id == null)
            throw new NullPointerException("Null id are not allowed");
        if(acceptedTypes == null)
            throw new NullPointerException("Null acceptedTypes are not allowed");
        if(maxAmount <= 0)
            throw new IllegalArgumentException("maxAmount needs to be >= 0");

        this.id = id;
        this.acceptedTypes = acceptedTypes;
        this.maxAmount = maxAmount;

        currentType = null;
        currentAmount = 0;
    }

    /**
     *
     * @return the current type held by this shelf if not empty. If the shelf is empty return is null
     */
    public ResourceSingle getCurrentType(){
        return currentType;
    }

    /**
     *
     * @return the current amount of resources held
     */
    public int getAmount(){
        return currentAmount;
    }

    /**
     * Adds resources to this shelf
     * @param type the type of resources to insert
     * @param amount the amount of resources to insert
     * @throws UnsupportedShelfInsertionException if after the insertion, size or type
     *         limitations are violated
     */
    public void addResources(ResourceSingle type, int amount){
        if(amount <= 0)
            throw new IllegalArgumentException("Can't add a non-positive amount of resources");
        if(type == null)
            throw new NullPointerException();
        if(!type.isA(acceptedTypes))
            throw new UnsupportedShelfInsertionException(type + " is not supported for this shelf");

        if(getCurrentType() != null && !type.isA(getCurrentType()))
            throw new UnsupportedShelfInsertionException(type + " is not consistent with the type already contained in" +
                    " the shelf");


        if(currentAmount + amount > maxAmount)
            throw new UnsupportedShelfInsertionException("The shelf has no space left for this insertion");



        this.currentAmount += amount;
        this.currentType = type;

    }

    /**
     * Removes a resource from this shelf
     * @param amount the amount of resources to remove
     * @throws IllegalArgumentException if amount is less then or equal to 0
     * @throws UnsupportedShelfInsertionException if there is not enough room for the removal
     */
    public void removeResources(int amount){
        if(amount <= 0 ) throw new IllegalArgumentException("Can't remove a non-positive amount of resources");

        if(currentAmount - amount < 0) throw new UnsupportedShelfRemovalException("There are not enough resources");

        currentAmount -= amount;
        if(currentAmount == 0)
            currentType = null;

    }

    /**
     *
     * @return the id of this shelf
     */
    public String getId(){
        return id;
    }

    /**
     *
     * @return the representation of this shelf
     */
    public String toString(){

        return id + " " + "{" + ((currentType != null) ? currentType + ": " + currentAmount : "") + "}";
    }


}
