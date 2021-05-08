package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.fsm.ActionHandler;
import it.polimi.ingsw.model.fsm.GameContext;

import java.util.List;

/**
 * Common interface of an action. It defines the method execute that will execute the action
 * on the provided game context. Each action will also return a message that contains information about
 * the change applied to the model
 */
public interface Action {

    /**
     * Calls the appropriate method of the handler
     * @param handler the handler that will execute this action
     * @return the list of messages to send to the client
     * @throws NullPointerException if handler is null
     * @throws FSMTransitionFailedException if the state fails to execute this action
     */
    List<Message> acceptHandler(ActionHandler handler) throws FSMTransitionFailedException;

    /**
     * Executes the action on the provided game context
     * @param gameContext the current context of the game
     * @return a list of messages that contains information about the change applied to the model
     * @throws IllegalActionException if the action cannot be performed on the model
     * @throws NullPointerException if gameContext is null
     */
    List<Message> execute(GameContext gameContext) throws IllegalActionException;

    /**
     * Returns the sender of this action
     * @return the sender of this action
     */
    String getSender();

    /**
     * Checks if all attributes are set and have meaningful values.
     * In case they are not, this throws the appropriate RuntimeException.
     * It needs to be used since this class can be created by deserialization
     */
    void checkFormat();
}
