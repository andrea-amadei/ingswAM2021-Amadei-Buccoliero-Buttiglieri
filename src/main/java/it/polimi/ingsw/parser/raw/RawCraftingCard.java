package it.polimi.ingsw.parser.raw;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.exceptions.IllegalRawConversionException;
import it.polimi.ingsw.gamematerials.*;
import it.polimi.ingsw.model.production.Crafting;
import it.polimi.ingsw.model.production.CraftingCard;
import it.polimi.ingsw.model.production.UpgradableCrafting;
import it.polimi.ingsw.server.Console;
import it.polimi.ingsw.parser.UniqueRawObject;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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

    public RawCraftingCard() { }

    public RawCraftingCard(CraftingCard craftingCard) {
        if(craftingCard == null)
            throw new NullPointerException();

        id = craftingCard.getId();
        flag = craftingCard.getFlag().getColor();
        level = craftingCard.getCrafting().getLevel();

        cost = craftingCard.getCost()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(e -> e.getKey().getId().toUpperCase(), Map.Entry::getValue));

        crafting = new RawCrafting(craftingCard.getCrafting());

        points = craftingCard.getPoints();
    }

    public String getId() {
        return String.format("%03d", id);
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
    public CraftingCard toObject() throws IllegalRawConversionException {
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
            newCrafting = crafting.toObject();
            upgradableCrafting = new UpgradableCrafting(newCrafting.getInput(), newCrafting.getOutput(), newCrafting.getFaithOutput(), level);
        } catch (IllegalArgumentException e) {
            throw new IllegalRawConversionException(e.getMessage() + " (id: " + id + ")");
        }

        for(String i : cost.keySet())
            try {
                newCost.put(ResourceTypeSingleton.getInstance().getResourceSingleByName(i), cost.get(i));
            } catch (NoSuchElementException e) {
                throw new IllegalRawConversionException("\"" + i + "\" is not an available resource (id: " + id + ")");
            }

        try {
            return new CraftingCard(id, levelFlag, newCost, upgradableCrafting, points);
        } catch (IllegalArgumentException e) {
            throw new IllegalRawConversionException(e.getMessage() + " (id: " + id + ")");
        }
    }
}
