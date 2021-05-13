package it.polimi.ingsw.parser.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import it.polimi.ingsw.server.clienthandling.setupactions.SetUsernameSetupAction;
import it.polimi.ingsw.server.clienthandling.setupactions.SetupAction;

import java.lang.reflect.Type;

public class SetupActionAdapter implements JsonDeserializer<SetupAction> {
    @Override
    public SetupAction deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String serializedType = jsonElement.getAsJsonObject().get("type").getAsString();
        jsonElement.getAsJsonObject().remove("type");

        switch(serializedType){
            case "set_username":
                return jsonDeserializationContext.deserialize(jsonElement, SetUsernameSetupAction.class);
            default:
                throw new JsonParseException("Unknown type \"" + serializedType + "\"");
        }
    }
}
