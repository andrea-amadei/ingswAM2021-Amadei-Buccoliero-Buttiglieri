package it.polimi.ingsw.model.storage;

import it.polimi.ingsw.exceptions.IllegalResourceTransfer;
import it.polimi.ingsw.gamematerials.ResourceSingle;

import java.util.*;

public abstract class ResourceContainer {
    /**
     * Moves a given amount of a given resource from a Resource Container to another
     * @param to the Resource Container to transfer the resources to
     * @param resource the resource to transfer
     * @param amount the amount to transfer
     * @throws IllegalResourceTransfer if either:<ul>
     *     <li>the receiver container doesn't accept the resource or the amount</li>
     *     <li>the requested resource or amount is not available in the sender container</li></ul>
     * @throws NullPointerException if to or resource are null
     * @throws IllegalArgumentException if amount is non-positive
     */
    public void moveTo(ResourceContainer to, ResourceSingle resource, int amount) throws IllegalResourceTransfer {
        if(to == null || resource == null)
            throw new NullPointerException();

        if(amount <= 0)
            throw new IllegalArgumentException("The amount to move must be positive");

        if(!getAllResources().containsKey(resource))
            throw new IllegalResourceTransfer("No resources \"" + resource.getId() + "\" available to move");

        if(getAllResources().get(resource) < amount)
            throw new IllegalResourceTransfer("Not enough resources \"" + resource.getId() + "\" available to move");

        try {
            to.addResources(resource, amount);
        } catch (IllegalResourceTransfer e) {
            throw new IllegalResourceTransfer(e.getMessage());
        }

        this.removeResources(resource, amount);
    }

    /**
     * Adds to the container a given amount of a given resource
     * @param resource the resource to add
     * @param amount the amount of resource to add
     * @throws IllegalResourceTransfer if the resource or the amount cannot be added to the container
     */
    public abstract void addResources(ResourceSingle resource, int amount) throws IllegalResourceTransfer;

    /**
     * Removes from the container a given amount of a given resource
     * @param resource the resource to remove
     * @param amount the amount of resource to remove
     * @throws IllegalResourceTransfer if the resource or the amount cannot be removed from the container
     */
    public abstract void removeResources(ResourceSingle resource, int amount) throws IllegalResourceTransfer;

    /**
     * @return a map of the stored resources with their given amount
     */
    public abstract Map<ResourceSingle, Integer> getAllResources();
}
