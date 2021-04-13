package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.fsm.GameContext;

import java.util.List;

/**
 * Common interface of an action. It defines the method execute that will execute the action
 * on the provided game context. Each action will also return a message that contains information about
 * the change applied to the model
 */
public interface Action {

    /**
     * Executes the action on the provided game context
     * @param gameContext the current context of the game
     * @return a list of messages that contains information about the change applied to the model
     * @throws IllegalActionException if the action cannot be performed on the model
     * @throws NullPointerException if gameContext is null
     */
    List<Message> execute(GameContext gameContext) throws IllegalActionException;
}
