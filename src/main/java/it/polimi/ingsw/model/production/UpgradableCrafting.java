package it.polimi.ingsw.model.production;

import it.polimi.ingsw.gamematerials.ResourceType;

import java.util.Map;

/**
 * The UpgradableCrafting class extents the Crafting class by adding a level to it.
 * The class is immutable.
 */
public class UpgradableCrafting extends Crafting {
    private final int level;

    /**
     * Creates a new Upgradable Crafting Recipe (with a level)
     * @param input the input ingredients of the recipe and their respective amount.
     *              Can either be a single resource or a group. Cannot be empty.
     *              Ingredients amounts must be positives.
     * @param output the output ingredients of the recipe and their amount.
     *               Can either be a single resource or a group. Can be empty if faithOutput is not zero.
     *               Ingredients amounts must be positives.
     * @param faithOutput the faith points outputted by the recipe, Cannot be negative
     * @param level the level of the crafting recipe
     * @throws NullPointerException if at least one of the parameters is null
     * @throws IllegalArgumentException if inputs or outputs are empty, faith points is negative or level exceed
     *                                  the limits of GameParameters
     */
    public UpgradableCrafting(Map<ResourceType, Integer> input, Map<ResourceType, Integer> output, int faithOutput, int level) {
        super(input, output, faithOutput);

        this.level = level;
    }

    /**
     * @return the current level of the recipe
     */
    public int getLevel() {
        return level;
    }
}
