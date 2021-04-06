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
    private ResourceSingle resource;

    @SerializedName(value = "acceptedTypes", alternate = "accepted_types")
    private ResourceType acceptedTypes;

    @SerializedName("crafting")
    private Crafting crafting;

    @SerializedName("amount")
    private int amount;
}
