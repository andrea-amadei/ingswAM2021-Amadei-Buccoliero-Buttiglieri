package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.common.InfoPayload;
import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.PayloadComponent;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.exceptions.IllegalResourceTransferException;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Shop;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.production.CraftingCard;
import it.polimi.ingsw.model.production.Production;
import it.polimi.ingsw.model.storage.LimitedStorage;
import it.polimi.ingsw.model.storage.ResourceContainer;
import it.polimi.ingsw.model.storage.Storage;
import it.polimi.ingsw.server.Console;

import java.util.*;
import java.util.stream.Collectors;

public class BuyFromShopAction implements Action{

    private final String player;

    /**
     * Creates a new BuyFromShopAction with specifying player
     * @param player the username of the current player
     * @throws NullPointerException if player is null
     */
    public BuyFromShopAction(String player){
        if(player == null)
            throw new NullPointerException();
        this.player = player;
    }
    /**
     * It buys the selected card from the shop and puts the corresponding crafting in the selected slot of the production.
     * Matching level of card and crafting is already guaranteed by the previous actions.
     * Resources are removed from the player storage, the upgradable crafting is put on the correct slot
     * and the selections of the storage and of the production are reset.
     * Flags and victory points are added to the player.
     * In order to be executed, the selected resources must match the cost of the card
     * If the action is illegal, it will not reset the selections (we may want to let the user deselect some resources)
     *
     * @param gameContext the current context of the game
     * @return a list of messages that contains information about the change applied to the model
     * @throws IllegalActionException if the action cannot be performed on the model
     * @throws NullPointerException   if gameContext is null
     */
    @Override
    public List<Message> execute(GameContext gameContext) throws IllegalActionException {
        if(gameContext == null)
            throw new NullPointerException();

        GameModel model = gameContext.getGameModel();
        Shop shop = model.getShop();

        Player currentPlayer;

        try{
            currentPlayer = model.getPlayerById(player);
        }catch(NoSuchElementException e){
            throw new IllegalActionException(e.getMessage());
        }

        Storage storage = currentPlayer.getBoard().getStorage();
        Production production = currentPlayer.getBoard().getProduction();

        Map<ResourceContainer, Map<ResourceSingle, Integer>> selectedResources = storage.getSelection();
        CraftingCard card = shop.getSelectedCard();
        Integer selectedCrafting = production.getSelectedCraftingIndex();

        if(selectedResources == null || card == null || selectedCrafting == null)
            throw new IllegalActionException("Trying to buy a card without having selected all the necessary");

        //creating a limited storage to check if the cost of the card is correct
        LimitedStorage checkStorage = new LimitedStorage(card.getCost(), new HashMap<>());

        //try to get all selected resources and see if the cost is correct
        Map<ResourceSingle, Integer> flatSelectedResources =
                selectedResources.entrySet()
                                 .stream()
                                 .flatMap(x -> x.getValue().entrySet().stream())
                                 .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                            Integer::sum));

        try{
            for(ResourceSingle res : flatSelectedResources.keySet())
                checkStorage.addResources(res, flatSelectedResources.get(res));
        } catch (IllegalResourceTransferException e) {
            throw new IllegalActionException("Wrong selection");
        }

        if(!checkStorage.isFull())
            throw new IllegalActionException("Too few resources to buy the card");


        //transaction can be done

        //remove resources from the storage
        for(ResourceContainer container : selectedResources.keySet()){
            for(ResourceSingle res : selectedResources.get(container).keySet()){
                try {
                    container.removeResources(res, selectedResources.get(container).get(res));
                } catch (IllegalResourceTransferException e) {
                    Console.log("Logic failed in BuyFromShopAction", Console.Severity.ERROR);
                    throw new IllegalActionException(e.getMessage());
                }
            }
        }

        //add crafting to the production
        production.setUpgradableCrafting(selectedCrafting, card.getCrafting());

        //removes the card from the shop
        shop.removeCard(card.getCrafting().getLevel(), card.getFlag().getColor());

        //add victory points to the player
        currentPlayer.addPoints(card.getPoints());

        //add the flag of the player to the flag holder
        currentPlayer.getBoard().getFlagHolder().addFlag(card.getFlag());

        //reset all selections
        shop.resetSelectedCard();
        production.resetUpgradableCraftingSelection();
        storage.resetSelection();

        //write the messages
        PayloadComponent updates = new InfoPayload(player + " has bought a card from market");
        return Collections.singletonList(new Message(Collections.singletonList(player), Collections.singletonList(updates)));
    }
}
