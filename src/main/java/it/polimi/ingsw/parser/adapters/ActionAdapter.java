package it.polimi.ingsw.parser.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import it.polimi.ingsw.model.actions.Action;
import it.polimi.ingsw.model.actions.ActivateLeaderAction;

import java.lang.reflect.Type;

public class ActionAdapter implements JsonDeserializer<Action> {
    @Override
    public Action deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String serializedType = jsonElement.getAsJsonObject().get("type").getAsString();
        jsonElement.getAsJsonObject().remove("type");

        switch(serializedType){
            case "activate_leader":
                return jsonDeserializationContext.deserialize(jsonElement, ActivateLeaderAction.class);
            default:
                throw new JsonParseException("Unknown type \"" + serializedType + "\"");
        }
    }
}
