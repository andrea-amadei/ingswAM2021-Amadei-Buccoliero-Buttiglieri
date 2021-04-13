package it.polimi.ingsw.model.production;

import it.polimi.ingsw.exceptions.NegativeCostException;
import it.polimi.ingsw.gamematerials.LevelFlag;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.server.Console;
import it.polimi.ingsw.parser.SerializedObject;

import java.util.HashMap;
import java.util.Map;

/**
 * The CraftingCard class defines a crafting card, composed by a flag, a cost and a crafting recipe.
 * Can grant victory points.
 * The class is immutable.
 */
public class CraftingCard implements SerializedObject {
    private final int id;
    private final LevelFlag flag;
    private final Map<ResourceSingle, Integer> cost;
    private final UpgradableCrafting crafting;
    private final int points;

    /**
     * Creates a new crafting card
     * @param flag the flag of the card
     * @param cost the cost in resources of the card. Cannot accept resource groups. Cannot be empty.
     *             Every resource amount must be positive
     * @param crafting the crafting recipe granted by the card
     * @param points the victory points granted by the card. Cannot be negative.
     * @throws NullPointerException if any of the arguments are null
     * @throws IllegalArgumentException if the cost is empty or negative, the victory points are negative or
     *                                  the level of the flag is different from the one of the crafting.
     * @throws NegativeCostException if the  cost contains below zero amounts of resources
     */
    public CraftingCard(int id, LevelFlag flag, Map<ResourceSingle, Integer> cost, UpgradableCrafting crafting, int points) {
        if(id <= 0)
            throw new IllegalArgumentException("Id number must be positive");

        if(flag == null || cost == null || crafting == null)
            throw new NullPointerException();

        if(cost.size() == 0)
            throw new IllegalArgumentException("Cost cannot be empty");

        for(ResourceSingle i : cost.keySet())
            if(cost.get(i) <= 0)
                throw new NegativeCostException("Cost cannot accept negative amounts of resources");

        if(points < 0)
            throw new IllegalArgumentException("Points cannot be negative a negative amount");

        if(flag.getLevel() != crafting.getLevel())
            throw new IllegalArgumentException("Flag level doesn't match crafting level");

        this.id = id;
        this.flag = flag;
        this.cost = cost;
        this.crafting = crafting;
        this.points = points;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the flag
     */
    public LevelFlag getFlag() {
        return flag;
    }

    /**
     * @return the cost of the card
     */
    public Map<ResourceSingle, Integer> getCost() {
        return new HashMap<>(cost);
    }

    /**
     * @return the crafting recipe held by the card
     */
    public UpgradableCrafting getCrafting() {
        return crafting;
    }

    /**
     * @return the victory points granted by the card
     */
    public int getPoints() {
        return points;
    }

    @Override
    public void printDebugInfo() {
        Console.log("Id: " + getId());
        Console.log("Points: " + getPoints());
        Console.log("Flag: " + getFlag());
        Console.log("Cost: " + getCost());
        Console.log("Crafting: " + getCrafting());
    }
}
