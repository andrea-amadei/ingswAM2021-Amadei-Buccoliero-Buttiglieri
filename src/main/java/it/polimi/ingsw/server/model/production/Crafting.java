package it.polimi.ingsw.server.model.production;

import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.exceptions.NegativeCraftingIngredientException;
import it.polimi.ingsw.common.exceptions.NotReadyToCraftException;
import it.polimi.ingsw.server.model.basetypes.*;
import it.polimi.ingsw.server.model.faithpath.FaithPath;
import it.polimi.ingsw.server.model.global.Player;
import it.polimi.ingsw.common.parser.raw.RawCrafting;
import it.polimi.ingsw.common.parser.raw.RawStorage;
import it.polimi.ingsw.server.Logger;
import it.polimi.ingsw.common.parser.SerializableObject;
import it.polimi.ingsw.common.utils.PayloadFactory;

import java.util.*;

/**
 * The Crafting class represents the recipe for a crafting.
 * It's defined by it's input and output ingredients. Can also output faith points.
 * The class is immutable.
 */
public class Crafting implements SerializableObject<RawCrafting> {
    private final Map<ResourceType, Integer> input;
    private final Map<ResourceType, Integer> output;
    private final int faithOutput;

    private final transient Set<ResourceGroup> undecided;
    private final transient Map<ResourceGroup, Map<ResourceSingle, Integer>> conversion;

    private transient boolean allResourcesTransferred;

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

        allResourcesTransferred = false;
    }

    /**
     * Returns a copy of the inputs
     * @return a copy of the inputs
     */
    public Map<ResourceType, Integer> getInput() {
        return new HashMap<>(input);
    }

    /**
     * Returns a copy of the outputs
     * @return a copy of the outputs
     */
    public Map<ResourceType, Integer> getOutput() {
        return new HashMap<>(output);
    }

    /**
     * Returns the faith points outputted by the recipe
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
     * Resets all undecided outputs
     */
    public void resetUndecidedOutputs() {
        conversion.clear();
        undecided.clear();

        for(ResourceType i : output.keySet())
            if(i.isGroup()) {
                conversion.put((ResourceGroup) i, null);
                undecided.add((ResourceGroup) i);
            }
    }

    /**
     * Returns true if all the necessary resources have been transferred, false otherwise
     * @return true if all the necessary resources have been transferred, false otherwise
     */
    public boolean hasAllResourcesTransferred() {
        return allResourcesTransferred;
    }

    /**
     * Sets if all resources have been transferred or not
     * @param allResourcesTransferred true if all resources have been transferred, false otherwise
     */
    public void setAllResourcesTransferred(boolean allResourcesTransferred) {
        this.allResourcesTransferred = allResourcesTransferred;
    }

    /**
     * Returns true if the crafting pot contains all the necessary ingredients and all the undecided resources are
     * resolved, false otherwise.
     * @return true the crafting recipe is ready to craft, false otherwise
     */
    public boolean readyToCraft() {
        return undecided.size() == 0 && allResourcesTransferred;
    }

    /**
     * Activates the current crafting and converts all necessary resources
     * @param player the player who performed the action
     */
    public List<PayloadComponent> activateCrafting(Player player, FaithPath faithPath) {
        if(player == null)
            throw new NullPointerException();

        if(!readyToCraft())
            if(undecided.size() != 0)
                throw new NotReadyToCraftException("Not all undecided outputs are resolved");
            else
                throw new NotReadyToCraftException("Not all ingredients have been transferred");

        List<PayloadComponent> payload = new ArrayList<>();

        Map<String, Integer> addedResources = new HashMap<>();
        for(ResourceType i : output.keySet())
            if(!i.isGroup()) {
                player.getBoard().getStorage().getChest().addResources((ResourceSingle) i, output.get(i));
                addedResources.putIfAbsent(i.toString().toLowerCase(), 0);
                addedResources.put(i.toString().toLowerCase(), addedResources.get(i.toString().toLowerCase()) + output.get(i));
            }

        for(ResourceGroup i : conversion.keySet())
            for(ResourceSingle j : conversion.get(i).keySet()) {
                player.getBoard().getStorage().getChest().addResources(j, conversion.get(i).get(j));
                addedResources.putIfAbsent(j.toString().toLowerCase(), 0);
                addedResources.put(j.toString().toLowerCase(), addedResources.get(j.toString().toLowerCase()) + conversion.get(i).get(j));
            }

        payload.add(PayloadFactory.changeResources(player.getUsername(),
                new RawStorage("Chest", addedResources)));


        if(faithOutput > 0)
            payload.addAll(faithPath.executeMovement(faithOutput, player));


        resetUndecidedOutputs();
        allResourcesTransferred = false;

        return payload;
    }

    @Override
    public RawCrafting toRaw() {
        return new RawCrafting(this);
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
        Logger.log(toString());
    }
}
