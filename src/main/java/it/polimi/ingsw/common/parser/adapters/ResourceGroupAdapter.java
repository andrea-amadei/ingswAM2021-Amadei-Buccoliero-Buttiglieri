package it.polimi.ingsw.common.parser.adapters;

import com.google.gson.*;
import it.polimi.ingsw.server.model.basetypes.ResourceGroup;
import it.polimi.ingsw.server.model.basetypes.ResourceTypeSingleton;

import java.lang.reflect.Type;

public class ResourceGroupAdapter implements JsonSerializer<ResourceGroup>, JsonDeserializer<ResourceGroup> {
    @Override
    public JsonElement serialize(ResourceGroup resourceGroup, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(resourceGroup.getId());
    }

    @Override
    public ResourceGroup deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return ResourceTypeSingleton.getInstance().getResourceGroupByName(jsonElement.getAsString());
    }
}
