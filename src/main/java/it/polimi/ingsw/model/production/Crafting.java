package it.polimi.ingsw.model.production;

import it.polimi.ingsw.exceptions.NegativeCraftingIngredientException;
import it.polimi.ingsw.gamematerials.*;
import it.polimi.ingsw.model.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * The Crafting class represents the recipe for a crafting.
 * It's defined by it's input and output ingredients. Can also output faith points.
 * The class is immutable.
 */
public class Crafting {
    private final Map<ResourceType, Integer> input;
    private final Map<ResourceType, Integer> output;
    private final int faithOutput;

    //TODO: Add crafting pot
    //private BaseStorage craftingPot;

    /**
     * Creates a new crafting recipe
     * @param input the input ingredients of the recipe and their respective amount.
     *              Can either be a single resource or a group. Cannot be empty.
     *              Ingredients amounts must be positives.
     * @param output the output ingredients of the recipe and their amount.
     *               Can either be a single resource or a group. Can be empty if faithOutput is not zero.
     *               Ingredients amounts must be positives.
     * @param faithOutput the faith points outputted by the recipe, Cannot be negative
     * @throws NullPointerException if at least one of the parameters is null
     * @throws IllegalArgumentException if inputs are empty or output is empty and faith points is not positive
     * @throws NegativeCraftingIngredientException if inputs or outputs contain below zero amounts
     */
    public Crafting(Map<ResourceType, Integer> input, Map<ResourceType, Integer> output, int faithOutput) {
        if(input == null || output == null)
            throw new NullPointerException();

        if(faithOutput < 0)
            throw new IllegalArgumentException("Faith output cannot be negative");

        if(input.size() == 0)
            throw new IllegalArgumentException("Input cannot be empty");
        if(output.size() == 0 && faithOutput <= 0)
            throw new IllegalArgumentException("Output cannot be empty if faithOutput is 0");

        for(ResourceType i : input.keySet())
            if(input.get(i) <= 0)
                throw new NegativeCraftingIngredientException("Input cannot accept negative amounts of ingredients");

        for(ResourceType i : output.keySet())
            if(output.get(i) <= 0)
                throw new NegativeCraftingIngredientException("Output cannot accept negative amounts of ingredients");

        this.input = input;
        this.output = output;
        this.faithOutput = faithOutput;
    }

    /**
     * @return a copy of the inputs
     */
    public Map<ResourceType, Integer> getInput() {
        return new HashMap<>(input);
    }

    /**
     * @return a copy of the outputs
     */
    public Map<ResourceType, Integer> getOutput() {
        return new HashMap<>(output);
    }

    /**
     * @return the faith points outputted by the recipe
     */
    public int getFaithOutput() {
        return faithOutput;
    }

    /*
    public boolean hasAllIngredients() {
        //TODO: Add this
        return true;
    }

    public void activateCrafting(Player player) {
        //TODO: Add this
    }
    */
}
