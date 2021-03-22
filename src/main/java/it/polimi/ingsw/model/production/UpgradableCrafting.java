package it.polimi.ingsw.model.production;

import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceType;
import it.polimi.ingsw.model.GameParameters;

import java.util.Map;

public class UpgradableCrafting extends Crafting {
    private final int level;

    public UpgradableCrafting(Map<ResourceType, Integer> input, Map<ResourceType, Integer> output, int faithOutput, int level) {
        super(input, output, faithOutput);

        if(level < GameParameters.MIN_CARD_LEVEL || level > GameParameters.MAX_CARD_LEVEL)
            throw new IllegalArgumentException("Level not valid");

        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
