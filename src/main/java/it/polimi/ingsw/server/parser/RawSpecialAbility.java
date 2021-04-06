package it.polimi.ingsw.server.parser;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.gamematerials.MarbleColor;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceType;
import it.polimi.ingsw.model.production.Crafting;

public class RawSpecialAbility {
    @SerializedName("type")
    private String type;

    @SerializedName("from")
    private MarbleColor from;

    @SerializedName(value = "resource", alternate = "to")
    private String resource;

    @SerializedName(value = "acceptedTypes", alternate = "accepted_types")
    private String acceptedTypes;

    @SerializedName("crafting")
    private RawCrafting crafting;

    @SerializedName("amount")
    private int amount;

    public String getType() {
        return type;
    }

    public MarbleColor getFrom() {
        return from;
    }

    public String getResource() {
        return resource;
    }

    public String getAcceptedTypes() {
        return acceptedTypes;
    }

    public RawCrafting getCrafting() {
        return crafting;
    }

    public int getAmount() {
        return amount;
    }
}
