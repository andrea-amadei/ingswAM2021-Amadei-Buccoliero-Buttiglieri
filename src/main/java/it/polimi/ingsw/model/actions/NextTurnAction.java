package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.fsm.GameContext;

import java.util.ArrayList;
import java.util.List;

public class NextTurnAction implements Action{
    /**
     * Informs the state machine that a new turn is started
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
        return new ArrayList<>();
    }
}
