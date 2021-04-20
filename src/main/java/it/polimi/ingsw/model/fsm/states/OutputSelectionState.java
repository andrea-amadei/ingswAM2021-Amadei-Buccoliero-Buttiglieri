package it.polimi.ingsw.model.fsm.states;

import it.polimi.ingsw.common.InfoPayload;
import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.actions.BackAction;
import it.polimi.ingsw.model.actions.SelectCraftingOutputAction;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.fsm.State;

import java.util.Collections;
import java.util.List;

public class OutputSelectionState extends State {
    /**
     * A new State is created with the provided game context. The nextState and the interruptListener
     * are both initially null
     *
     * @param gameContext the context of the game
     * @throws NullPointerException if gameContext is null
     */
    public OutputSelectionState(GameContext gameContext) {
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

        getGameContext().getCurrentPlayer().getBoard().getProduction().resetCraftingSelection();

        setNextState(new CraftingState(getGameContext()));

        messages.add(new Message(getGameContext().getGameModel().getPlayerNames(),
                Collections.singletonList(new InfoPayload("Reset selected crafting"))));

        return messages;
    }

    /**
     * The player wants to select all the undecided crafting outputs
     *
     * If the action can be performed, this method returns the list of messages that need to be sent to the client
     * and sets the moved property remains false in the game context. The new state will be MenuState
     * @param selectCraftingOutputAction the action to be executed
     * @return the list of messages that need to be sent to the clients
     * @throws NullPointerException if backAction is null
     * @throws FSMTransitionFailedException if the action cannot be executed
     */
    @Override
    public List<Message> handleAction(SelectCraftingOutputAction selectCraftingOutputAction) throws FSMTransitionFailedException {
        if(selectCraftingOutputAction == null)
            throw new NullPointerException();

        List<Message> messages;
        try {
            messages = selectCraftingOutputAction.execute(getGameContext());
        } catch(IllegalActionException e) {
            throw new FSMTransitionFailedException(e.getMessage());
        }

        setNextState(new CraftingResourceSelectionState(getGameContext()));

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

        messages.add(new Message(Collections.singletonList(getGameContext().getCurrentPlayer().getUsername()),
                Collections.singletonList(new InfoPayload("Possible Actions: Back, SelectOutputs"))));

        return messages;
    }

    /**
     * Returns a string representation of the object.
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "OutputSelectionState";
    }
}
