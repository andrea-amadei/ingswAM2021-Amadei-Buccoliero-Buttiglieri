package it.polimi.ingsw.model.production;

import it.polimi.ingsw.exceptions.NegativeCostException;
import it.polimi.ingsw.gamematerials.LevelFlag;
import it.polimi.ingsw.gamematerials.ResourceSingle;

import java.util.HashMap;
import java.util.Map;

public class CraftingCard {
    private final LevelFlag flag;
    private final Map<ResourceSingle, Integer> cost;
    private final UpgradableCrafting crafting;
    private final int points;

    public CraftingCard(LevelFlag flag, Map<ResourceSingle, Integer> cost, UpgradableCrafting crafting, int points) {
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

        this.flag = flag;
        this.cost = cost;
        this.crafting = crafting;
        this.points = points;
    }

    public LevelFlag getFlag() {
        return flag;
    }

    public Map<ResourceSingle, Integer> getCost() {
        return new HashMap<>(cost);
    }

    public UpgradableCrafting getCrafting() {
        return crafting;
    }

    public int getPoints() {
        return points;
    }
}
