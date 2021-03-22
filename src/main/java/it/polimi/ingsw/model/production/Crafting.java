package it.polimi.ingsw.model.production;

import it.polimi.ingsw.exceptions.NegativeCraftingIngredientException;
import it.polimi.ingsw.gamematerials.*;
import it.polimi.ingsw.model.Player;

import java.util.HashMap;
import java.util.Map;

public class Crafting {
    private final Map<ResourceType, Integer> input;
    private final Map<ResourceType, Integer> output;
    private final int faithOutput;

    //TODO: Add crafting pot
    //private BaseStorage craftingPot;

    public Crafting(Map<ResourceType, Integer> input, Map<ResourceType, Integer> output, int faithOutput) {
        if(input == null || output == null)
            throw new NullPointerException();

        if(input.size() == 0)
            throw new IllegalArgumentException("Input cannot be empty");
        if(output.size() == 0)
            throw new IllegalArgumentException("Output cannot be empty");

        for(ResourceType i : input.keySet())
            if(input.get(i) <= 0)
                throw new NegativeCraftingIngredientException("Input cannot accept negative amounts of ingredients");

        for(ResourceType i : output.keySet())
            if(output.get(i) <= 0)
                throw new NegativeCraftingIngredientException("Output cannot accept negative amounts of ingredients");

        if(faithOutput < 0)
            throw new IllegalArgumentException("Faith output cannot be negative");

        this.input = input;
        this.output = output;
        this.faithOutput = faithOutput;
    }

    public Map<ResourceType, Integer> getInput() {
        return new HashMap<>(input);
    }

    public Map<ResourceType, Integer> getOutput() {
        return new HashMap<>(output);
    }

    public int getFaithOutput() {
        return faithOutput;
    }

    public boolean hasAllIngredients() {
        //TODO: Add this
        return true;
    }

    public void activateCrafting(Player player) {
        //TODO: Add this
    }
}
