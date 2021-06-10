package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.fsm.ActionHandler;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.utils.GameUtilities;

import java.util.*;

public class PreliminaryPickAction implements Action{

    private final String player;
    private final List<Integer> leadersToDiscard;
    private final Map<ResourceSingle, Integer> chosenResources;

    /**
     * PreliminaryPickAction constructor.
     * @param player the player performing the action
     * @param leadersToDiscard the list of to be discarded leaders' IDs
     * @param chosenResources the resources chosen by the player
     * @throws NullPointerException iff pointer to player, leadersToDiscard or chosenResources is null
     * @throws IllegalArgumentException iff IDs of leaders or amount of chosen resources are negative.
     */
    public PreliminaryPickAction(String player, List<Integer> leadersToDiscard, Map<ResourceSingle, Integer> chosenResources){
        this.player = player;
        this.leadersToDiscard = leadersToDiscard;
        this.chosenResources = chosenResources;
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
     * Action discards selected leaders, and potentially adds faith points and moves chosen resources to the player's hand.
     * @param gameContext the current context of the game
     * @return a list of messages that contains information about the change applied to the model
     * @throws IllegalActionException if the action cannot be performed on the model
     * @throws NullPointerException   if gameContext is null
     */
    @Override
    public List<Message> execute(GameContext gameContext) throws IllegalActionException {
        if(gameContext == null)
            throw new NullPointerException();

        Player currentPlayer = gameContext.getCurrentPlayer();
        if(!currentPlayer.getUsername().equals(player))
            throw new IllegalActionException("Player must wait for their turn to perform this action");

        return GameUtilities.doPreliminaryPick(gameContext, player, leadersToDiscard, chosenResources);
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
        if(player == null || leadersToDiscard == null || chosenResources == null)
            throw new NullPointerException();

        for(Integer i : leadersToDiscard)
            if(i<0)
                throw new IllegalArgumentException("IDs of leaders to discard must be between 0 and total amount of leaders");

        for(Map.Entry<ResourceSingle, Integer> entry : chosenResources.entrySet())
            if(entry.getValue()<0)
                throw new IllegalArgumentException("Amount of chosen resources cannot be negative");
    }

    public List<Integer> getLeadersToDiscard() {
        return leadersToDiscard;
    }

    public Map<ResourceSingle, Integer> getChosenResources() {
        return chosenResources;
    }
}
