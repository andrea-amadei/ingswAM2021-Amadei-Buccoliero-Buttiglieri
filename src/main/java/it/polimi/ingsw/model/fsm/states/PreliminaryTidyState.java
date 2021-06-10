package it.polimi.ingsw.model.fsm.states;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.payload_components.groups.PossibleActions;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.actions.ConfirmTidyAction;
import it.polimi.ingsw.model.actions.DisconnectPlayerAction;
import it.polimi.ingsw.model.actions.ResourcesMoveAction;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.fsm.State;
import it.polimi.ingsw.utils.GameUtilities;
import it.polimi.ingsw.utils.PayloadFactory;

import java.util.ArrayList;
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

        //calculate and execute transition to next state and next current player
        messages.addAll(executePreliminaryTidyTransition(getGameContext()));

        return messages;
    }

    /**
     * This action sets newly disconnected player as disconnected. Then if  the current player is the last player of the list,
     * the game goes to menu state and the first connected player of the list starts to play; if current player is not
     * the last player of the list, the next connected player goes to preliminary pick state.
     * @param disconnectPlayerAction the action to execute
     * @return the list of messages to send to the clients
     * @throws FSMTransitionFailedException iff action cannot be performed
     * @throws NullPointerException iff pointer to resourceMoveAction is null
     */
    @Override
    public List<Message> handleAction(DisconnectPlayerAction disconnectPlayerAction) throws FSMTransitionFailedException{
        if(disconnectPlayerAction == null)
            throw new NullPointerException();
        List<Message> messages;

        try {
            messages = new ArrayList<>(disconnectPlayerAction.execute(getGameContext()));
        }catch (IllegalActionException e){
            throw new FSMTransitionFailedException(e.getMessage());
        }

        //if disconnected player is the current player, the transition to the next state and the next current
        // player are executed
        if(disconnectPlayerAction.getTarget().equals(getGameContext().getCurrentPlayer().getUsername())) {
            messages.addAll(executePreliminaryTidyTransition(getGameContext()));
        }
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
     * executes transition from PreliminaryTidyState for both confirm and disconnect actions. Sets next state and next
     * current player accordingly.
     * @param gameContext the context of the game
     * @return the list of messages to send to the clients
     */
    private List<Message> executePreliminaryTidyTransition(GameContext gameContext){
        List<Message> messages = new ArrayList<>();
        Player nextConnectedPlayer = GameUtilities.calculateNextConnectedPlayer(gameContext);

        //if there are no players connected, return messages and set next state to this
        if(nextConnectedPlayer == null){
            setNextState(this);
            return messages;
        }

        //if round starts again, next state is MenuState and random picks must be made for players who have not
        // already picked
        if(GameUtilities.doesRoundStartAgain(gameContext)){
            messages.addAll(GameUtilities.automatedPick(gameContext));

            gameContext.setCurrentPlayer(nextConnectedPlayer);
            messages.add(new Message(gameContext.getGameModel().getPlayerNames(), Collections.singletonList(
                    PayloadFactory.changeCurrentPlayer(nextConnectedPlayer.getUsername()))));
            setNextState(new MenuState(gameContext));
            return messages;
        }

        //if round does not start again, next state is PreliminaryPickState and next player is NextConnectedPlayer
        gameContext.setCurrentPlayer(nextConnectedPlayer);
        messages.add(new Message(gameContext.getGameModel().getPlayerNames(), Collections.singletonList(
                PayloadFactory.changeCurrentPlayer(nextConnectedPlayer.getUsername()))));
        setNextState(new PreliminaryPickState(gameContext));

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
