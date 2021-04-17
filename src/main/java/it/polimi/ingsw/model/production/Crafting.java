package it.polimi.ingsw.model.production;

import it.polimi.ingsw.exceptions.NegativeCraftingIngredientException;
import it.polimi.ingsw.exceptions.NotReadyToCraftException;
import it.polimi.ingsw.gamematerials.*;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.storage.LimitedStorage;
import it.polimi.ingsw.server.Console;
import it.polimi.ingsw.parser.SerializedObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The Crafting class represents the recipe for a crafting.
 * It's defined by it's input and output ingredients. Can also output faith points.
 * The class is immutable.
 */
public class Crafting implements SerializedObject {
    private final Map<ResourceType, Integer> input;
    private final Map<ResourceType, Integer> output;
    private final int faithOutput;

    private final transient Set<ResourceGroup> undecided;
    private final transient Map<ResourceGroup, Map<ResourceSingle, Integer>> conversion;

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

        conversion = new HashMap<>();
        undecided = new HashSet<>();

        for(ResourceType i : output.keySet())
            if(i.isGroup()) {
                conversion.put((ResourceGroup) i, null);
                undecided.add((ResourceGroup) i);
            }
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

    /**
     * Returns a set containing all the undecided outputs of the crafting recipe, in particular all ambiguous resources
     * that needs to be specified in order to start the crafting. If the set is empty there are no more unspecified
     * resources
     * @return a map containing the undecided outputs. Empty if no outputs still needs to be decided
     */
    public Set<ResourceGroup> getUndecidedOutputs() {
        return new HashSet<>(undecided);
    }

    /**
     * Returns the selected conversion of a given ResourceGroup. The result is a Map containing all the resources in
     * which the group will be converted and their respective amounts. If the conversion still needs to be selected, it
     * will return null.
     * @param group the ResourceGroup to get the conversion from
     * @return a map containing the selected resources to convert group to, with their respective amount. If no
     *         conversion was selected yet, returns null.
     * @throws NullPointerException if group is null
     * @throws IllegalArgumentException if group is not part of the output
     */
    public Map<ResourceSingle, Integer> getGroupConversion(ResourceGroup group) {
        if(group == null)
            throw new NullPointerException();

        if(!conversion.containsKey(group))
            throw new IllegalArgumentException("The specified group is not part of the recipe output");

        return conversion.get(group);
    }

    /**
     * Sets the given ResourceGroup to a group of ResourceSingle with their specified amounts
     * @param from the ResourceGroup to be converted
     * @param to a map containing all the ResourceSingle to convert the given ResourceGroup to. All the amounts must
     *           add up, meaning no resource can be left unconverted. No over conversion is allowed either.
     * @throws NullPointerException if from or to are null
     * @throws IllegalArgumentException if the specified from parameter is not part of the output, if it is not possible
     *                                  to convert one resource to the other or if the given amounts don't add up
     */
    public void setGroupConversion(ResourceGroup from, Map<ResourceSingle, Integer> to) {
        if(from == null || to == null)
            throw new NullPointerException();

        if(!conversion.containsKey(from))
            throw new IllegalArgumentException("The specified group is not part of the recipe output");

        int n = 0;
        for(ResourceSingle i : to.keySet()) {
            if (!i.isA(from))
                throw new IllegalArgumentException("Cannot convert from " + from.getId() + " to " + i.getId());

            n += to.get(i);
        }

        if(output.get(from) != n)
            throw new IllegalArgumentException("Cannot convert " + output.get(from) + " " + from.getId() +
                    " to " + n + " total resources");

        undecided.remove(from);
        conversion.put(from, to);
    }

    /**
     * Returns true if the crafting pot contains all the necessary ingredients and all the undecided resources are
     * resolved, false otherwise.
     * @return true the crafting recipe is ready to craft, false otherwise
     */
    public boolean readyToCraft() {
        return undecided.size() == 0;
    }

    /**
     * Activates the current crafting and converts all necessary resources
     * @param player the player who performed the action
     */
    public void activateCrafting(Player player) {
        if(player == null)
            throw new NullPointerException();

        if(!readyToCraft())
            if(undecided.size() != 0)
                throw new NotReadyToCraftException("Not all undecided outputs are resolved");
            else
                throw new NotReadyToCraftException("Crafting pot doesn't contain all the necessary ingredients");

        for(ResourceType i : output.keySet())
            if(!i.isGroup())
                player.getBoard().getStorage().getChest().addResources((ResourceSingle) i, output.get(i));


        for(ResourceGroup i : conversion.keySet())
            for(ResourceSingle j : conversion.get(i).keySet())
                player.getBoard().getStorage().getChest().addResources(j, conversion.get(i).get(j));

        // TODO: Remake this with the faith path
        if(faithOutput > 0)
            player.getBoard().getFaithHolder().addFaithPoints(faithOutput);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder(this.getClass().getSimpleName()).append("{");

        for(ResourceType i : input.keySet())
            str.append(i).append(": ").append(input.get(i)).append(" ");

        str.append("-> ");

        for(ResourceType i : output.keySet())
            str.append(i).append(": ").append(output.get(i)).append(" ");

        str.append("+ Faith: ").append(faithOutput).append("}");

        return str.toString();
    }

    @Override
    public void printDebugInfo() {
        Console.log(toString());
    }
}
