package it.polimi.ingsw.parser.adapters;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import it.polimi.ingsw.gamematerials.ResourceSingle;

import java.lang.reflect.Type;

public class ResourceSingleSerializer implements JsonSerializer<ResourceSingle> {
    @Override
    public JsonElement serialize(ResourceSingle resourceSingle, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(resourceSingle.getId());
    }
}
