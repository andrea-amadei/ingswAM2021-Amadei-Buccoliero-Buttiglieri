package it.polimi.ingsw.parser.adapters;

import com.google.gson.*;
import it.polimi.ingsw.annotations.SerializedGroup;
import it.polimi.ingsw.annotations.SerializedType;
import it.polimi.ingsw.common.payload_components.PayloadComponent;

import java.lang.reflect.Type;

public class PayloadComponentAdapter implements JsonSerializer<PayloadComponent>, JsonDeserializer<PayloadComponent> {

    @Override
    public JsonElement serialize(PayloadComponent payloadComponent, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject finalObject = new JsonObject();

        finalObject.add("type", new JsonPrimitive(payloadComponent.getClass().getAnnotation(SerializedType.class).value()));
        finalObject.add("group", new JsonPrimitive(payloadComponent.getClass().getAnnotation(SerializedGroup.class).value()));

        JsonObject originalObject = jsonSerializationContext.serialize(payloadComponent, payloadComponent.getClass()).getAsJsonObject();
        for(String key : originalObject.keySet()){
            finalObject.add(key, originalObject.get(key));
        }

        return finalObject;
    }

    @Override
    public PayloadComponent deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return null;
    }

}
