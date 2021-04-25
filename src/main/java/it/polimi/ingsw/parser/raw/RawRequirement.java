package it.polimi.ingsw.parser.raw;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.exceptions.IllegalRawConversionException;
import it.polimi.ingsw.gamematerials.*;
import it.polimi.ingsw.model.leader.FlagRequirement;
import it.polimi.ingsw.model.leader.LevelFlagRequirement;
import it.polimi.ingsw.model.leader.Requirement;
import it.polimi.ingsw.model.leader.ResourceRequirement;
import it.polimi.ingsw.parser.JSONSerializer;
import it.polimi.ingsw.parser.RawObject;

import java.util.NoSuchElementException;

public class RawRequirement implements RawObject<Requirement> {
    @SerializedName("type")
    private String type;

    @SerializedName(value = "flag", alternate = "color")
    private FlagColor flag;

    @SerializedName("resource")
    private String resource;

    @SerializedName("level")
    private Integer level;

    @SerializedName("amount")
    private Integer amount;

    public RawRequirement() { }

    public RawRequirement(FlagRequirement flagRequirement) {
        type = "flag";

        flag = flagRequirement.getFlag().getColor();
        amount = flagRequirement.getAmount();
    }

    public RawRequirement(LevelFlagRequirement levelFlagRequirement) {
        type = "level_flag";

        flag = levelFlagRequirement.getFlag().getColor();
        level = levelFlagRequirement.getFlag().getLevel();
        amount = levelFlagRequirement.getAmount();
    }

    public RawRequirement(ResourceRequirement resourceRequirement) {
        type = "resource";

        resource = resourceRequirement.getResource().getId();
        amount = resourceRequirement.getAmount();
    }

    public RawRequirement(Requirement requirement) {
        if(requirement instanceof FlagRequirement) {
            FlagRequirement flagRequirement = (FlagRequirement) requirement;

            type = "flag";

            flag = flagRequirement.getFlag().getColor();
            amount = flagRequirement.getAmount();
        }
        else if(requirement instanceof LevelFlagRequirement) {
            LevelFlagRequirement levelFlagRequirement = (LevelFlagRequirement) requirement;

            type = "level_flag";

            flag = levelFlagRequirement.getFlag().getColor();
            level = levelFlagRequirement.getFlag().getLevel();
            amount = levelFlagRequirement.getAmount();
        }
        else if(requirement instanceof ResourceRequirement) {
            ResourceRequirement resourceRequirement = (ResourceRequirement) requirement;

            type = "resource";

            resource = resourceRequirement.getResource().getId();
            amount = resourceRequirement.getAmount();
        }
        else
            throw new IllegalArgumentException("Unsupported requirement!");
    }

    public String getType() {
        return type;
    }

    public FlagColor getFlag() {
        return flag;
    }

    public String getResource() {
        return resource;
    }

    public int getLevel() {
        return level;
    }

    public int getAmount() {
        return amount;
    }

    public Requirement toObject() throws IllegalRawConversionException {
        if(type == null)
            throw new IllegalRawConversionException("Missing field \"type\" for requirement");

        switch (type) {
            case "flag":
                if(flag == null)
                    throw new IllegalRawConversionException("Illegal or absent field \"flag\" for a \"" + type + "\" requirement");

                if(amount == null)
                    throw new IllegalRawConversionException("Missing field \"amount\" for a \"" + type + "\" requirement");

                try {
                    return new FlagRequirement(new BaseFlag(flag), amount);
                } catch (IllegalArgumentException e) {
                    throw new IllegalRawConversionException(e.getMessage());
                }

            case "level_flag":
            case "levelFlag":
                if(flag == null)
                    throw new IllegalRawConversionException("Illegal or absent field \"flag\" for a \"" + type + "\" requirement");

                if(amount == null)
                    throw new IllegalRawConversionException("Missing field \"amount\" for a \"" + type + "\" requirement");

                if(level == null)
                    throw new IllegalRawConversionException("Missing field \"level\" for a \"" + type + "\" requirement");

                try {
                    return new LevelFlagRequirement(new LevelFlag(flag, level), amount);
                } catch (IllegalArgumentException e) {
                    throw new IllegalRawConversionException(e.getMessage());
                }

            case "resource":
                if(resource == null)
                    throw new IllegalRawConversionException("Missing field \"resource\" for a \"" + type + "\" requirement");

                if(amount == null)
                    throw new IllegalRawConversionException("Missing field \"amount\" for a \"" + type + "\" requirement");

                ResourceSingle r;

                try {
                    r = ResourceTypeSingleton.getInstance().getResourceSingleByName(resource);
                } catch (NoSuchElementException e) {
                    throw new IllegalRawConversionException("\"" + resource + "\" is not an available resource");
                }

                try {
                    return new ResourceRequirement(r, amount);
                } catch (IllegalArgumentException e) {
                    throw new IllegalRawConversionException(e.getMessage());
                }

            default:
                throw new IllegalRawConversionException("Unknown type \"" + type + "\"");
        }
    }

    @Override
    public String toString() {
        return JSONSerializer.toJson(this);
    }
}
