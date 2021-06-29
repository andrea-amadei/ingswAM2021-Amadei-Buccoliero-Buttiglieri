package it.polimi.ingsw.common.parser.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import it.polimi.ingsw.server.clienthandling.setupactions.*;

import java.lang.reflect.Type;

public class SetupActionAdapter implements JsonDeserializer<SetupAction> {
    @Override
    public SetupAction deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String serializedType = jsonElement.getAsJsonObject().get("type").getAsString();
        jsonElement.getAsJsonObject().remove("type");

        switch(serializedType){
            case "set_username":
                return jsonDeserializationContext.deserialize(jsonElement, SetUsernameSetupAction.class);
            case "create_match":
                return jsonDeserializationContext.deserialize(jsonElement, CreateMatchSetupAction.class);
            case "join_match":
                return jsonDeserializationContext.deserialize(jsonElement, JoinMatchSetupAction.class);
            case "reconnect":
                return jsonDeserializationContext.deserialize(jsonElement, ReconnectSetupAction.class);
            default:
                throw new JsonParseException("Unknown type \"" + serializedType + "\"");
        }
    }
}
