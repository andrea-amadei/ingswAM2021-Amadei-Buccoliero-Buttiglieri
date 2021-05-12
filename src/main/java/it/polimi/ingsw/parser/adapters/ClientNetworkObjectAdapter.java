package it.polimi.ingsw.parser.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import it.polimi.ingsw.server.clienthandling.ClientNetworkObject;
import it.polimi.ingsw.server.clienthandling.setupactions.SetupAction;

import java.lang.reflect.Type;

public class ClientNetworkObjectAdapter implements JsonDeserializer<ClientNetworkObject> {
    @Override
    public ClientNetworkObject deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String group = jsonElement.getAsJsonObject().get("group").getAsString();
        jsonElement.getAsJsonObject().remove("group");
        switch(group) {
            case "setup" :
                return jsonDeserializationContext.deserialize(jsonElement, SetupAction.class);
            default:
                throw new JsonParseException("Unknown group \"" + group + "\"");
        }
    }
}
