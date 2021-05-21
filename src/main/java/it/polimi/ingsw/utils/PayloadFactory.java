package it.polimi.ingsw.utils;

import it.polimi.ingsw.common.payload_components.groups.PossibleActionsPayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.PossibleActions;
import it.polimi.ingsw.common.payload_components.groups.UpdatePayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.updates.*;
import it.polimi.ingsw.model.holder.FaithHolder;
import it.polimi.ingsw.model.market.ConversionActuator;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.production.Production;
import it.polimi.ingsw.parser.raw.RawCrafting;
import it.polimi.ingsw.parser.raw.RawLevelFlag;
import it.polimi.ingsw.parser.raw.RawMarket;
import it.polimi.ingsw.parser.raw.RawStorage;

import java.util.List;
import java.util.Set;

public class PayloadFactory {
    public static UpdatePayloadComponent addLeaderCard(String player, Integer id){
        return new AddLeaderCardUpdatePayloadComponent(player, id);
    }

    public static UpdatePayloadComponent discardLeaderCard(String player, Integer id){
        return new DiscardLeaderCardUpdatePayloadComponent(player, id);
    }

    public static UpdatePayloadComponent changeCoveredLeaderCard(String player, Integer delta){
        return new ChangeCoveredLeaderCardUpdatePayloadComponent(player, delta);
    }

    public static UpdatePayloadComponent changeResources(String player, RawStorage deltaResources){
        return new ChangeResourcesUpdatePayloadComponent(player, deltaResources);
    }

    //TODO: use this instead of the addCrafting when a card is bought from the shop
    public static UpdatePayloadComponent addUpgradableCrafting(String player, Integer id, Integer index){
        return new AddUpgradableCraftingUpdatePayloadComponent(player, id, index);
    }

    public static UpdatePayloadComponent addCrafting(String player, RawCrafting crafting, Production.CraftingType craftingType, Integer index){
        return new AddCraftingUpdatePayloadComponent(player, crafting, craftingType, index);
    }

    public static UpdatePayloadComponent addFaith(String player, Integer amount){
        return new AddFaithUpdatePayloadComponent(player, amount);
    }

    public static UpdatePayloadComponent changePopeCard(String player, FaithHolder.CheckpointStatus status, Integer index){
        return new ChangePopeCardUpdatePayloadComponent(player, status, index);
    }

    public static UpdatePayloadComponent addPoints(String player, Integer amount){
        return new AddPointsUpdatePayloadComponent(player, amount);
    }

    public static UpdatePayloadComponent addFlag(String player, RawLevelFlag flag){
        return new AddFlagUpdatePayloadComponent(player, flag);
    }

    public static UpdatePayloadComponent addDiscount(String player, String resource, Integer discount){
        return new AddDiscountUpdatePayloadComponent(player, resource, discount);
    }

    public static UpdatePayloadComponent addBoughtCard(String player, Integer amount){
        return new AddBoughtCardUpdatePayloadComponent(player, amount);
    }

    public static UpdatePayloadComponent selectedResource(String player, String containerId, String resource, Integer amount){
        return new SelectedResourceUpdatePayloadComponent(player, containerId, resource, amount);
    }

    public static UpdatePayloadComponent selectedCrafting(String player, Production.CraftingType craftingType, Integer index){
        return new SelectedCraftingUpdatePayloadComponent(player, craftingType, index);
    }

    public static UpdatePayloadComponent selectedShopCard(String player, Integer x, Integer y){
        return new SelectedShopCardUpdatePayloadComponent(player, x, y);
    }

    public static UpdatePayloadComponent unselect(String player, String section){
        return new UnselectUpdatePayloadComponent(player, section);
    }

    public static UpdatePayloadComponent addShelf(String player, String id, String resourceType, Integer size){
        return new AddShelfUpdatePayloadComponent(player, id, resourceType, size);
    }

    public static UpdatePayloadComponent changeShop(Integer x, Integer y, Integer id){
        return new ChangeShopUpdatePayloadComponent(x, y, id);
    }

    public static UpdatePayloadComponent changeMarket(RawMarket market){
        return new ChangeMarketUpdatePayloadComponent(market);
    }

    public static PossibleActionsPayloadComponent possibleActions(Set<PossibleActions> possibleActions){
        return new PossibleActionsPayloadComponent(possibleActions);
    }

    public static UpdatePayloadComponent changePossibleConversions(String player, List<Marble> selectedMarbles, List<List<ConversionActuator>> possibleConversions){
        return new ChangePossibleConversionsUpdatePayloadComponent(player, selectedMarbles, possibleConversions);
    }

    public static UpdatePayloadComponent changeCurrentPlayer(String newPlayer){
        return new ChangeCurrentPlayerPayloadComponent(newPlayer);
    }

}
