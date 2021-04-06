package it.polimi.ingsw.server.parser;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.exceptions.IllegalRawConversionException;
import it.polimi.ingsw.gamematerials.*;
import it.polimi.ingsw.model.leader.FlagRequirement;
import it.polimi.ingsw.model.leader.LevelFlagRequirement;
import it.polimi.ingsw.model.leader.Requirement;
import it.polimi.ingsw.model.leader.ResourceRequirement;

import java.util.NoSuchElementException;

public class RawRequirement {
    @SerializedName("type")
    private String type;

    @SerializedName(value = "flag", alternate = "color")
    private FlagColor flag;

    @SerializedName("resource")
    private String resource;

    @SerializedName("level")
    private int level;

    @SerializedName("amount")
    private int amount;

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

    public Requirement toRequirement(int id) throws IllegalRawConversionException {
        if(type == null)
            throw new IllegalRawConversionException("Mandatory field \"type\" is missing (id: " + id + ")");

        switch (type) {
            case "flag":
                if(flag == null)
                    throw new IllegalRawConversionException("Illegal or absent field \"flag\" for a \"" + type + "\" requirement (id: " + id + ")");

                if(amount == 0)
                    throw new IllegalRawConversionException("Illegal or absent field \"amount\" for a \"" + type + "\" requirement (id: " + id + ")");

                try {
                    return new FlagRequirement(new BaseFlag(flag), amount);
                } catch (IllegalArgumentException e) {
                    throw new IllegalRawConversionException(e.getMessage() + " (id: " + id + ")");
                }

            case "level_flag":
            case "levelFlag":
                if(flag == null)
                    throw new IllegalRawConversionException("Illegal or absent field \"flag\" for a \"" + type + "\" requirement (id: " + id + ")");

                if(amount == 0)
                    throw new IllegalRawConversionException("Illegal or absent field \"amount\" for a \"" + type + "\" requirement (id: " + id + ")");

                if(level == 0)
                    throw new IllegalRawConversionException("Illegal or absent field \"level\" for a \"" + type + "\" requirement (id: " + id + ")");

                try {
                    return new LevelFlagRequirement(new LevelFlag(flag, level), amount);
                } catch (IllegalArgumentException e) {
                    throw new IllegalRawConversionException(e.getMessage() + " (id: " + id + ")");
                }

            case "resource":
                if(resource == null)
                    throw new IllegalRawConversionException("Illegal or absent field \"resource\" for a \"" + type + "\" requirement (id: " + id + ")");

                if(amount == 0)
                    throw new IllegalRawConversionException("Illegal or absent field \"amount\" for a \"" + type + "\" requirement (id: " + id + ")");

                ResourceSingle r;

                try {
                    r = ResourceTypeSingleton.getInstance().getResourceSingleByName(resource);
                } catch (NoSuchElementException e) {
                    throw new IllegalRawConversionException("\"" + resource + "\" is not an available resource (id: " + id + ")");
                }

                try {
                    return new ResourceRequirement(r, amount);
                } catch (IllegalArgumentException e) {
                    throw new IllegalRawConversionException(e.getMessage() + " (id: " + id + ")");
                }

            default:
                throw new IllegalRawConversionException("Unknown type \"" + type + "\" (id: " + id + ")");
        }
    }
}
