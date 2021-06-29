package it.polimi.ingsw.server.model.fsm.states;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.InfoPayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.PossibleActions;
import it.polimi.ingsw.common.payload_components.groups.PossibleActionsPayloadComponent;
import it.polimi.ingsw.common.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.common.exceptions.IllegalActionException;
import it.polimi.ingsw.common.exceptions.IllegalResourceTransferException;
import it.polimi.ingsw.server.model.basetypes.ResourceGroup;
import it.polimi.ingsw.server.model.basetypes.ResourceSingle;
import it.polimi.ingsw.server.model.basetypes.ResourceType;
import it.polimi.ingsw.server.model.actions.BackAction;
import it.polimi.ingsw.server.model.actions.ConfirmAction;
import it.polimi.ingsw.server.model.actions.SelectResourcesAction;
import it.polimi.ingsw.server.model.fsm.GameContext;
import it.polimi.ingsw.server.model.fsm.State;
import it.polimi.ingsw.server.model.production.Crafting;
import it.polimi.ingsw.server.model.production.Production;
import it.polimi.ingsw.server.model.storage.LimitedStorage;
import it.polimi.ingsw.server.model.storage.ResourceContainer;
import it.polimi.ingsw.server.model.storage.Storage;
import it.polimi.ingsw.common.parser.raw.RawStorage;
import it.polimi.ingsw.server.Logger;
import it.polimi.ingsw.common.utils.ForegroundColor;
import it.polimi.ingsw.common.utils.PayloadFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Extends State and represents a concrete state of the state machine.
 * In this state the player has already chosen what crafting to activate and needs now to select the wright resources
 * for the crafting input. They can go back to later select a different crafting, select the resources or
 * confirm their selection once they think they are done.
 */
public class CraftingResourceSelectionState extends State {

    /**
     * A new State is created with the provided game context. The nextState and the interruptListener
     * are both initially null
     *
     * @param gameContext the context of the game
     * @throws NullPointerException if gameContext is null
     */
    public CraftingResourceSelectionState(GameContext gameContext) {
        super(gameContext);
    }

    /**
     * The player wants to go back in the crafting state.
     *
     * If the action can be performed, this method returns the list of messages that need to be sent to the client
     * and sets the moved property remains false in the game context. The new state will be MenuState
     * @param backAction the action to be executed
     * @return the list of messages that need to be sent to the clients
     * @throws NullPointerException if backAction is null
     * @throws FSMTransitionFailedException if the action cannot be executed
     */
    @Override
    public List<Message> handleAction(BackAction backAction) throws FSMTransitionFailedException {
        if(backAction == null)
            throw new NullPointerException();

        List<Message> messages;
        try {
            messages = backAction.execute(getGameContext());
        } catch(IllegalActionException e) {
            throw new FSMTransitionFailedException(e.getMessage());
        }

        getGameContext().getCurrentPlayer().getBoard().getProduction().getSelectedCrafting().resetUndecidedOutputs();
        getGameContext().getCurrentPlayer().getBoard().getProduction().resetCraftingSelection();
        getGameContext().getCurrentPlayer().getBoard().getStorage().resetSelection();

        setNextState(new CraftingState(getGameContext()));

        List<PayloadComponent> payload = new ArrayList<>();


        //reset crafting selection
        payload.add(PayloadFactory.unselect(getGameContext().getCurrentPlayer().getUsername(), "production"));
        payload.add(PayloadFactory.unselect(getGameContext().getCurrentPlayer().getUsername(), "storage"));

        messages.add(new Message(getGameContext().getGameModel().getPlayerNames(), payload));

        return messages;
    }

    /**
     * The player wants to add other resources to the selection
     *
     * If the action can be performed, this method returns the list of messages that need to be sent to the client
     * and sets the moved property remains false in the game context. The new state will be MenuState
     * @param selectResourcesAction the action to be executed
     * @return the list of messages that need to be sent to the clients
     * @throws NullPointerException if backAction is null
     * @throws FSMTransitionFailedException if the action cannot be executed
     */
    @Override
    public List<Message> handleAction(SelectResourcesAction selectResourcesAction) throws FSMTransitionFailedException {
        if(selectResourcesAction == null)
            throw new NullPointerException();

        List<Message> messages;
        try {
            messages = selectResourcesAction.execute(getGameContext());
        } catch(IllegalActionException e) {
            throw new FSMTransitionFailedException(e.getMessage());
        }

        setNextState(this);

        return messages;
    }

    /**
     * The player wants to confirm all selected resources and mark the selected crafting as ready
     *
     * If the action can be performed, this method returns the list of messages that need to be sent to the client
     * and sets the moved property remains false in the game context. The new state will be MenuState
     * @param confirmAction the action to be executed
     * @return the list of messages that need to be sent to the clients
     * @throws NullPointerException if backAction is null
     * @throws FSMTransitionFailedException if the action cannot be executed
     */
    @Override
    public List<Message> handleAction(ConfirmAction confirmAction) throws FSMTransitionFailedException {
        if(confirmAction == null)
            throw new NullPointerException();

        List<Message> messages;
        List<PayloadComponent> payload = new ArrayList<>();

        try {
            messages = new ArrayList<>(confirmAction.execute(getGameContext()));
        } catch(IllegalActionException e) {
            throw new FSMTransitionFailedException(e.getMessage());
        }

        Storage storage = getGameContext().getCurrentPlayer().getBoard().getStorage();
        Production production = getGameContext().getCurrentPlayer().getBoard().getProduction();

        //get selected resources and selected crafting
        Map<ResourceContainer, Map<ResourceSingle, Integer>> selectedResources = storage.getSelection();
        Crafting selectedCrafting;
        try {
            selectedCrafting = production.getSelectedCrafting();
        } catch(NoSuchElementException e) {
            throw new FSMTransitionFailedException(e.getMessage());
        }

        //if the player has not selected any resource
        if(selectedResources == null)
            throw new FSMTransitionFailedException("No resources were selected");

        //distinguish between inputs
        Map<ResourceSingle, Integer> single = new HashMap<>();
        Map<ResourceGroup, Integer> group = new HashMap<>();

        for(ResourceType i : selectedCrafting.getInput().keySet())
            if(i.isGroup())
                group.put((ResourceGroup) i, selectedCrafting.getInput().get(i));
            else
                single.put((ResourceSingle) i, selectedCrafting.getInput().get(i));

        //creating a limited storage to check if the cost of the crafting is correct
        LimitedStorage checkStorage = new LimitedStorage(single, group, "check");

        //try to get all selected resources and see if the cost is correct
        Map<ResourceSingle, Integer> flatSelectedResources =
                selectedResources.entrySet()
                        .stream()
                        .flatMap(x -> x.getValue().entrySet().stream())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                Integer::sum));

        // check if all resources are correct
        boolean isSelectionCorrect = true;
        String errorMessage = "";
        try{
            for(ResourceSingle res : flatSelectedResources.keySet())
                checkStorage.addResources(res, flatSelectedResources.get(res));
        } catch (IllegalResourceTransferException e) {
            errorMessage = "Wrong resources are selected";
            isSelectionCorrect = false;
        }

        // check if the storage is full
        if(!checkStorage.isFull()) {
            if(isSelectionCorrect)
                errorMessage = "Not enough resources selected";
            isSelectionCorrect = false;
        }

        if(!isSelectionCorrect){
            getGameContext().getCurrentPlayer().getBoard().getStorage().resetSelection();
            payload.add(PayloadFactory.unselect(getGameContext().getCurrentPlayer().getUsername(), "storage"));
            messages.add(new Message(getGameContext().getGameModel().getPlayerNames(), payload));
            throw new FSMTransitionFailedException(messages, errorMessage);
        }

        // remove all resources
        for(ResourceContainer container : selectedResources.keySet()) {
            Map<String, Integer> resourcesToRemove = new HashMap<>();
            for (ResourceSingle res : selectedResources.get(container).keySet()) {
                try {
                    resourcesToRemove.put(res.getId().toLowerCase(), -selectedResources.get(container).get(res));
                    container.removeResources(res, selectedResources.get(container).get(res));
                } catch (IllegalResourceTransferException e) {
                    Logger.log("Somehow a resource transfer failed and an exception wasn't thrown before!",
                            Logger.Severity.ERROR, ForegroundColor.RED);
                    throw new FSMTransitionFailedException("Somehow a resource transfer failed and an exception wasn't thrown before!");
                }
            }

            //add information about the removed resources
            payload.add(PayloadFactory.changeResources(getGameContext().getCurrentPlayer().getUsername(),
                    new RawStorage(container.getId(), resourcesToRemove)));
        }

        selectedCrafting.setAllResourcesTransferred(true);
        payload.add(PayloadFactory.changeCraftingStatus(getGameContext().getCurrentPlayer().getUsername(), true, production.getSelectedCraftingIndex(), production.getSelectedCraftingType()));

        production.resetCraftingSelection();
        payload.add(PayloadFactory.unselect(getGameContext().getCurrentPlayer().getUsername(), "production"));
        storage.resetSelection();
        payload.add(PayloadFactory.unselect(getGameContext().getCurrentPlayer().getUsername(), "storage"));


        messages.add(new Message(getGameContext().getGameModel().getPlayerNames(), payload));

        getGameContext().setPlayerMoved(true);
        setNextState(new CraftingState(getGameContext()));

        return messages;
    }

    /**
     * This method will be executed every time this state is entered from a different state.
     * Informs the current player of the possible actions to be performed
     * @return the list of messages to be sent to the client
     */
    @Override
    public List<Message> onEntry() {
        List<Message> messages = super.onEntry();

        List<PayloadComponent> payload = new ArrayList<>();

        payload.add(new InfoPayloadComponent("Select the resources needed for the crafting and then confirm"));
        payload.add(new PossibleActionsPayloadComponent(
                new HashSet<>(){{
                    add(PossibleActions.BACK);
                    add(PossibleActions.SELECT_RESOURCES);
                    add(PossibleActions.CONFIRM);
                }}
        ));
        messages.add(new Message(Collections.singletonList(getGameContext().getCurrentPlayer().getUsername()), payload));

        return messages;
    }
}
