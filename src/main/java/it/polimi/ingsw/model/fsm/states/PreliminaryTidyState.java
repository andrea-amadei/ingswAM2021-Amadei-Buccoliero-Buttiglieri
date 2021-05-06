package it.polimi.ingsw.model.fsm.states;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.payload_components.groups.PossibleActions;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.actions.ConfirmTidyAction;
import it.polimi.ingsw.model.actions.ResourcesMoveAction;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.fsm.State;
import it.polimi.ingsw.utils.PayloadFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class PreliminaryTidyState extends State {

    /**
     * A new State is created with the provided game context. The nextState and the interruptListener
     * are both initially null
     *
     * @param gameContext the context of the game
     * @throws NullPointerException if gameContext is null
     */
    public PreliminaryTidyState(GameContext gameContext) {
        super(gameContext);
    }

    /**
     * the player moves the resources they have in their hand to their cupboard
     * @param resourcesMoveAction the action to execute
     * @return the list of messages to send to the clients
     * @throws FSMTransitionFailedException iff action cannot be performed
     * @throws NullPointerException iff pointer to resourceMoveAction is null
     */
    @Override
    public List<Message> handleAction(ResourcesMoveAction resourcesMoveAction) throws FSMTransitionFailedException{
        if(resourcesMoveAction == null)
            throw new NullPointerException();
        List<Message> messages;

        try{
            messages = resourcesMoveAction.execute(getGameContext());
        }catch (IllegalActionException e){
            throw new FSMTransitionFailedException(e.getMessage());
        }
        setNextState(this);
        return messages;
    }

    /**
     * action makes sure current player's hand is empty. Then if current player is the last player of the list,
     * the game goes to menu state and the first connected player of the list starts to play; if current player is not
     * the last player of the list, the next connected player goes to preliminary pick state.
     * @param confirmTidyAction the action to execute
     * @return the list of messages to send to the clients
     * @throws FSMTransitionFailedException iff action cannot be performed
     * @throws NullPointerException iff pointer to resourceMoveAction is null
     */
    @Override
    public List<Message> handleAction(ConfirmTidyAction confirmTidyAction) throws FSMTransitionFailedException{
        if(confirmTidyAction == null)
            throw new NullPointerException();
        List<Message> messages;

        try {
            messages = confirmTidyAction.execute(getGameContext());
        }catch (IllegalActionException e){
            throw new FSMTransitionFailedException(e.getMessage());
        }

        //if there are no players, return messages and set next state to this
        if(getGameContext().getGameModel().getPlayers().stream().noneMatch(Player::isConnected)){
            setNextState(this);
            return messages;
        }

        //TODO: add the payload type to communicate to the clients the next player
        //set next connected player as next player
        Player nextPlayer = null;
        int index = getGameContext().getGameModel().getPlayers().indexOf(getGameContext().getCurrentPlayer());
        for(int i = index + 1; i < getGameContext().getGameModel().getPlayers().size(); i++){
            if(getGameContext().getGameModel().getPlayers().get(i).isConnected())
                nextPlayer = getGameContext().getGameModel().getPlayers().get(i);
        }

        //current player is not last player of the list, another player goes to preliminary pick state
        if(nextPlayer != null){
            setNextState(new PreliminaryPickState(getGameContext()));
            getGameContext().setCurrentPlayer(nextPlayer);
            return messages;
        }

        //at this point current player is the last player of the list. Next state is menu state and next player
        //is the first connected player starting from player 0
        nextPlayer = getGameContext().getGameModel().getPlayers().stream().filter(Player::isConnected).findFirst().orElse(null);
        setNextState(new MenuState(getGameContext()));
        getGameContext().setCurrentPlayer(nextPlayer);
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
                            add(PossibleActions.RESOURCE_MOVE);
                            add(PossibleActions.CONFIRM_TIDY);
                        }}
                ))));


        return messages;
    }

    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "PreliminaryTidyState";
    }

}
