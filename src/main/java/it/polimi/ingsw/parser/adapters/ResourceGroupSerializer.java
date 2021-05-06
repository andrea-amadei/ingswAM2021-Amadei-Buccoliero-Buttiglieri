package it.polimi.ingsw.parser.adapters;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import it.polimi.ingsw.gamematerials.ResourceGroup;

import java.lang.reflect.Type;

public class ResourceGroupSerializer implements JsonSerializer<ResourceGroup> {
    @Override
    public JsonElement serialize(ResourceGroup resourceGroup, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(resourceGroup.getId());
    }
}
