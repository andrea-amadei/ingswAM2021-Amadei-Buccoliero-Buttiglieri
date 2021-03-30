package it.polimi.ingsw.model.storage;

import it.polimi.ingsw.exceptions.IllegalCupboardException;
import it.polimi.ingsw.gamematerials.ResourceSingle;

import java.util.Set;

/**
 * Defines methods for managing the resources stored in the shelves of the player
 */
public interface Cupboard {

    /**
     * Returns all the shelves contained in the cupboard
     * @return the set of all shelves contained in the cupboard
     */
    Set<Shelf> getShelves();

    /**
     * Gets the shelf with the desired id
     * @param id the id of the desired shelf
     * @return the selected shelf
     */
    Shelf getShelfById(String id);

    /**
     * Moves an amount of resources from one shelf to another
     * @param from the shelf from which resources are taken
     * @param to the shelf to which resources are moved
     * @param amount the amount of resources to transfer
     */
    void moveBetweenShelves(Shelf from, Shelf to, int amount) throws IllegalCupboardException;


    /**
     * Adds an amount of resources to the specified shelf
     * @param to the shelf to which the resources are added
     * @param resource the type of the resource to add
     * @param amount the amount of resources to add
     */
    void addResource(Shelf to, ResourceSingle resource, int amount) throws IllegalCupboardException;

    /**
     * Adds an amount of resources to the specified shelf from the specified ResourceContainer
     * @param container the ResourceContainer from which resources are taken
     * @param to the shelf to which the resources are added
     * @param resource the type of the resource to add
     * @param amount the amount of resources to add
     */
    void addResourceFromContainer(ResourceContainer container, Shelf to, ResourceSingle resource, int amount) throws IllegalCupboardException;

    /**
     * Removes the specified amount from the shelf
     * @param from the shelf from which resources are taken
     * @param amount the amount of resources to remove
     */
    void removeResource(Shelf from, int amount) throws IllegalCupboardException;

    /**
     * Moves resources from a shelf to a container
     * @param container the ResourceContainer to which resources are added
     * @param from the Shelf from which resources are removed
     * @param resource the resource type of the transferred resources
     * @param amount the amount of the transferred resources
     * @throws IllegalCupboardException if the transaction can't be performed
     */
    void moveResourceToContainer(ResourceContainer container, Shelf from, ResourceSingle resource, int amount) throws IllegalCupboardException;

    /**
     * Returns true if the cupboard contains the indicated shelf
     * @return true if the cupboard contains the indicated shelf
     */
    boolean contains(Shelf shelf);

}
