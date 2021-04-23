package it.polimi.ingsw.model.storage;

import it.polimi.ingsw.exceptions.IllegalResourceTransferException;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceType;

import java.util.HashMap;
import java.util.Map;


/**
 * The Shelf class is responsible for storing resources in the base storage and in the leader storage of the player.
 * Every shelf has limitations in space (maxAmount), it can store a single type of resource at a time
 * (currentType) and it only allows resources of acceptedTypes type
 */

public class Shelf extends ResourceContainer{
    private final String id;
    private final ResourceType acceptedTypes;
    private ResourceSingle currentType;
    private final int maxAmount;
    private int currentAmount;

    /**
     * Creates a new Shelf with the specified limitations.
     * currentType is null if currentAmount == 0 and it contains the currentType held if currentAmount > 0
     * @param id the identifier of the shelf
     * @param acceptedTypes the allowed types
     * @param maxAmount the max amount of resources that this shelf can hold
     * @throws IllegalArgumentException if the maxAmount specified is less then or equals to 0
     * @throws NullPointerException if parameters are null
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
     * Returns the current type held by this shelf if not empty. If the shelf is empty return is null
     * @return the current type held by this shelf if not empty. If the shelf is empty return is null
     */
    public ResourceSingle getCurrentType(){
        return currentType;
    }

    /**
     * Returns the current amount of resources held
     * @return the current amount of resources held
     */
    public int getAmount(){
        return currentAmount;
    }

    /**
     * Returns the types allowed to be stored in this shelf
     * @return the types allowed to be stored in this shelf
     */
    public ResourceType getAcceptedTypes() {
        return acceptedTypes;
    }

    /**
     * Returns the maximum amount of resources the shelf can hold
     * @return the maximum amount of resources the shelf can hold
     */
    public int getMaxAmount() {
        return maxAmount;
    }

    /**
     * Adds resources to this shelf. The current type of the shelf is updated
     * @param resource the type of resources to insert
     * @param amount the amount of resources to insert
     * @throws NullPointerException if parameters are null
     * @throws IllegalArgumentException if amount is <= 0
     * @throws IllegalResourceTransferException if after the insertion, size or type limitations are violated
     */
    public void addResources(ResourceSingle resource, int amount) throws IllegalResourceTransferException {
        if(amount <= 0)
            throw new IllegalArgumentException("Can't add a non-positive amount of resources");
        if(resource == null)
            throw new NullPointerException();
        if(!resource.isA(acceptedTypes))
            throw new IllegalResourceTransferException(resource + " is not supported for this shelf");

        if(getCurrentType() != null && !resource.isA(getCurrentType()))
            throw new IllegalResourceTransferException(resource + " is not consistent with the type already contained in" +
                    " the shelf");

        if(currentAmount + amount > maxAmount)
            throw new IllegalResourceTransferException("The shelf has no space left for this insertion");

        this.currentAmount += amount;
        this.currentType = resource;

    }

    /**
     * Removes from the container a given amount of a given resource. If the shelf becomes empty, currentType is set
     * to null
     * @param resource the resource type to remove
     * @param amount   the amount of resources to remove
     * @throws IllegalArgumentException if amount <= 0
     * @throws NullPointerException if resource is null
     * @throws IllegalResourceTransferException if the resource or the amount cannot be removed from the container
     */
    @Override
    public void removeResources(ResourceSingle resource, int amount) throws IllegalResourceTransferException {
        if(amount <= 0)
            throw new IllegalArgumentException("Amount must be grater then 0");
        if(currentAmount == 0)
            throw new IllegalResourceTransferException("Shelf is empty");
        if(resource == null)
            throw new NullPointerException();
        if(!resource.equals(currentType))
            throw new IllegalResourceTransferException("Mismatch between current type and required type to remove");
        removeResources(amount);
    }

    /**
     * Removes a resource from this shelf
     * @param amount the amount of resources to remove
     * @throws IllegalArgumentException if amount is less then or equal to 0
     * @throws IllegalResourceTransferException if there is not enough room for the removal
     */
    public void removeResources(int amount) throws IllegalResourceTransferException {
        if(amount <= 0 ) throw new IllegalArgumentException("Can't remove a non-positive amount of resources");

        if(currentAmount - amount < 0) throw new IllegalResourceTransferException("There are not enough resources");
        currentAmount -= amount;
        if(currentAmount == 0)
            currentType = null;
    }

    /**
     * Returns a map of the stored resources with their given amount
     * @return a map of the stored resources with their given amount
     */
    @Override
    public Map<ResourceSingle, Integer> getAllResources() {
        Map<ResourceSingle, Integer> result = new HashMap<>();
        if(currentType != null)
            result.put(currentType, currentAmount);
        return result;
    }

    /**
     * Returns the id of this shelf
     * @return the id of this shelf
     */
    public String getId(){
        return id;
    }

    /**
     * Returns the representation of this shelf
     * @return the representation of this shelf
     */
    public String toString(){
        if(currentAmount == 0)
            return id + "{acceptedTypes=" + acceptedTypes + ", amount=" + maxAmount  + "}";
        else
            return id + "{" + ((currentType != null) ? currentType + ": " + currentAmount : "") + "}";
    }


}
