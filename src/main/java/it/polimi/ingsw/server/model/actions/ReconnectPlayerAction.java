package it.polimi.ingsw.server.model.actions;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.payload_components.groups.InfoPayloadComponent;
import it.polimi.ingsw.common.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.common.exceptions.IllegalActionException;
import it.polimi.ingsw.server.model.fsm.ActionHandler;
import it.polimi.ingsw.server.model.fsm.GameContext;

import java.util.Collections;
import java.util.List;

/**
 * Class implements interface Action. This action manages the reconnection of a player, informing the state machine.
 */
public class ReconnectPlayerAction implements Action{

    private final String target;

    public ReconnectPlayerAction(String target){
        this.target = target;
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
     * Executes the action on the provided game context
     *
     * @param gameContext the current context of the game
     * @return a list of messages that contains information about the change applied to the model
     * @throws IllegalActionException if the action cannot be performed on the model
     * @throws NullPointerException   if gameContext is null
     */
    @Override
    public List<Message> execute(GameContext gameContext) throws IllegalActionException {
        gameContext.getGameModel().getPlayerById(target).setConnected(true);
        return Collections.singletonList(new Message(gameContext.getGameModel().getPlayerNames(),
                Collections.singletonList(new InfoPayloadComponent("Player \"" + target + "\" re-joined the game"))));
    }

    /**
     * Returns the sender of this action
     *
     * @return the sender of this action
     */
    @Override
    public String getSender() {
        return "AI";
    }

    /**
     * Returns the target of this action
     * @return the target of this action
     */
    public String getTarget() {
        return target;
    }

    /**
     * Checks if all attributes are set and have meaningful values.
     * In case they are not, this throws the appropriate RuntimeException.
     * It needs to be used since this class can be created by deserialization
     */
    @Override
    public void checkFormat() {
        if(this.target == null)
            throw new NullPointerException("The target of the reconnections is null");
    }
}
