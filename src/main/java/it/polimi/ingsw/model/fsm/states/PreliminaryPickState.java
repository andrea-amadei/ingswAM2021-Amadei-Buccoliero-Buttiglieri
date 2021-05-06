package it.polimi.ingsw.model.fsm.states;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.payload_components.groups.PossibleActions;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.actions.PreliminaryPickAction;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.fsm.State;
import it.polimi.ingsw.utils.PayloadFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class PreliminaryPickState extends State {
    /**
     * A new State is created with the provided game context. The nextState and the interruptListener
     * are both initially null
     * @param gameContext the context of the game
     * @throws NullPointerException if gameContext is null
     */
    public PreliminaryPickState(GameContext gameContext) {
        super(gameContext);
    }

    /**
     * the player chooses the leaders to discard and, depending on the player's order, picks resources and adds faith points.
     * @param preliminaryPickAction the action to execute
     * @return the list of messages to send to the clients
     * @throws FSMTransitionFailedException iff the action cannot be executed
     * @throws NullPointerException iff pointer to preliminaryPickAction is null
     */
    @Override
    public List<Message> handleAction(PreliminaryPickAction preliminaryPickAction) throws FSMTransitionFailedException{
        if(preliminaryPickAction == null)
            throw new NullPointerException();
        List<Message> messages;

        try{
            messages = new ArrayList<>(preliminaryPickAction.execute(getGameContext()));
        }catch (IllegalActionException e){
            throw new FSMTransitionFailedException(e.getMessage());
        }
        setNextState(new PreliminaryTidyState(getGameContext()));
        return messages;
    }

    /**
     * This method will be executed every time this state is entered.
     * Informs the current player of the next available actions.
     * @return the list of messages to send to the client
     */
    @Override
    public List<Message> onEntry() {
        List<Message> messages = super.onEntry();

        messages.add(new Message(Collections.singletonList(getGameContext().getCurrentPlayer().getUsername()),
                Collections.singletonList(PayloadFactory.possibleActions(
                        new HashSet<>(){{
                            add(PossibleActions.PRELIMINARY_PICK);
                        }}
                ))));

        return messages;
    }

    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "PreliminaryPickState";
    }

}
