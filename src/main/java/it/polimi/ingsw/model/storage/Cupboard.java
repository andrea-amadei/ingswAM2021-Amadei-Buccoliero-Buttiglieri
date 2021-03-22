package it.polimi.ingsw.model.storage;

import it.polimi.ingsw.gamematerials.ResourceSingle;

import java.util.Set;

public interface Cupboard {

    /**
     *
     * @return the set of all shelves contained in the cupboard
     */
    Set<Shelf> getShelves();

    /**
     *
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
    void moveResource(Shelf from, Shelf to, int amount);

    /**
     * Add an amount of resources to the specified shelf
     * @param to the shelf to which the resources are added
     * @param resource the type of the resource to add
     * @param amount the amount of resources to add
     */
    void addResource(Shelf to, ResourceSingle resource, int amount);

    //TODO: Add the methods used to move resources from hand to shelf
}
