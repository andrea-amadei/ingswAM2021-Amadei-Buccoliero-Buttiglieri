package it.polimi.ingsw.parser.adapters;

import com.google.gson.*;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;

import java.lang.reflect.Type;

public class ResourceSingleAdapter implements JsonSerializer<ResourceSingle>, JsonDeserializer<ResourceSingle> {
    @Override
    public JsonElement serialize(ResourceSingle resourceSingle, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(resourceSingle.getId());
    }

    @Override
    public ResourceSingle deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return ResourceTypeSingleton.getInstance().getResourceSingleByName(jsonElement.getAsString());
    }
}
