package it.polimi.ingsw.parser.raw;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.exceptions.IllegalRawConversionException;
import it.polimi.ingsw.exceptions.NegativeCraftingIngredientException;
import it.polimi.ingsw.gamematerials.ResourceType;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.production.Crafting;
import it.polimi.ingsw.parser.JSONSerializer;
import it.polimi.ingsw.parser.RawObject;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class RawCrafting implements RawObject<Crafting> {
    @SerializedName("input")
    private Map<String, Integer> input;

    @SerializedName("output")
    private Map<String, Integer> output;

    @SerializedName(value = "faithOutput", alternate = "faith_output")
    private int faithOutput;

    public RawCrafting() { }

    public RawCrafting(Crafting crafting) {
        if(crafting == null)
            throw new NullPointerException();

        input = crafting.getInput()
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(e -> e.getKey().getId().toUpperCase(), Map.Entry::getValue));

        output = crafting   .getOutput()
                            .entrySet()
                            .stream()
                            .collect(Collectors.toMap(e -> e.getKey().getId().toUpperCase(), Map.Entry::getValue));

        faithOutput = crafting.getFaithOutput();
    }

    public Map<String, Integer> getInput() {
        return input;
    }

    public Map<String, Integer> getOutput() {
        return output;
    }

    public int getFaithOutput() {
        return faithOutput;
    }

    public Crafting toObject() throws IllegalRawConversionException {
        if(input == null)
            throw new IllegalRawConversionException("Missing mandatory field \"input\" in crafting");

        if(output == null)
            throw new IllegalRawConversionException("Missing mandatory field \"output\" in crafting");

        Map<ResourceType, Integer> in = new HashMap<>(input.size());
        Map<ResourceType, Integer> out = new HashMap<>(output.size());
        Crafting c;

        // convert inputs
        for(String i : input.keySet())
            try {
                in.put(ResourceTypeSingleton.getInstance().getResourceTypeByName(i), input.get(i));
            } catch (NoSuchElementException e) {
                throw new IllegalRawConversionException("\"" + i + "\" is not an available resource");
            }

        // convert outputs
        for(String i : output.keySet())
            try {
                out.put(ResourceTypeSingleton.getInstance().getResourceTypeByName(i), output.get(i));
            } catch (NoSuchElementException e) {
                throw new IllegalRawConversionException("\"" + i + "\" is not an available resource");
            }

        // create object
        try {
            c = new Crafting(in, out, faithOutput);
        } catch (IllegalArgumentException | NegativeCraftingIngredientException e) {
            throw new IllegalRawConversionException(e.getMessage());
        }

        return c;
    }

    @Override
    public String toString() {
        return JSONSerializer.toJson(this);
    }
}
