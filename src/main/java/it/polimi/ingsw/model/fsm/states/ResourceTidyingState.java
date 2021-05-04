package it.polimi.ingsw.model.fsm.states;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.actions.ConfirmTidyAction;
import it.polimi.ingsw.model.actions.ResourcesMoveAction;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.fsm.State;

import java.util.ArrayList;
import java.util.List;

public class ResourceTidyingState extends State {
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


}
