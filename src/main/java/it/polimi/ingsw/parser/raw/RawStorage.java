package it.polimi.ingsw.parser.raw;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.exceptions.IllegalRawConversionException;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceType;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.storage.BaseStorage;
import it.polimi.ingsw.model.storage.ResourceContainer;
import it.polimi.ingsw.model.storage.Shelf;
import it.polimi.ingsw.parser.JSONSerializer;
import it.polimi.ingsw.parser.RawObject;
import it.polimi.ingsw.parser.UniqueRawObject;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class RawStorage implements UniqueRawObject<ResourceContainer> {

    @SerializedName("id")
    private String id;

    @SerializedName("resources")
    private Map<String, Integer> resources;

    public RawStorage() { }

    public RawStorage(ResourceContainer resourceContainer) {
        if(resourceContainer == null)
            throw new NullPointerException();

        resources = resourceContainer.getAllResources()
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(e -> e.getKey().getId().toUpperCase(), Map.Entry::getValue));

        this.id = resourceContainer.getId();
    }

    public RawStorage(String id, Map<String, Integer> resources) {
        this.resources = resources;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Map<String, Integer> getResources() {
        return resources;
    }

    @Override
    public String getStringId() {
        return id;
    }

    @Override
    public BaseStorage toObject() throws IllegalRawConversionException {
        if(id == null)
            throw new IllegalRawConversionException("Missing mandatory field \"id\" in storage");

        if(resources == null)
            throw new IllegalRawConversionException("Missing mandatory field \"resources\" in storage");

        Map<ResourceSingle, Integer> res = new HashMap<>(resources.size());

        for(String i : resources.keySet())
            try {
                res.put(ResourceTypeSingleton.getInstance().getResourceSingleByName(i), resources.get(i));
            } catch (NoSuchElementException e) {
                throw new IllegalRawConversionException("\"" + i + "\" is not an available resource");
            }

        try {
            return new BaseStorage(res, id);
        } catch (IllegalArgumentException e) {
            throw new IllegalRawConversionException(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return JSONSerializer.toJson(this);
    }
}
