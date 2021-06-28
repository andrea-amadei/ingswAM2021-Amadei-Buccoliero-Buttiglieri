package it.polimi.ingsw.model.fsm.states;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.InfoPayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.PossibleActions;
import it.polimi.ingsw.common.payload_components.groups.PossibleActionsPayloadComponent;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.actions.DisconnectPlayerAction;
import it.polimi.ingsw.model.actions.PreliminaryPickAction;
import it.polimi.ingsw.model.actions.ReconnectPlayerAction;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.fsm.State;
import it.polimi.ingsw.utils.GameUtilities;
import it.polimi.ingsw.utils.PayloadFactory;

import java.util.*;

/**
 * Extends State and represents a concrete state of the state machine.
 * In this state the game has just started and the player must discard the correct amount of leader cards
 * and select the resources they want.
 */
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
     * a player disconnects during the preliminary pick phase and their disconnection is handled according to the game logic.
     * @param disconnectPlayerAction the action to be executed
     * @return the list of messages to send to the clients
     * @throws FSMTransitionFailedException iff the action cannot be executed
     * @throws NullPointerException iff pointer to preliminaryPickAction is null
     */
    @Override
    public List<Message> handleAction(DisconnectPlayerAction disconnectPlayerAction) throws FSMTransitionFailedException{
        if(disconnectPlayerAction == null)
            throw new NullPointerException();
        List<Message> messages;
        String disconnectedPlayerID = disconnectPlayerAction.getTarget();

        try {
            messages = new ArrayList<>(disconnectPlayerAction.execute(getGameContext()));
        }catch (IllegalActionException e){
            throw new FSMTransitionFailedException(e.getMessage());
        }

        Player nextConnectedPlayer = GameUtilities.calculateNextConnectedPlayer(getGameContext());

        //if there are no players connected, game pauses at current state
        if(nextConnectedPlayer == null){
            setNextState(this);
            return messages;
        }

        //player disconnected is current player
        if(disconnectedPlayerID.equals(getGameContext().getCurrentPlayer().getUsername())){
            //player disconnected is current player and last connected player
            if(GameUtilities.doesRoundStartAgain(getGameContext())){
                messages.addAll(GameUtilities.automatedPick(getGameContext()));
                getGameContext().setCurrentPlayer(GameUtilities.calculateNextConnectedPlayer(getGameContext()));
                messages.add(new Message(getGameContext().getGameModel().getPlayerNames(), Collections.singletonList(
                        PayloadFactory.changeCurrentPlayer(nextConnectedPlayer.getUsername()))));
                setNextState(new MenuState(getGameContext()));
            }
            //player disconnected is current player but not the last connected player
            else {
                getGameContext().setCurrentPlayer(GameUtilities.calculateNextConnectedPlayer(getGameContext()));
                messages.add(new Message(getGameContext().getGameModel().getPlayerNames(), Collections.singletonList(
                        PayloadFactory.changeCurrentPlayer(nextConnectedPlayer.getUsername()))));
                setNextState(new PreliminaryPickState(getGameContext()));
            }
        }

        return  messages;
    }

    /**
     * Reconnects the player. If the player hasn't lost the turn, the game remains in this state.
     * If the player lost the turn, automated picks are computed and next state is MenuState
     * @param reconnectPlayerAction the action to be executed
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

        //check if the reconnected player needs to do their turn
        if(GameUtilities.comesFirst(getGameContext(), reconnectingPlayer, oldCurrentPlayer)){
            messages.addAll(GameUtilities.automatedPick(getGameContext()));

            setNextState(new MenuState(getGameContext()));
            return messages;
        }

        //the reconnected player needs to do their turn
        setNextState(this);
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

        payload.add(new InfoPayloadComponent("Please select the cards to discard and the resources to receive"));

        payload.add(new PossibleActionsPayloadComponent(
            new HashSet<>(){{
                add(PossibleActions.PRELIMINARY_PICK);
            }}
        ));
        messages.add(new Message(Collections.singletonList(getGameContext().getCurrentPlayer().getUsername()), payload));

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
