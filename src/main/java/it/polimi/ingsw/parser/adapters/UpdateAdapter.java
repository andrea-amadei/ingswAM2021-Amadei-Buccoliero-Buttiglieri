package it.polimi.ingsw.parser.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import it.polimi.ingsw.client.updates.*;

import java.lang.reflect.Type;

public class UpdateAdapter implements JsonDeserializer<Update> {
    @Override
    public Update deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String serializedType = jsonElement.getAsJsonObject().get("type").getAsString();
        jsonElement.getAsJsonObject().remove("type");

        switch(serializedType){
            case "set_username":
                return jsonDeserializationContext.deserialize(jsonElement, SetUsernameUpdate.class);
            case "set_game_name":
                return jsonDeserializationContext.deserialize(jsonElement, SetGameNameUpdate.class);
            case "info":
            case "text":
                return jsonDeserializationContext.deserialize(jsonElement, AddServerMessageUpdate.class);
            case "add_bought_card":
                return jsonDeserializationContext.deserialize(jsonElement, AddBoughtCardUpdate.class);
            case "add_crafting":
                return jsonDeserializationContext.deserialize(jsonElement, AddCraftingUpdate.class);
            case "add_discount":
                return jsonDeserializationContext.deserialize(jsonElement, AddDiscountUpdate.class);
            case "add_faith":
                return jsonDeserializationContext.deserialize(jsonElement, AddFaithUpdate.class);
            case "add_flag":
                return jsonDeserializationContext.deserialize(jsonElement, AddFlagUpdate.class);
            case "add_leader_card":
                return jsonDeserializationContext.deserialize(jsonElement, AddLeaderCardUpdate.class);
            case "add_points":
                return jsonDeserializationContext.deserialize(jsonElement, AddPointsUpdate.class);
            case "add_shelf":
                return jsonDeserializationContext.deserialize(jsonElement, AddShelfUpdate.class);
            case "add_upgradable_crafting":
                return jsonDeserializationContext.deserialize(jsonElement, AddUpgradableCraftingUpdate.class);
            case "change_covered_leader_card":
                return jsonDeserializationContext.deserialize(jsonElement, ChangeCoveredLeaderCardUpdate.class);
            case "change_crafting_status":
                return jsonDeserializationContext.deserialize(jsonElement, ChangeCraftingStatusUpdate.class);
            case "change_current_player":
                return jsonDeserializationContext.deserialize(jsonElement, ChangeCurrentPlayerUpdate.class);
            case "change_market":
                return jsonDeserializationContext.deserialize(jsonElement, ChangeMarketUpdate.class);
            case "change_pope_card":
                return jsonDeserializationContext.deserialize(jsonElement, ChangePopeCardUpdate.class);
            case "change_possible_conversions":
                return jsonDeserializationContext.deserialize(jsonElement, ChangePossibleConversionsUpdate.class);
            case "change_resources":
                return jsonDeserializationContext.deserialize(jsonElement, ChangeResourcesUpdate.class);
            case "change_shop":
                return jsonDeserializationContext.deserialize(jsonElement, ChangeShopUpdate.class);
            case "discard_leader_card":
                return jsonDeserializationContext.deserialize(jsonElement, DiscardLeaderCardUpdate.class);
            case "possible_actions":
                return jsonDeserializationContext.deserialize(jsonElement, ChangePossibleActionsUpdate.class);
            case "selected_crafting":
                return jsonDeserializationContext.deserialize(jsonElement, SelectedCraftingUpdate.class);
            case "selected_resource":
                return jsonDeserializationContext.deserialize(jsonElement, SelectedResourceUpdate.class);
            case "selected_shop_card":
                return jsonDeserializationContext.deserialize(jsonElement, SelectedShopCardUpdate.class);
            case "set_initial_configuration":
                return jsonDeserializationContext.deserialize(jsonElement, InitialConfigurationUpdate.class);
            case "unselect":
                return jsonDeserializationContext.deserialize(jsonElement, UnselectUpdate.class);
            default:
                throw new JsonParseException("Unknown type \"" + serializedType + "\"");
        }
    }
}
