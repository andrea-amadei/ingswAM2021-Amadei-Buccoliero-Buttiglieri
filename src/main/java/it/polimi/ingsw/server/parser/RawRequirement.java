package it.polimi.ingsw.server.parser;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.gamematerials.FlagColor;

public class RawRequirement {
    @SerializedName("type")
    private String type;

    @SerializedName("flag")
    private FlagColor flag;

    @SerializedName("resource")
    private String resource;

    @SerializedName("level")
    private int level;

    @SerializedName("amount")
    private int amount;
}
