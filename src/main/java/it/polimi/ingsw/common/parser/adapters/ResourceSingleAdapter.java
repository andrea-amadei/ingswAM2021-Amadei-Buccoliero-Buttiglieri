package it.polimi.ingsw.common.parser.adapters;

import com.google.gson.*;
import it.polimi.ingsw.server.model.basetypes.ResourceSingle;
import it.polimi.ingsw.server.model.basetypes.ResourceTypeSingleton;

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
