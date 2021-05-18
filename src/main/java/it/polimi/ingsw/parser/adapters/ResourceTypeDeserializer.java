package it.polimi.ingsw.parser.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import it.polimi.ingsw.gamematerials.ResourceType;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;

import java.lang.reflect.Type;

public class ResourceTypeDeserializer implements JsonDeserializer<ResourceType> {
    @Override
    public ResourceType deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return ResourceTypeSingleton.getInstance().getResourceTypeByName(jsonElement.getAsString());
    }
}
