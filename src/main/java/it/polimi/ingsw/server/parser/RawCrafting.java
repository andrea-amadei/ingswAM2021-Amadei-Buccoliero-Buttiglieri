package it.polimi.ingsw.server.parser;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.gamematerials.ResourceType;

import java.util.Map;

public class RawCrafting {
    @SerializedName("input")
    private Map<String, Integer> input;

    @SerializedName("output")
    private Map<String, Integer> output;

    @SerializedName(value = "faithOutput", alternate = "faith_output")
    private int faithOutput;

    public Map<String, Integer> getInput() {
        return input;
    }

    public Map<String, Integer> getOutput() {
        return output;
    }

    public int getFaithOutput() {
        return faithOutput;
    }
}
