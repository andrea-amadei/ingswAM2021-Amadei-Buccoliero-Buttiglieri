package it.polimi.ingsw.model.fsm.states;

import it.polimi.ingsw.common.InfoPayload;
import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.exceptions.IllegalResourceTransferException;
import it.polimi.ingsw.gamematerials.ResourceGroup;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceType;
import it.polimi.ingsw.model.actions.BackAction;
import it.polimi.ingsw.model.actions.ConfirmAction;
import it.polimi.ingsw.model.actions.SelectResourcesAction;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.fsm.State;
import it.polimi.ingsw.model.production.Crafting;
import it.polimi.ingsw.model.production.Production;
import it.polimi.ingsw.model.storage.LimitedStorage;
import it.polimi.ingsw.model.storage.ResourceContainer;
import it.polimi.ingsw.model.storage.Storage;
import it.polimi.ingsw.server.Console;

import java.util.*;
import java.util.stream.Collectors;

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

        messages.add(new Message(getGameContext().getGameModel().getPlayerNames(),
                Arrays.asList(  new InfoPayload("Reset undecided outputs"),
                                new InfoPayload("Reset selected crafting"),
                                new InfoPayload("Reset selection")
                )));

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
        try {
            messages = confirmAction.execute(getGameContext());
        } catch(IllegalActionException e) {
            throw new FSMTransitionFailedException(e.getMessage());
        }

        Storage storage = getGameContext().getCurrentPlayer().getBoard().getStorage();
        Production production = getGameContext().getCurrentPlayer().getBoard().getProduction();

        Map<ResourceContainer, Map<ResourceSingle, Integer>> selectedResources = storage.getSelection();
        Crafting selectedCrafting;
        try {
            selectedCrafting = production.getSelectedCrafting();
        } catch(NoSuchElementException e) {
            throw new FSMTransitionFailedException(e.getMessage());
        }

        if(selectedResources == null)
            throw new FSMTransitionFailedException("No resources were selected");

        Map<ResourceSingle, Integer> single = new HashMap<>();
        Map<ResourceGroup, Integer> group = new HashMap<>();

        for(ResourceType i : selectedCrafting.getInput().keySet())
            if(i.isGroup())
                group.put((ResourceGroup) i, selectedCrafting.getInput().get(i));
            else
                single.put((ResourceSingle) i, selectedCrafting.getInput().get(i));

        //creating a limited storage to check if the cost of the card is correct
        LimitedStorage checkStorage = new LimitedStorage(single, group);

        //try to get all selected resources and see if the cost is correct
        Map<ResourceSingle, Integer> flatSelectedResources =
                selectedResources.entrySet()
                        .stream()
                        .flatMap(x -> x.getValue().entrySet().stream())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                Integer::sum));

        // check if all resources are correct
        try{
            for(ResourceSingle res : flatSelectedResources.keySet())
                checkStorage.addResources(res, flatSelectedResources.get(res));
        } catch (IllegalResourceTransferException e) {
            throw new FSMTransitionFailedException("Wrong resources selected");
        }

        // check if the store is full
        if(!checkStorage.isFull())
            throw new FSMTransitionFailedException("Too few resources selected");

        // remove all resources
        for(ResourceContainer container : selectedResources.keySet())
            for(ResourceSingle res : container.getAllResources().keySet())
                try {
                    container.removeResources(res, container.getAllResources().get(res));
                } catch (IllegalResourceTransferException e) {
                    Console.log("Somehow a resource transfer failed and an exception wasn't thrown before!",
                            Console.Severity.ERROR, Console.Format.RED);
                }

        selectedCrafting.readyToCraft();
        production.resetCraftingSelection();

        //TODO: add more precise resource remove
        messages.add(new Message(getGameContext().getGameModel().getPlayerNames(),
                Arrays.asList(  new InfoPayload("Removed resources"),
                                new InfoPayload("Reset selected crafting"),
                                new InfoPayload("Marked crafting as ready")
                )));

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


        //TODO: if not possible, hide Confirm
        messages.add(new Message(Collections.singletonList(getGameContext().getCurrentPlayer().getUsername()),
                Collections.singletonList(new InfoPayload("Possible Actions: Back, SelectResources, Confirm"))));

        return messages;
    }
}
