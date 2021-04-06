package it.polimi.ingsw.server.parser;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class RawLeaderCard {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("points")
    private int points;

    @SerializedName(value = "special_abilities", alternate = {"specialAbilities", "abilities"})
    private List<RawSpecialAbility> abilities;

    @SerializedName("requirements")
    private List<RawRequirement> requirements;
}
