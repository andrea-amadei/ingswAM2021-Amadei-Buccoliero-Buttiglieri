package it.polimi.ingsw.parser.raw;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.exceptions.IllegalRawConversionException;
import it.polimi.ingsw.model.leader.LeaderCard;
import it.polimi.ingsw.model.leader.Requirement;
import it.polimi.ingsw.model.leader.SpecialAbility;
import it.polimi.ingsw.server.Console;
import it.polimi.ingsw.parser.UniqueRawObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RawLeaderCard implements UniqueRawObject<LeaderCard> {
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
    public String getId() {
        return String.format("%03d", id);
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
    public LeaderCard convert() throws IllegalRawConversionException {
        if(name == null)
            throw new IllegalRawConversionException("Missing mandatory field \"name\" (id: " + id + ")");

        if(abilities == null)
            throw new IllegalRawConversionException("Missing mandatory field \"abilities\" (id: " + id + ")");

        if(requirements == null)
            throw new IllegalRawConversionException("Missing mandatory field \"requirements\" (id: " + id + ")");

        if(points == 0)
            Console.log("Points for card " + id + " are set to 0 or absent. Is it intentional?",
                    Console.Severity.WARNING, Console.Format.YELLOW);

        List<SpecialAbility> a = new ArrayList<>(abilities.size());
        List<Requirement> r = new ArrayList<>(requirements.size());

        try {
            for(RawSpecialAbility i : abilities)
                a.add(i.convert());

            for(RawRequirement i : requirements)
                r.add(i.convert());

            return new LeaderCard(id, name, points, a, r);
        } catch (IllegalArgumentException e) {
            throw new IllegalRawConversionException(e.getMessage() + " (id: " + id + ")");
        }
    }
}
