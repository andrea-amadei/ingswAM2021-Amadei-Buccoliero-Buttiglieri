package it.polimi.ingsw.model.leader;

import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.model.Player;

/**
 * Class ResourceRequirement implements Requirement Interface
 * represents the requirement of a set amount of resources in the player inventory
 */
public class ResourceRequirement implements Requirement{

    private final ResourceSingle resource;
    private final int amount;

    /**
     * ResourceRequirement Constructor
     * @param resource the type of resource required
     * @param amount the amount of resources of said type required
     * @throws IllegalArgumentException if resource negative or null resources amount
     * @throws NullPointerException if resource pointer is null
     */
    public ResourceRequirement(ResourceSingle resource, int amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("Resource amount cannot be zero or negative");

        if(resource == null)
            throw new NullPointerException();

        this.resource = resource;
        this.amount = amount;
    }

    /**
     * get amount required
     * @return the amount of resources of a set type required
     */
    public int getAmount() {
        return amount;
    }

    /**
     * get resource required
     * @return the type of resource required
     */
    public ResourceSingle getResource() {
        return resource;
    }

    @Override
    public boolean isSatisfied(Player player) {

        //TODO: isSatisfied function
        return false;
    }
}
