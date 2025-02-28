package it.polimi.ingsw.server.model.fsm.states;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.InfoPayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.PossibleActions;
import it.polimi.ingsw.common.payload_components.groups.PossibleActionsPayloadComponent;
import it.polimi.ingsw.common.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.common.exceptions.IllegalActionException;
import it.polimi.ingsw.server.model.global.Player;
import it.polimi.ingsw.server.model.actions.ConfirmTidyAction;
import it.polimi.ingsw.server.model.actions.DisconnectPlayerAction;
import it.polimi.ingsw.server.model.actions.ReconnectPlayerAction;
import it.polimi.ingsw.server.model.actions.ResourcesMoveAction;
import it.polimi.ingsw.server.model.fsm.GameContext;
import it.polimi.ingsw.server.model.fsm.State;
import it.polimi.ingsw.common.utils.GameUtilities;
import it.polimi.ingsw.common.utils.PayloadFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Extends State and represents a concrete state of the state machine.
 * In this state the game has just started and the player has already made their preliminary pick. They can
 * move to their preferred shelf the resources they have chosen, if they have any, or confirm their choice.
 */
public class PreliminaryTidyState extends State {

    private boolean alreadyEntered = false;

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
     * If the game was in stale:
     *      -if the reconnected player comes before the old current player, automated picks are performed and
     *       next state is MenuState
     *      -if the reconnected player is the old current player, next state is still this state
     *      -if the reconnected player comes after the old current player, next state is PreliminaryPickState
     *
     * If the game is not in stale, then nothing happens
     * @param reconnectPlayerAction the reconnecting player
     * @return the messages that needs to be sent to the clients
     * @throws FSMTransitionFailedException if the action cannot be executed
     * @throws NullPointerException if reconnectPlayerAction is null
     */
    @Override
    public List<Message> handleAction(ReconnectPlayerAction reconnectPlayerAction) throws FSMTransitionFailedException{
        List<Message> messages;
        try {
            messages = new ArrayList<>(reconnectPlayerAction.execute(getGameContext()));
        }catch(IllegalActionException e){
            throw new FSMTransitionFailedException(e.getMessage());
        }

        if(GameUtilities.numOfConnectedPlayers(getGameContext()) > 1){
            resetNextState();
            return messages;
        }

        //we were in stall
        Player reconnectingPlayer = getGameContext().getGameModel().getPlayerById(reconnectPlayerAction.getTarget());
        Player oldCurrentPlayer = getGameContext().getCurrentPlayer();

        //set the next current player
        getGameContext().setCurrentPlayer(reconnectingPlayer);
        messages.add(new Message(getGameContext().getGameModel().getPlayerNames(), Collections.singletonList(
                PayloadFactory.changeCurrentPlayer(reconnectingPlayer.getUsername()))));

        //if the reconnecting player is the old current player, then it continues to do what he was doing
        if(reconnectingPlayer.getUsername().equals(oldCurrentPlayer.getUsername())){
            setNextState(this);
            return messages;
        }

        //if the reconnecting player comes before the old current player, then automated picks are performed and next state is menu state
        if(GameUtilities.comesFirst(getGameContext(), reconnectingPlayer, oldCurrentPlayer)){
            messages.addAll(GameUtilities.automatedPick(getGameContext()));
            setNextState(new MenuState(getGameContext()));
            return messages;
        }

        //the reconnected player needs to make their pick. So next state is PreliminaryPick
        setNextState(new PreliminaryPickState(getGameContext()));
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

        List<PayloadComponent> payload = new ArrayList<>();
        
        if(!alreadyEntered){
            payload.add(new InfoPayloadComponent("Tidy your resources and then confirm"));
            alreadyEntered = true;
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
