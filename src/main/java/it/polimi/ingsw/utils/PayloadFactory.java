package it.polimi.ingsw.utils;

import it.polimi.ingsw.common.GlobalUpdatePayload;
import it.polimi.ingsw.common.UpdatePayload;
import it.polimi.ingsw.model.holder.FaithHolder;
import it.polimi.ingsw.model.production.Production;
import it.polimi.ingsw.parser.raw.RawCrafting;
import it.polimi.ingsw.parser.raw.RawLevelFlag;
import it.polimi.ingsw.parser.raw.RawMarket;
import it.polimi.ingsw.parser.raw.RawStorage;

import java.util.HashMap;

public class PayloadFactory {
    public static UpdatePayload createAddLeaderCardPayload(String player, Integer id){
        return new UpdatePayload("add_leader_card", player, new HashMap<>(){{
            put("id", id);
        }});
    }

    public static UpdatePayload discardLeaderCardPayload(String player, Integer id){
        return new UpdatePayload("discard_leader_card", player, new HashMap<>(){{
            put("id", id);
        }});
    }

    public static UpdatePayload changeCoveredLeaderCard(String player, Integer n){
        return new UpdatePayload("change_covered_leader_card", player, new HashMap<>(){{
            put("n", n);
        }});
    }

    public static UpdatePayload changeResources(String player, RawStorage deltaResources){
        return new UpdatePayload("change_resources", player, new HashMap<>(){{
            put("delta_resources", deltaResources);
        }});
    }

    public static UpdatePayload addUpgradableCrafting(String player, Integer id, Integer index){
        return new UpdatePayload("add_upgradable_crafting", player, new HashMap<>(){{
            put("id", id);
            put("index", index);
        }});
    }

    public static UpdatePayload addCrafting(String player, RawCrafting crafting, Production.CraftingType craftingType, Integer index){
        return new UpdatePayload("add_crafting", player, new HashMap<>(){{
            put("crafting", crafting);
            put("crafting_type", craftingType);
            put("index", index);
        }});
    }

    public static UpdatePayload addFaith(String player, Integer amount){
        return new UpdatePayload("add_faith", player, new HashMap<>(){{
            put("amount", amount);
        }});
    }

    public static UpdatePayload changePopeCardPayload(String player, FaithHolder.CheckpointStatus status, Integer index){
        return new UpdatePayload("change_pope_card", player, new HashMap<>(){{
            put("status", status);
            put("index", index);
        }});
    }

    public static UpdatePayload addPointsPayload(String player, Integer amount){
        return new UpdatePayload("add_points", player, new HashMap<>(){{
            put("amount", amount);
        }});
    }

    public static UpdatePayload addFlagPayload(String player, RawLevelFlag flag){
        return new UpdatePayload("add_flag", player, new HashMap<>(){{
            put("flag", flag);
        }});
    }

    public static UpdatePayload addDiscountPayload(String player, String resource, Integer discount){
        return new UpdatePayload("add_discount", player, new HashMap<>(){{
            put("resource", resource);
            put("discount", discount);
        }});
    }

    public static UpdatePayload addBoughtCardPayload(String player, Integer amount){
        return new UpdatePayload("add_bought_card", player, new HashMap<>(){{
            put("amount", amount);
        }});
    }

    public static UpdatePayload selectedResourcePayload(String player, String containerId, String resource, Integer amount){
        return new UpdatePayload("selected_resource", player, new HashMap<>(){{
            put("container_id", containerId);
            put("resource", resource);
            put("amount", amount);
        }});
    }

    public static UpdatePayload selectedCraftingPayload(String player, Production.CraftingType craftingType, Integer index){
        return new UpdatePayload("selected_crafting", player, new HashMap<>(){{
            put("crafting_type", craftingType);
            put("index", index);
        }});
    }

    public static UpdatePayload selectedShopCardPayload(String player, Integer x, Integer y){
        return new UpdatePayload("selected_shop_card", player, new HashMap<>(){{
            put("x", x);
            put("y", y);
        }});
    }

    public static UpdatePayload unselectPayload(String player, String section){
        return new UpdatePayload("unselect", player, new HashMap<>(){{
            put("section", section);
        }});
    }

    public static GlobalUpdatePayload changeShopPayload(Integer x, Integer y, Integer id){
        return new GlobalUpdatePayload("change_shop", new HashMap<>(){{
            put("x", x);
            put("y", y);
            put("id", id);
        }});
    }

    public static GlobalUpdatePayload changeMarketPayload(RawMarket market){
        return new GlobalUpdatePayload("change_market", new HashMap<>(){{
            put("market", market);
        }});
    }

}
