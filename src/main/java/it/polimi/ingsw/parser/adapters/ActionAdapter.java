package it.polimi.ingsw.parser.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import it.polimi.ingsw.model.actions.*;

import java.lang.reflect.Type;

public class ActionAdapter implements JsonDeserializer<Action> {
    @Override
    public Action deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String serializedType = jsonElement.getAsJsonObject().get("type").getAsString();
        jsonElement.getAsJsonObject().remove("type");

        switch(serializedType){
            case "activate_leader":
                return jsonDeserializationContext.deserialize(jsonElement, ActivateLeaderAction.class);
            case "back":
                return jsonDeserializationContext.deserialize(jsonElement, BackAction.class);
            case "buy_from_market":
                return jsonDeserializationContext.deserialize(jsonElement, BuyFromMarketAction.class);
            case "confirm":
                return jsonDeserializationContext.deserialize(jsonElement, ConfirmAction.class);
            case "confirm_tidy":
                return jsonDeserializationContext.deserialize(jsonElement, ConfirmTidyAction.class);
            case "discard_leader":
                return jsonDeserializationContext.deserialize(jsonElement, DiscardLeaderAction.class);
            case "move_from_basket_to_shelf":
                return jsonDeserializationContext.deserialize(jsonElement, MoveFromBasketToShelfAction.class);
            case "next_turn":
                return jsonDeserializationContext.deserialize(jsonElement, NextTurnAction.class);
            case "preliminary_pick":
                return jsonDeserializationContext.deserialize(jsonElement, PreliminaryPickAction.class);
            case "resources_move":
                return jsonDeserializationContext.deserialize(jsonElement, ResourcesMoveAction.class);
            case "select_card_from_shop":
                return jsonDeserializationContext.deserialize(jsonElement, SelectCardFromShopAction.class);
            case "select_conversions":
                return jsonDeserializationContext.deserialize(jsonElement, SelectConversionsAction.class);
            case "select_crafting":
                return jsonDeserializationContext.deserialize(jsonElement, SelectCraftingAction.class);
            case "select_crafting_output":
                return jsonDeserializationContext.deserialize(jsonElement, SelectCraftingOutputAction.class);
            case "select_play":
                return jsonDeserializationContext.deserialize(jsonElement, SelectPlayAction.class);
            case "select_resources":
                return jsonDeserializationContext.deserialize(jsonElement, SelectResourcesAction.class);
            default:
                throw new JsonParseException("Unknown type \"" + serializedType + "\"");
        }
    }
}
