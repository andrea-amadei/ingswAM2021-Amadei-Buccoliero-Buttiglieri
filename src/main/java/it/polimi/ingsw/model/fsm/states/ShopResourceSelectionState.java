package it.polimi.ingsw.model.fsm.states;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.PossibleActions;
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
import it.polimi.ingsw.parser.raw.RawStorage;
import it.polimi.ingsw.server.Logger;
import it.polimi.ingsw.utils.PayloadFactory;

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

        try{
            messages = new ArrayList<>(backAction.execute(getGameContext()));
        }catch(IllegalActionException e){
            throw new FSMTransitionFailedException(e.getMessage());
        }

        getGameContext().getGameModel().getShop().resetSelectedCard();
        getGameContext().getCurrentPlayer().getBoard().getStorage().resetSelection();

        List<PayloadComponent> payload = new ArrayList<>();

        payload.add(PayloadFactory.unselect(getGameContext().getCurrentPlayer().getUsername(), "shop"));
        payload.add(PayloadFactory.unselect(getGameContext().getCurrentPlayer().getUsername(), "production"));
        payload.add(PayloadFactory.unselect(getGameContext().getCurrentPlayer().getUsername(), "storage"));

        messages.add(new Message(getGameContext().getGameModel().getPlayerNames(), payload));


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
        List<PayloadComponent> payload = new ArrayList<>();

        try{
            messages = new ArrayList<>(confirmAction.execute(getGameContext()));
        }catch (IllegalActionException e){
            throw new FSMTransitionFailedException(e.getMessage());
        }

        //get all necessary model components
        GameModel model = getGameContext().getGameModel();
        Shop shop = model.getShop();

        Player currentPlayer = getGameContext().getCurrentPlayer();
        Storage storage = currentPlayer.getBoard().getStorage();
        Production production = currentPlayer.getBoard().getProduction();

        //get the selected resources
        Map<ResourceContainer, Map<ResourceSingle, Integer>> selectedResources = Optional.ofNullable(storage.getSelection()).orElse(new HashMap<>());

        //get the selected card
        CraftingCard card = shop.getSelectedCard();

        //get the selected sloth
        Integer selectedCrafting;
        try {
            selectedCrafting = production.getSelectedCraftingIndex();
        }catch(NoSuchElementException e){
            throw new FSMTransitionFailedException(e.getMessage());
        }

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
        LimitedStorage checkStorage = new LimitedStorage(discountedCost, new HashMap<>(), "check");

        //try to get all selected resources and see if the cost is correct
        Map<ResourceSingle, Integer> flatSelectedResources =
                selectedResources.entrySet()
                        .stream()
                        .flatMap(x -> x.getValue().entrySet().stream())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                Integer::sum));

        //check correctness
        boolean isSelectionCorrect = true;
        String errorMessage = "";

        try{
            for(ResourceSingle res : flatSelectedResources.keySet())
                checkStorage.addResources(res, flatSelectedResources.get(res));
        } catch (IllegalResourceTransferException e) {
            isSelectionCorrect = false;
            errorMessage = "Wrong selection";
        }

        if(!checkStorage.isFull()) {
            isSelectionCorrect = false;
            errorMessage = "Not enough resources selected";
        }

        //if resource selection is wrong, an error message is sent to the current player and the update to unselect
        //the resources of the current player is sent to everyone
        if(!isSelectionCorrect){
            payload.add(PayloadFactory.unselect(currentPlayer.getUsername(), "storage"));
            messages.add(new Message(getGameContext().getGameModel().getPlayerNames(), payload));
            throw new FSMTransitionFailedException(messages, errorMessage);
        }


        //transaction can be done

        //remove resources from the storage
        for(ResourceContainer container : selectedResources.keySet()){
            Map<String, Integer> resourcesToRemoveFromContainer = new HashMap<>();
            for(ResourceSingle res : selectedResources.get(container).keySet()){
                try {
                    resourcesToRemoveFromContainer.put(res.getId().toLowerCase(), -selectedResources.get(container).get(res));
                    container.removeResources(res, selectedResources.get(container).get(res));
                } catch (IllegalResourceTransferException e) {
                    Logger.log("Logic failed in ShopResourceSelectionState", Logger.Severity.ERROR);
                    throw new FSMTransitionFailedException(e.getMessage());
                }
            }

            payload.add(PayloadFactory.changeResources(currentPlayer.getUsername(),
                    new RawStorage(container.getId(), resourcesToRemoveFromContainer)
                    ));
        }

        //add crafting to the production
        production.setUpgradableCrafting(selectedCrafting, card.getCrafting());
        payload.add(PayloadFactory.addCrafting(
                currentPlayer.getUsername(),
                card.getCrafting().toRaw(),
                Production.CraftingType.UPGRADABLE,
                selectedCrafting
        ));

        //removes the card from the shop and make the payload
        shop.removeCard(card.getCrafting().getLevel(), card.getFlag().getColor());
        int x = card.getFlag().getColor().ordinal();
        int y = card.getFlag().getLevel() - 1;
        int cardId;
        try{
            cardId = shop.getTopCard(y, x).getId();
        }catch(NoSuchElementException e){
            cardId = -1;
        }
        payload.add(PayloadFactory.changeShop(x, y, cardId));

        //add victory points to the player
        currentPlayer.addPoints(card.getPoints());
        payload.add(PayloadFactory.addPoints(currentPlayer.getUsername(), card.getPoints()));

        //add the flag of the card to the flag holder
        currentPlayer.getBoard().getFlagHolder().addFlag(card.getFlag());
        payload.add(PayloadFactory.addFlag(currentPlayer.getUsername(), card.getFlag().toRaw()));

        //reset all selections
        shop.resetSelectedCard();
        payload.add(PayloadFactory.unselect(currentPlayer.getUsername(), "shop"));

        production.resetCraftingSelection();
        payload.add(PayloadFactory.unselect(currentPlayer.getUsername(), "production"));

        storage.resetSelection();
        payload.add(PayloadFactory.unselect(currentPlayer.getUsername(), "storage"));


        getGameContext().setPlayerMoved(true);

        setNextState(new MenuState(getGameContext()));

        messages.add(new Message(getGameContext().getGameModel().getPlayerNames(), payload));
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
                Collections.singletonList(PayloadFactory.possibleActions(
                        new HashSet<>(){{
                            add(PossibleActions.BACK);
                            add(PossibleActions.SELECT_RESOURCES);
                            add(PossibleActions.CONFIRM);
                        }}
                ))));


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
