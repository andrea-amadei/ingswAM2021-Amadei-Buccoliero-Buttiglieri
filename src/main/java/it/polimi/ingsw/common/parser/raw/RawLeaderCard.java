package it.polimi.ingsw.common.parser.raw;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.common.exceptions.IllegalRawConversionException;
import it.polimi.ingsw.server.model.leader.LeaderCard;
import it.polimi.ingsw.server.model.leader.Requirement;
import it.polimi.ingsw.server.model.leader.SpecialAbility;
import it.polimi.ingsw.common.parser.JSONSerializer;
import it.polimi.ingsw.common.parser.UniqueRawObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RawLeaderCard implements UniqueRawObject<LeaderCard> {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("points")
    private Integer points;

    @SerializedName(value = "special_abilities", alternate = {"specialAbilities", "abilities"})
    private List<RawSpecialAbility> abilities;

    @SerializedName("requirements")
    private List<RawRequirement> requirements;

    public RawLeaderCard() { }

    public RawLeaderCard(LeaderCard leaderCard) {
        id = leaderCard.getId();
        name = leaderCard.getName();
        points = leaderCard.getPoints();

        abilities = leaderCard.getAbilities()   .stream()
                                                .map(RawSpecialAbility::new)
                                                .collect(Collectors.toList());

        requirements = leaderCard.getRequirements() .stream()
                                                    .map(RawRequirement::new)
                                                    .collect(Collectors.toList());
    }

    @Override
    public String getStringId() {
        return String.format("%03d", id);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public List<RawSpecialAbility> getAbilities() {
        return abilities;
    }

    public List<RawRequirement> getRequirements() {
        return requirements;
    }

    @Override
    public LeaderCard toObject() throws IllegalRawConversionException {
        if(name == null)
            throw new IllegalRawConversionException("Missing mandatory field \"name\"");

        if(abilities == null)
            throw new IllegalRawConversionException("Missing mandatory field \"abilities\"");

        if(requirements == null)
            throw new IllegalRawConversionException("Missing mandatory field \"requirements\"");

        if(points == null)
            throw new IllegalRawConversionException("Missing mandatory field \"points\"");

        List<SpecialAbility> a = new ArrayList<>(abilities.size());
        List<Requirement> r = new ArrayList<>(requirements.size());

        try {
            for(RawSpecialAbility i : abilities)
                a.add(i.toObject());

            for(RawRequirement i : requirements)
                r.add(i.toObject());

            return new LeaderCard(id, name, points, a, r);
        } catch (IllegalArgumentException e) {
            throw new IllegalRawConversionException(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return JSONSerializer.toJson(this);
    }
}
