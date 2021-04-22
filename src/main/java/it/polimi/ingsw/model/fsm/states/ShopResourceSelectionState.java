package it.polimi.ingsw.model.fsm.states;

import it.polimi.ingsw.common.InfoPayload;
import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.PayloadComponent;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.exceptions.IllegalResourceTransferException;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Shop;
import it.polimi.ingsw.model.actions.BackAction;
import it.polimi.ingsw.model.actions.ConfirmAction;
import it.polimi.ingsw.model.actions.SelectResourcesAction;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.fsm.State;
import it.polimi.ingsw.model.production.CraftingCard;
import it.polimi.ingsw.model.production.Production;
import it.polimi.ingsw.model.storage.LimitedStorage;
import it.polimi.ingsw.model.storage.ResourceContainer;
import it.polimi.ingsw.model.storage.Storage;
import it.polimi.ingsw.server.Console;

import java.util.*;
import java.util.stream.Collectors;

public class ShopResourceSelectionState extends State {
    /**
     * A new State is created with the provided game context. The nextState and the interruptListener
     * are both initially null
     *
     * @param gameContext the context of the game
     * @throws NullPointerException if gameContext is null
     */
    public ShopResourceSelectionState(GameContext gameContext) {
        super(gameContext);
    }

    /**
     * The player returns to the shop state.
     * Moved property is not yet set to true. Any previous election of a card or resources is undone.
     * Next state is ShopState.
     * @param backAction the action to execute
     * @return the list of messages to send to the clients
     * @throws NullPointerException iff the pointer to backAction is null
     * @throws FSMTransitionFailedException iff the action cannot be executed
     */
    @Override
    public List<Message> handleAction(BackAction backAction)throws FSMTransitionFailedException {
        if (backAction == null)
            throw new NullPointerException();
        List<Message> messages;

        getGameContext().getGameModel().getShop().resetSelectedCard();
        getGameContext().getCurrentPlayer().getBoard().getStorage().resetSelection();

        try{
            messages = backAction.execute(getGameContext());
        }catch(IllegalActionException e){
            throw new FSMTransitionFailedException(e.getMessage());
        }
        setNextState(new ShopState(getGameContext()));
        return messages;
    }

    /**
     * The player selects the resources they want to use to buy the selected card.
     * Moved property is not yet set to true. Next state is ShopResourceSelectionState.
     * @param selectResourcesAction the action to execute
     * @return the list of messages to send to the clients
     * @throws NullPointerException iff the pointer to selectResourcesAction is null
     * @throws FSMTransitionFailedException iff the action cannot be executed
     */
    @Override
    public List<Message> handleAction(SelectResourcesAction selectResourcesAction) throws FSMTransitionFailedException{
        if(selectResourcesAction == null)
            throw new NullPointerException();
        List<Message> messages;

        try{
            messages = selectResourcesAction.execute(getGameContext());
        }catch (IllegalActionException e){
            throw new FSMTransitionFailedException(e.getMessage());
        }
        setNextState(this);
        return messages;
    }

    /**
     * The player uses the previously selected resources to buy the card.
     * Moved property is now set to true. Next state is MenuState.
     * @param confirmAction the action to execute
     * @return the list of messages to send to the clients
     * @throws NullPointerException iff the pointer to confirmAction is null
     * @throws FSMTransitionFailedException iff the action cannot be executed
     */
    @Override
    public List<Message> handleAction(ConfirmAction confirmAction) throws  FSMTransitionFailedException{
        if(confirmAction == null)
            throw new NullPointerException();
        List<Message> messages;

        try{
            messages = confirmAction.execute(getGameContext());
        }catch (IllegalActionException e){
            throw new FSMTransitionFailedException(e.getMessage());
        }

        GameModel model = getGameContext().getGameModel();
        Shop shop = model.getShop();

        Player currentPlayer = getGameContext().getCurrentPlayer();
        Storage storage = currentPlayer.getBoard().getStorage();
        Production production = currentPlayer.getBoard().getProduction();

        Map<ResourceContainer, Map<ResourceSingle, Integer>> selectedResources = Optional.ofNullable(storage.getSelection()).orElse(new HashMap<>());
        CraftingCard card = shop.getSelectedCard();
        Integer selectedCrafting;
        try {
            selectedCrafting = production.getSelectedCraftingIndex();
        }catch(NoSuchElementException e){
            throw new FSMTransitionFailedException(e.getMessage());
        }
        if(card == null || selectedCrafting == null)
            throw new FSMTransitionFailedException("Trying to buy a card without having selected all the necessary");

        //calculate discounts if present and remove non required resources
        Map<ResourceSingle, Integer> discountedCost =
                card.getCost().entrySet().stream()
                                         .peek(entry -> {
                                             if(currentPlayer.getBoard().getDiscountHolder().getDiscounts().containsKey(entry.getKey()))
                                                 entry.setValue(entry.getValue() - currentPlayer.getBoard().getDiscountHolder().getDiscounts().get(entry.getKey()));
                                         })
                                         .filter(entry -> entry.getValue() > 0)
                                         .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        //creating a limited storage to check if the cost of the card is correct
        LimitedStorage checkStorage = new LimitedStorage(discountedCost, new HashMap<>());

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
            throw new FSMTransitionFailedException("Wrong selection");
        }

        if(!checkStorage.isFull())
            throw new FSMTransitionFailedException("Too few resources to buy the card");


        //transaction can be done

        //remove resources from the storage
        for(ResourceContainer container : selectedResources.keySet()){
            for(ResourceSingle res : selectedResources.get(container).keySet()){
                try {
                    container.removeResources(res, selectedResources.get(container).get(res));
                } catch (IllegalResourceTransferException e) {
                    Console.log("Logic failed in ShopResourceSelectionState", Console.Severity.ERROR);
                    throw new FSMTransitionFailedException(e.getMessage());
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
        production.resetCraftingSelection();
        storage.resetSelection();

        //write the messages
        PayloadComponent updates = new InfoPayload(currentPlayer.getUsername() + " has bought a card from market. Resources, crafting, points, flags changed "
                                                    +"selections are reset");

        getGameContext().setPlayerMoved(true);
        getGameContext().getGameModel().getShop().resetSelectedCard();
        getGameContext().getCurrentPlayer().getBoard().getStorage().resetSelection();

        setNextState(new MenuState(getGameContext()));
        return messages;
    }

    /**
     * This method will be executed every time this state is entered.
     * Informs the current player of the next available actions.
     * @return the list of messages to send to the client
     */
    @Override
    public List<Message> onEntry() {
        List<Message> messages = super.onEntry();
        messages.add(new Message(Collections.singletonList(getGameContext().getCurrentPlayer().getUsername()),
                Collections.singletonList(new InfoPayload("Possible Actions: Back, SelectResources, BuyFromShop"))));

        return messages;
    }

    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "ShopResourceSelectionState";
    }
}
