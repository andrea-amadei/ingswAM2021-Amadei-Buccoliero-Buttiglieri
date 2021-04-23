package it.polimi.ingsw.model.leader;

import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.parser.raw.RawRequirement;

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

    /**
     * method verifies that the player has enough resources to satisfy the requirement
     * @param player the player who is being verified
     * @return true iff the player satisfies the requirements
     * @throws NullPointerException if the pointer to player is null
     */
    @Override
    public boolean isSatisfied(Player player) {

        if(player == null)
            throw new NullPointerException();

        Integer availableAmount = player.getBoard().getStorage().getStoredResources().get(resource);
        if(availableAmount == null)
            return false;

        return availableAmount >= amount;
    }

    /**
     * function represents the requirement as a string
     * @return the resource requirement as a string
     */
    @Override
    public String toString() {
        return "ResourceRequirement{" +
                "resource=" + resource +
                ", amount=" + amount +
                '}';
    }

    @Override
    public RawRequirement toRaw() {
        return new RawRequirement(this);
    }
}
