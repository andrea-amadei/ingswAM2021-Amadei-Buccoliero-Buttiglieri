package it.polimi.ingsw.parser.raw;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.exceptions.IllegalRawConversionException;
import it.polimi.ingsw.gamematerials.*;
import it.polimi.ingsw.model.leader.*;
import it.polimi.ingsw.model.market.ConversionActuator;
import it.polimi.ingsw.model.storage.Shelf;
import it.polimi.ingsw.parser.JSONSerializer;
import it.polimi.ingsw.parser.RawObject;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class RawSpecialAbility implements RawObject<SpecialAbility> {
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
    private Integer amount;

    @SerializedName(value = "faithOutput", alternate = "faith_output")
    private Integer faithOutput;

    public RawSpecialAbility() { }

    public RawSpecialAbility(ConversionAbility conversionAbility) {
        type = "conversion";

        from = conversionAbility.getFrom();
        to = conversionAbility.getTo().getResources().stream().map(ResourceType::getId).collect(Collectors.toList());
        faithOutput = conversionAbility.getTo().getFaith();
    }

    public RawSpecialAbility(CraftingAbility craftingAbility) {
        type = "crafting";

        crafting = new RawCrafting(craftingAbility.getCrafting());
    }

    public RawSpecialAbility(DiscountAbility discountAbility) {
        type = "discount";

        resource = discountAbility.getResource().getId();
        amount = discountAbility.getAmount();
    }

    public RawSpecialAbility(StorageAbility storageAbility) {
        type = "storage";

        acceptedTypes = storageAbility.getShelf().getAcceptedTypes().getId();
        amount = storageAbility.getShelf().getMaxAmount();
    }

    public RawSpecialAbility(SpecialAbility specialAbility) {
        if(specialAbility instanceof ConversionAbility) {
            ConversionAbility conversionAbility = (ConversionAbility) specialAbility;

            type = "conversion";

            from = conversionAbility.getFrom();
            to = conversionAbility.getTo().getResources().stream().map(ResourceType::getId).collect(Collectors.toList());
            faithOutput = conversionAbility.getTo().getFaith();
        }
        else if(specialAbility instanceof CraftingAbility) {
            CraftingAbility craftingAbility = (CraftingAbility) specialAbility;

            type = "crafting";

            crafting = new RawCrafting(craftingAbility.getCrafting());
        }
        else if(specialAbility instanceof DiscountAbility) {
            DiscountAbility discountAbility = (DiscountAbility) specialAbility;

            type = "discount";

            resource = discountAbility.getResource().getId();
            amount = discountAbility.getAmount();
        }
        else if(specialAbility instanceof StorageAbility) {
            StorageAbility storageAbility = (StorageAbility) specialAbility;

            type = "storage";

            acceptedTypes = storageAbility.getShelf().getAcceptedTypes().getId();
            amount = storageAbility.getShelf().getMaxAmount();
        }
        else
            throw new IllegalArgumentException("Unsupported special ability!");
    }

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

    public SpecialAbility toObject() throws IllegalRawConversionException {
        List<ResourceSingle> sList = new ArrayList<>();
        ResourceSingle s;
        ResourceType t;


        if(type == null)
            throw new IllegalRawConversionException("Missing field \"type\" for special ability");

        switch (type) {
            case "conversion":
            case "conversion_ability":
                if(from == null)
                    throw new IllegalRawConversionException("Illegal or absent field \"from\" for a \"" + type + "\" special ability");

                if(to == null)
                    throw new IllegalRawConversionException("Missing field \"to\" for a \"" + type + "\" special ability");

                if(faithOutput == null)
                    throw new IllegalRawConversionException("Missing field \"faith_output\" for a \"" + type + "\" special ability");

                for(String i : to)
                    try {
                        s = ResourceTypeSingleton.getInstance().getResourceSingleByName(i);
                        sList.add(s);
                    } catch (NoSuchElementException e) {
                        throw new IllegalRawConversionException("\"" + i + "\" is not an available resource");
                    }

                try {
                    return new ConversionAbility(from, new ConversionActuator(sList, faithOutput));
                } catch (IllegalArgumentException e) {
                    throw new IllegalRawConversionException(e.getMessage());
                }

            case "discount":
            case "discount_ability":
                if(resource == null)
                    throw new IllegalRawConversionException("Illegal or absent field \"resource\" for a \"" + type + "\" special ability");

                if(amount == null)
                    throw new IllegalRawConversionException("Missing field \"amount\" for a \"" + type + "\" special ability");

                try {
                    s = ResourceTypeSingleton.getInstance().getResourceSingleByName(resource);
                } catch (NoSuchElementException e) {
                    throw new IllegalRawConversionException("\"" + resource + "\" is not an available resource");
                }

                try {
                    return new DiscountAbility(amount, s);
                } catch (IllegalArgumentException e) {
                    throw new IllegalRawConversionException(e.getMessage());
                }

            case "crafting":
            case "crafting_ability":
                if(crafting == null)
                    throw new IllegalRawConversionException("Illegal or absent field \"crafting\" for a \"" + type + "\" special ability");

                // no need to catch this since it will already throw the right exception
                return new CraftingAbility(crafting.toObject());

            case "storage":
            case "storage_ability":
                if(acceptedTypes == null)
                    throw new IllegalRawConversionException("Illegal or absent field \"accepted_types\" for a \"" + type + "\" special ability");

                if(amount == null)
                    throw new IllegalRawConversionException("Missing field \"amount\" for a \"" + type + "\" special ability");

                try {
                    t = ResourceTypeSingleton.getInstance().getResourceTypeByName(acceptedTypes);
                } catch (NoSuchElementException e) {
                    throw new IllegalRawConversionException("\"" + resource + "\" is not an available resource");
                }

                try {
                    //TODO: choose a meaningful name
                    return new StorageAbility(new Shelf("Leader_", t, amount));
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
