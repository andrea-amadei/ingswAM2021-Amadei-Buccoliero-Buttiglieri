package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.fsm.ActionHandler;
import it.polimi.ingsw.model.fsm.GameContext;

import java.util.ArrayList;
import java.util.List;

public class BackAction implements Action {

    private final String player;

    /**
     * Creates a new BackAction. It is used to return to the previous state (if possible)
     * @param player the username of the player that wants to go back
     * @throws NullPointerException if player is null
     */
    public BackAction(String player){
        this.player = player;
        checkFormat();
    }

    /**
     * Calls the appropriate method of the handler
     *
     * @param handler the handler that will execute this action
     * @return the list of messages to send to the client
     * @throws NullPointerException         if handler is null
     * @throws FSMTransitionFailedException if the state fails to execute this action
     */
    @Override
    public List<Message> acceptHandler(ActionHandler handler) throws FSMTransitionFailedException {
        if(handler == null)
            throw new NullPointerException();
        return handler.handleAction(this);
    }

    /**
     * The player requests to go to the previous state, canceling everything he was doing
     * until that point. The back action cannot always be activated since some states or actions are
     * irreversible.
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

        Player currentPlayer;

        if(!player.equals(gameContext.getCurrentPlayer().getUsername()))
            throw new IllegalActionException("It is not your turn");

        currentPlayer = gameContext.getCurrentPlayer();

        if(!currentPlayer.equals(gameContext.getCurrentPlayer()))
            throw new IllegalActionException("The current player doesn't match the executor player");

        return new ArrayList<>();
    }

    /**
     * Returns the sender of this action
     *
     * @return the sender of this action
     */
    @Override
    public String getSender() {
        return player;
    }

    /**
     * Checks if all attributes are set and have meaningful values.
     * In case they are not, this throws the appropriate RuntimeException.
     * It needs to be used since this class can be created by deserialization
     */
    @Override
    public void checkFormat() {
        if(player == null)
            throw new NullPointerException();
    }
}
