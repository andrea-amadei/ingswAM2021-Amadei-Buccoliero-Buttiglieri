package it.polimi.ingsw.model.fsm.states;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.InfoPayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.PossibleActions;
import it.polimi.ingsw.common.payload_components.groups.PossibleActionsPayloadComponent;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.actions.SelectConversionsAction;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.fsm.State;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Extends State and represents a concrete state of the state machine.
 * In this state the player has already chosen what marbles to buy from the market and needs now to select which conversions
 * to apply. The only available move is to select the conversions.
 */
public class ConversionSelectionState extends State {
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
     * Next state is ResourceTidyingState
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

        setNextState(new ResourceTidyingState(getGameContext()));

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

        List<PayloadComponent> payload = new ArrayList<>();
        payload.add(new InfoPayloadComponent("Select the conversions for the selected marbles"));
        payload.add(new PossibleActionsPayloadComponent(
                new HashSet<>(){{
                    add(PossibleActions.SELECT_CONVERSIONS);
                }}
        ));
        messages.add(new Message(Collections.singletonList(getGameContext().getCurrentPlayer().getUsername()),
                payload));

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
