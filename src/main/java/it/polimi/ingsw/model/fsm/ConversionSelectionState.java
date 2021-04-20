package it.polimi.ingsw.model.fsm;

import it.polimi.ingsw.common.InfoPayload;
import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.actions.SelectConversionsAction;

import java.util.Collections;
import java.util.List;

public class ConversionSelectionState extends State{
    /**
     * A new State is created with the provided game context. The nextState and the interruptListener
     * are both initially null
     *
     * @param gameContext the context of the game
     * @throws NullPointerException if gameContext is null
     */
    public ConversionSelectionState(GameContext gameContext) {
        super(gameContext);
    }

    /**
     * The client specifies the chosen conversions.
     * If the action can be performed, the selected resources are moved to the market basket and the faith points
     * are added to the faith path. Note: the faith path could launch an interrupt.
     * Next state is BasketCollectState
     * @param selectConversionsAction the action to be performed
     * @return the list of messages that need to be sent to the clients
     * @throws NullPointerException if selectConversion is null
     * @throws FSMTransitionFailedException if the action cannot be performed
     */
    @Override
    public List<Message> handleAction(SelectConversionsAction selectConversionsAction) throws FSMTransitionFailedException {
        if(selectConversionsAction == null)
            throw new NullPointerException();

        List<Message> messages;

        try {
            messages = selectConversionsAction.execute(getGameContext());
        }catch(IllegalActionException e){
            throw new FSMTransitionFailedException(e.getMessage());
        }

        setNextState(new BasketCollectState(getGameContext()));

        return messages;
    }

    /**
     * This method will be executed every time this state is entered from a different state.
     * It informs the current player of the possible actions to be performed
     * @return the list of messages to be sent to the client
     */
    @Override
    public List<Message> onEntry() {
        List<Message> messages = super.onEntry();
        messages.add(new Message(Collections.singletonList(getGameContext().getCurrentPlayer().getUsername()),
                Collections.singletonList(new InfoPayload("Possible Actions: SelectConversions"))));

        return messages;
    }

    /**
     * Returns a string representation of the object.
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "ConversionSelectionState";
    }
}
