package it.polimi.ingsw.server.parser.raw;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.exceptions.IllegalRawConversionException;
import it.polimi.ingsw.gamematerials.*;
import it.polimi.ingsw.model.production.Crafting;
import it.polimi.ingsw.model.production.CraftingCard;
import it.polimi.ingsw.model.production.UpgradableCrafting;
import it.polimi.ingsw.server.Console;
import it.polimi.ingsw.server.parser.UniqueRawObject;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class RawCraftingCard implements UniqueRawObject<CraftingCard> {
    @SerializedName("id")
    private int id;

    @SerializedName(value = "flag", alternate = "color")
    private FlagColor flag;

    @SerializedName("level")
    private int level;

    @SerializedName("cost")
    private Map<String, Integer> cost;

    @SerializedName("crafting")
    private RawCrafting crafting;

    @SerializedName("points")
    private int points;

    public int getId() {
        return id;
    }

    public FlagColor getFlag() {
        return flag;
    }

    public int getLevel() {
        return level;
    }

    public Map<String, Integer> getCost() {
        return cost;
    }

    public RawCrafting getCrafting() {
        return crafting;
    }

    public int getPoints() {
        return points;
    }

    @Override
    public CraftingCard convert() throws IllegalRawConversionException {
        if(flag == null)
            throw new IllegalRawConversionException("Missing mandatory field \"flag\" in crafting card (id: " + id + ")");

        if(cost == null)
            throw new IllegalRawConversionException("Missing mandatory field \"cost\" in crafting card (id: " + id + ")");

        if(crafting == null)
            throw new IllegalRawConversionException("Missing mandatory field \"crafting\" in crafting card (id: " + id + ")");

        if(level == 0)
            throw new IllegalRawConversionException("Missing mandatory field \"level\" in crafting card (id: " + id + ")");

        if(points == 0)
            Console.log("Points for card " + id + " are set to 0 or absent. Is it intentional?",
                    Console.Severity.WARNING, Console.Format.YELLOW);

        LevelFlag levelFlag;
        Crafting newCrafting;
        UpgradableCrafting upgradableCrafting;
        Map<ResourceSingle, Integer> newCost = new HashMap<>(cost.size());

        try {
            levelFlag = new LevelFlag(flag, level);
        } catch (IllegalArgumentException e) {
            throw new IllegalRawConversionException(e.getMessage() + " (id: " + id + ")");
        }

        for(String i : cost.keySet())
            try {
                newCost.put(ResourceTypeSingleton.getInstance().getResourceSingleByName(i), cost.get(i));
            } catch (NoSuchElementException e) {
                throw new IllegalRawConversionException("\"" + i + "\" is not an available resource (id: " + id + ")");
            }

        newCrafting = crafting.toCrafting(id);

        try {
            upgradableCrafting = new UpgradableCrafting(newCrafting.getInput(), newCrafting.getOutput(), newCrafting.getFaithOutput(), level);
        } catch (IllegalArgumentException e) {
            throw new IllegalRawConversionException(e.getMessage() + " (id: " + id + ")");
        }

        try {
            return new CraftingCard(id, levelFlag, newCost, upgradableCrafting, points);
        } catch (IllegalArgumentException e) {
            throw new IllegalRawConversionException(e.getMessage() + " (id: " + id + ")");
        }
    }
}
