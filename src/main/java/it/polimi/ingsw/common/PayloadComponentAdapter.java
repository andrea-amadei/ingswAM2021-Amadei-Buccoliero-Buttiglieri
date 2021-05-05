package it.polimi.ingsw.common;

import com.google.gson.*;
import it.polimi.ingsw.common.payload_components.PayloadComponent;

import java.lang.reflect.Type;

//TODO: REMAKE ADAPTER
public class PayloadComponentAdapter implements JsonDeserializer<PayloadComponent> {
    @Override
    public PayloadComponent deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonElement specificTypeElement = jsonObject.get("type");
        if(specificTypeElement == null)
            throw new JsonParseException("A subclass of " + type.getClass().getSimpleName() + " does not have serialized" +
                    "the attribute type");

        String specificTypeStr = specificTypeElement.getAsString();
        Type specificType;
        switch(specificTypeStr){
            case "test1":
                specificType = Object.class;
                break;
            default:
                throw new JsonParseException("type attribute \"" + specificTypeStr + "\" not valid");

        }

        return jsonDeserializationContext.deserialize(jsonElement, specificType);
    }
}