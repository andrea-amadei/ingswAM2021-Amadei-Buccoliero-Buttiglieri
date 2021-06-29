package it.polimi.ingsw.server.model.fsm.states;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.InfoPayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.PossibleActions;
import it.polimi.ingsw.common.payload_components.groups.PossibleActionsPayloadComponent;
import it.polimi.ingsw.common.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.common.exceptions.IllegalActionException;
import it.polimi.ingsw.server.model.actions.ConfirmTidyAction;
import it.polimi.ingsw.server.model.actions.ResourcesMoveAction;
import it.polimi.ingsw.server.model.fsm.GameContext;
import it.polimi.ingsw.server.model.fsm.State;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Extends State and represents a concrete state of the state machine.
 * In this state the player has already chosen what resources to buy from the market and has them in their
 * market basket. They can now reorganize their storage to make room for the new resources, or confirm their
 * choice.
 */
public class ResourceTidyingState extends State {

    private boolean alreadyVisited = false;
    /**
     * A new State is created with the provided game context. The nextState and the interruptListener
     * are both initially null
     *
     * @param gameContext the context of the game
     * @throws NullPointerException if gameContext is null
     */
    public ResourceTidyingState(GameContext gameContext) {
        super(gameContext);
    }
    /**
     * The player moves resources between hand and shelves to get ready to collect resources from the market basket.
     * After the execution the state doesn't change.
     * @param resourcesMoveAction the action to be performed
     * @return the list of messages that need to be sent to the clients
     * @throws NullPointerException if resourcesMoveAction is null
     * @throws FSMTransitionFailedException if the action cannot be performed
     */
    @Override
    public List<Message> handleAction(ResourcesMoveAction resourcesMoveAction) throws FSMTransitionFailedException {
        if(resourcesMoveAction == null)
            throw new NullPointerException();

        List<Message> messages;

        try {
            messages = new ArrayList<>(resourcesMoveAction.execute(getGameContext()));
        }catch(IllegalActionException e){
            throw new FSMTransitionFailedException(e.getMessage());
        }

        setNextState(this);

        return messages;
    }

    /**
     * The player confirms that he/she has the desired storage configuration.
     * After this action is performed, the player cannot tidy his storage and it will only be possible to collect
     * resources from the market basket.
     * Note: The hand of the player must be empty, otherwise the action will fail.
     * The next state is BasketCollectState
     * @param confirmTidyAction the action to be performed
     * @return the list of messages that need to be sent to the clients
     * @throws NullPointerException if confirmTidyAction is null
     * @throws FSMTransitionFailedException if the action cannot be performed
     */
    @Override
    public List<Message> handleAction(ConfirmTidyAction confirmTidyAction) throws FSMTransitionFailedException {
        if(confirmTidyAction == null)
            throw new NullPointerException();

        List<Message> messages;

        try {
            messages = confirmTidyAction.execute(getGameContext());
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
    public List<Message> onEntry(){
        List<Message> messages = super.onEntry();

        List<PayloadComponent> payload = new ArrayList<>();

        if(!alreadyVisited) {
            payload.add(new InfoPayloadComponent("You can now move resources between shelves and hand, or confirm the configuration"));
            alreadyVisited = true;
        }
        payload.add(new PossibleActionsPayloadComponent(
            new HashSet<>(){{
                add(PossibleActions.RESOURCE_MOVE);
                add(PossibleActions.CONFIRM_TIDY);
            }}
        ));
        messages.add(new Message(Collections.singletonList(getGameContext().getCurrentPlayer().getUsername()), payload));

        return messages;
    }


}
