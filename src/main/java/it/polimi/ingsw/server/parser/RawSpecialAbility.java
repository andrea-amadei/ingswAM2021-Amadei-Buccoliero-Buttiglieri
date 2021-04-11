package it.polimi.ingsw.server.parser;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.exceptions.IllegalRawConversionException;
import it.polimi.ingsw.gamematerials.*;
import it.polimi.ingsw.model.leader.*;
import it.polimi.ingsw.model.market.ConversionActuator;
import it.polimi.ingsw.model.production.Crafting;
import it.polimi.ingsw.model.storage.Shelf;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class RawSpecialAbility {
    @SerializedName("type")
    private String type;

    @SerializedName("from")
    private MarbleColor from;

    @SerializedName(value = "resource")
    private String resource;

    @SerializedName("to")
    private List<String> to;

    @SerializedName(value = "acceptedTypes", alternate = "accepted_types")
    private String acceptedTypes;

    @SerializedName("crafting")
    private RawCrafting crafting;

    @SerializedName("amount")
    private int amount;

    @SerializedName(value = "faithOutput", alternate = "faith_output")
    private int faithOutput;

    public String getType() {
        return type;
    }

    public MarbleColor getFrom() {
        return from;
    }

    public String getResource() {
        return resource;
    }

    public List<String> getTo() {
        return to;
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

    public int getFaithOutput() {
        return faithOutput;
    }

    public SpecialAbility toSpecialAbility(int id) throws IllegalRawConversionException {
        List<ResourceSingle> sList = new ArrayList<>();
        ResourceSingle s;
        ResourceType t;


        if(type == null)
            throw new IllegalRawConversionException("Mandatory field \"type\" is missing (id: " + id + ")");

        switch (type) {
            case "conversion":
            case "conversion_ability":
                if(from == null)
                    throw new IllegalRawConversionException("Illegal or absent field \"from\" for a \"" + type + "\" special ability (id: " + id + ")");

                if(to == null)
                    throw new IllegalRawConversionException("Illegal or absent field \"to\" for a \"" + type + "\" special ability (id: " + id + ")");

                for(String i : to)
                    try {
                        s = ResourceTypeSingleton.getInstance().getResourceSingleByName(i);
                        sList.add(s);
                    } catch (NoSuchElementException e) {
                        throw new IllegalRawConversionException("\"" + i + "\" is not an available resource (id: " + id + ")");
                    }

                try {
                    return new ConversionAbility(from, new ConversionActuator(sList, faithOutput));
                } catch (IllegalArgumentException e) {
                    throw new IllegalRawConversionException(e.getMessage() + " (id: " + id + ")");
                }

            case "discount":
            case "discount_ability":
                if(resource == null)
                    throw new IllegalRawConversionException("Illegal or absent field \"resource\" for a \"" + type + "\" special ability (id: " + id + ")");

                if(amount == 0)
                    throw new IllegalRawConversionException("Illegal or absent field \"amount\" for a \"" + type + "\" special ability (id: " + id + ")");

                try {
                    s = ResourceTypeSingleton.getInstance().getResourceSingleByName(resource);
                } catch (NoSuchElementException e) {
                    throw new IllegalRawConversionException("\"" + resource + "\" is not an available resource (id: " + id + ")");
                }

                try {
                    return new DiscountAbility(amount, s);
                } catch (IllegalArgumentException e) {
                    throw new IllegalRawConversionException(e.getMessage() + " (id: " + id + ")");
                }

            case "crafting":
            case "crafting_ability":
                if(crafting == null)
                    throw new IllegalRawConversionException("Illegal or absent field \"crafting\" for a \"" + type + "\" special ability (id: " + id + ")");

                // no need to catch this since it will already throw  the right exception
                return new CraftingAbility(crafting.toCrafting(id));

            case "storage":
            case "storage_ability":
                if(acceptedTypes == null)
                    throw new IllegalRawConversionException("Illegal or absent field \"accepted_types\" for a \"" + type + "\" special ability (id: " + id + ")");

                if(amount == 0)
                    throw new IllegalRawConversionException("Illegal or absent field \"amount\" for a \"" + type + "\" special ability (id: " + id + ")");

                try {
                    t = ResourceTypeSingleton.getInstance().getResourceTypeByName(acceptedTypes);
                } catch (NoSuchElementException e) {
                    throw new IllegalRawConversionException("\"" + resource + "\" is not an available resource (id: " + id + ")");
                }

                try {
                    //TODO: choose a meaningful name
                    return new StorageAbility(new Shelf("leader_" + id, t, amount));
                } catch (IllegalArgumentException e) {
                    throw new IllegalRawConversionException(e.getMessage() + " (id: " + id + ")");
                }

            default:
                throw new IllegalRawConversionException("Unknown type \"" + type + "\" (id: " + id + ")");
        }
    }
}
