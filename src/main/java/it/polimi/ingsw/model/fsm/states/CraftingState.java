package it.polimi.ingsw.model.fsm.states;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.InfoPayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.PossibleActions;
import it.polimi.ingsw.common.payload_components.groups.PossibleActionsPayloadComponent;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.actions.BackAction;
import it.polimi.ingsw.model.actions.ConfirmAction;
import it.polimi.ingsw.model.actions.SelectCraftingAction;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.fsm.State;
import it.polimi.ingsw.utils.PayloadFactory;

import java.util.*;

public class CraftingState extends State {
    /**
     * A new State is created with the provided game context. The nextState and the interruptListener
     * are both initially null
     *
     * @param gameContext the context of the game
     * @throws NullPointerException if gameContext is null
     */
    public CraftingState(GameContext gameContext) {
        super(gameContext);
    }

    /**
     * The player wants to go back in the menu state.
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

        if(getGameContext().getCurrentPlayer().getBoard().getProduction().isCraftingReady())
            throw new FSMTransitionFailedException("Cannot go back to menu if a crafting is ready");

        List<Message> messages;

        try {
            messages = new ArrayList<>(backAction.execute(getGameContext()));
        } catch(IllegalActionException e) {
            throw new FSMTransitionFailedException(e.getMessage());
        }

        setNextState(new MenuState(getGameContext()));
        return messages;
    }

    /**
     * The player wants to activate the production and go back to the menu.
     * It is not possible to execute this action if no crafting is ready to craft.
     *
     * If the action can be performed, this method returns the list of messages that need to be sent to the client
     * and sets the moved property remains false in the game context. The new state will be MenuState
     * @param confirmAction the action to be executed
     * @return the list of messages that need to be sent to the clients
     * @throws NullPointerException if backAction is null
     * @throws FSMTransitionFailedException if the action cannot be executed
     */
    @Override
    //TODO: REMAKE EVERYTHING ONCE EVERYTHING IS OVER
    public List<Message> handleAction(ConfirmAction confirmAction) throws FSMTransitionFailedException {
        if(confirmAction == null)
            throw new NullPointerException();

        if(!getGameContext().getCurrentPlayer().getBoard().getProduction().isCraftingReady())
            throw new FSMTransitionFailedException("Cannot activate production if no crafting is ready");

        List<Message> messages;

        try {
            messages = new ArrayList<>(confirmAction.execute(getGameContext()));
        } catch(IllegalActionException e) {
            throw new FSMTransitionFailedException(e.getMessage());
        }

        //activate production and retrieve all the associated payload components
        List<PayloadComponent> payload = new ArrayList<>(
                getGameContext().getCurrentPlayer().getBoard().getProduction().activateProduction(
                    getGameContext().getCurrentPlayer(),
                    getGameContext().getGameModel().getFaithPath()
                )
        );


        setNextState(new MenuState(getGameContext()));

        messages.add(new Message(getGameContext().getGameModel().getPlayerNames(), payload));
        return messages;
    }

    /**
     * The player wants to go choose the crafting to use
     *
     * If the action can be performed, this method returns the list of messages that need to be sent to the client.
     * if the crafting doesn't have any undecided outputs, the new state will be CraftingResourceSelection,
     * otherwise next state will be OutputSelection
     * @param selectCraftingAction the action to be executed
     * @return the list of messages that need to be sent to the clients
     * @throws NullPointerException if backAction is null
     * @throws FSMTransitionFailedException if the action cannot be executed
     */
    @Override
    public List<Message> handleAction(SelectCraftingAction selectCraftingAction) throws FSMTransitionFailedException {
        if(selectCraftingAction == null)
            throw new NullPointerException();

        List<Message> messages;

        try {
            messages = new ArrayList<>(selectCraftingAction.execute(getGameContext()));
        } catch(IllegalActionException e) {
            throw new FSMTransitionFailedException(e.getMessage());
        }

        //if the crafting doesn't have any undecided outputs, the new state will be CraftingResourceSelection
        //otherwise next state will be OutputSelection
        if(getGameContext().getCurrentPlayer().getBoard().getProduction().getSelectedCrafting().getUndecidedOutputs().isEmpty())
            setNextState(new CraftingResourceSelectionState(getGameContext()));
        else
            setNextState(new OutputSelectionState(getGameContext()));


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
        Set<PossibleActions> possibleActions = new HashSet<>();

        StringBuilder sb = new StringBuilder();
        sb.append("Select a slot in the production for crafting");

        possibleActions.add(PossibleActions.SELECT_CRAFTING);
        possibleActions.add(PossibleActions.BACK);
        if(getGameContext().getCurrentPlayer().getBoard().getProduction().isCraftingReady()){
            sb.append(" or confirm to start the production");
            possibleActions.add(PossibleActions.CONFIRM);
        }

        payload.add(new InfoPayloadComponent(sb.toString()));
        payload.add(new PossibleActionsPayloadComponent(possibleActions));
        messages.add(new Message(Collections.singletonList(getGameContext().getCurrentPlayer().getUsername()), payload));


        return messages;
    }

    /**
     * Returns a string representation of the object.
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "CraftingState";
    }
}
