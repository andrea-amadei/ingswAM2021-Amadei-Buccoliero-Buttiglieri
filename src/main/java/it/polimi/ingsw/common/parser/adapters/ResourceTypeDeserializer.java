package it.polimi.ingsw.common.parser.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import it.polimi.ingsw.server.model.basetypes.ResourceType;
import it.polimi.ingsw.server.model.basetypes.ResourceTypeSingleton;

import java.lang.reflect.Type;

public class ResourceTypeDeserializer implements JsonDeserializer<ResourceType> {
    @Override
    public ResourceType deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return ResourceTypeSingleton.getInstance().getResourceTypeByName(jsonElement.getAsString());
    }
}
