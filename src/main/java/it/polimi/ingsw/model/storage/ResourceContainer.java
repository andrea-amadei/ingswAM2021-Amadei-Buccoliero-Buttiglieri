package it.polimi.ingsw.model.storage;

import it.polimi.ingsw.exceptions.IllegalResourceTransferException;
import it.polimi.ingsw.gamematerials.ResourceSingle;

import java.util.*;

/**
 * The abstract class ResourceContainer defines the standard methods to implement any container of resources.
 * Any Resource Container should:
 * <ul>
 *     <li>be able to add resources to itself</li>
 *     <li>be able to remove resources from himself</li>
 *     <li>be able to transfer resources from itself to any other Resource Container</li>
 *     <li>return a representation of itself via a Map collection, including any resource they contain and
 *      their amount. An empty map is admitted if the container is empty.
 *     </li>
 * </ul>
 */
public abstract class ResourceContainer {
    /**
     * Moves a given amount of a given resource from a Resource Container to another
     * @param to the Resource Container to transfer the resources to
     * @param resource the resource to transfer
     * @param amount the amount to transfer
     * @throws IllegalResourceTransferException if either:<ul>
     *     <li>the receiver container doesn't accept the resource or the amount</li>
     *     <li>the requested resource or amount is not available in the sender container</li></ul>
     * @throws NullPointerException if to or resource are null
     * @throws IllegalArgumentException if amount is non-positive
     */
    public void moveTo(ResourceContainer to, ResourceSingle resource, int amount) throws IllegalResourceTransferException {
        if(to == null || resource == null)
            throw new NullPointerException();

        if(amount <= 0)
            throw new IllegalArgumentException("The amount to move must be positive");

        if(!getAllResources().containsKey(resource))
            throw new IllegalResourceTransferException("No resources \"" + resource.getId() + "\" available to move");

        if(getAllResources().get(resource) < amount)
            throw new IllegalResourceTransferException("Not enough resources \"" + resource.getId() + "\" available to move");

        try {
            to.addResources(resource, amount);
        } catch (IllegalResourceTransferException e) {
            throw new IllegalResourceTransferException(e.getMessage());
        }

        this.removeResources(resource, amount);
    }

    /**
     * Adds to the container a given amount of a given resource
     * @param resource the resource to add
     * @param amount the amount of resource to add
     * @throws IllegalResourceTransferException if the resource or the amount cannot be added to the container
     */
    public abstract void addResources(ResourceSingle resource, int amount) throws IllegalResourceTransferException;

    /**
     * Removes from the container a given amount of a given resource
     * @param resource the resource to remove
     * @param amount the amount of resource to remove
     * @throws IllegalResourceTransferException if the resource or the amount cannot be removed from the container
     */
    public abstract void removeResources(ResourceSingle resource, int amount) throws IllegalResourceTransferException;

    /**
     * Returns a map of the stored resources with their given amount
     * @return a map of the stored resources with their given amount
     */
    public abstract Map<ResourceSingle, Integer> getAllResources();
}
