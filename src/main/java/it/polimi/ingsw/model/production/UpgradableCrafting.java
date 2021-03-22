package it.polimi.ingsw.model.production;

import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceType;

import java.util.Map;

public class UpgradableCrafting extends Crafting {
    private final int level;
    private static final int MIN_LEVEL = 1;
    private static final int MAX_LEVEL = 3;

    public UpgradableCrafting(Map<ResourceType, Integer> input, Map<ResourceType, Integer> output, int faithOutput, int level) {
        super(input, output, faithOutput);

        if(level < MIN_LEVEL || level > MAX_LEVEL)
            throw new IllegalArgumentException("Level not valid");

        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
