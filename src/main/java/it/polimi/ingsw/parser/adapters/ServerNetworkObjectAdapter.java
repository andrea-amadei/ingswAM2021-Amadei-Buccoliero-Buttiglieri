package it.polimi.ingsw.parser.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import it.polimi.ingsw.client.network.ServerNetworkObject;
import it.polimi.ingsw.client.ping.Ping;
import it.polimi.ingsw.client.updates.Update;

import java.lang.reflect.Type;

public class ServerNetworkObjectAdapter implements JsonDeserializer<ServerNetworkObject> {
    @Override
    public ServerNetworkObject deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String group = jsonElement.getAsJsonObject().get("group").getAsString();
        jsonElement.getAsJsonObject().remove("group");
        switch(group) {
            case "update" :
            case "setup" :
            case "possible_actions":
                return jsonDeserializationContext.deserialize(jsonElement, Update.class);
            case "ping":
                return jsonDeserializationContext.deserialize(jsonElement, Ping.class);
            default:
                throw new JsonParseException("Unknown group \"" + group + "\"");
        }
    }
}
