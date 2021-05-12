package it.polimi.ingsw.clientproto.parser.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import it.polimi.ingsw.clientproto.updates.AddServerMessageUpdate;
import it.polimi.ingsw.clientproto.updates.SetUsernameUpdate;
import it.polimi.ingsw.clientproto.updates.Update;

import java.lang.reflect.Type;

public class UpdateAdapter implements JsonDeserializer<Update> {
    @Override
    public Update deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String serializedType = jsonElement.getAsJsonObject().get("type").getAsString();
        jsonElement.getAsJsonObject().remove("type");

        switch(serializedType){
            case "set_username":
                return jsonDeserializationContext.deserialize(jsonElement, SetUsernameUpdate.class);
            case "text":
                return jsonDeserializationContext.deserialize(jsonElement, AddServerMessageUpdate.class);
            default:
                throw new JsonParseException("Unknown type \"" + serializedType + "\"");
        }
    }
}
