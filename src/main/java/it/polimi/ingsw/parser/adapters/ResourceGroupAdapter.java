package it.polimi.ingsw.parser.adapters;

import com.google.gson.*;
import it.polimi.ingsw.gamematerials.ResourceGroup;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;

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
