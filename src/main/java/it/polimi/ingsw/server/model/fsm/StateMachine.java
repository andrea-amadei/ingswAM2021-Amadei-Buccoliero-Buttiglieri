package it.polimi.ingsw.server.model.fsm;

import it.polimi.ingsw.common.ActionQueue;
import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.payload_components.groups.ErrorPayloadComponent;
import it.polimi.ingsw.common.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.server.model.actions.Action;

import java.util.*;

/**
 * It is a state machine. It manages the flow of the game, preventing the player from making moves that would not be
 * valid within the context of the game.
 * It implements interface InterruptListener, hence managing the received interrupts.
 * It handles the entire process of executing actions and sending messages to the clients.
 */
public class StateMachine implements InterruptListener{

    private final ActionQueue actionQueue;
    private final GameContext gameContext;
    private State currentState;

    /**
     * Creates a state machine, setting the current state to null.
     * @param actionQueue the action que.
     * @param gameContext the context of the game.
     */
    public StateMachine(ActionQueue actionQueue, GameContext gameContext){
        this.actionQueue = actionQueue;
        this.gameContext = gameContext;

        currentState = null;
    }

    /**
     * Creates a state machine.
     * @param actionQueue the action que.
     * @param gameContext the context of the game.
     * @param initialState the initial state.
     */
    public StateMachine(ActionQueue actionQueue, GameContext gameContext, State initialState){
        this(actionQueue, gameContext);
        this.currentState = initialState;
        initialState.setListener(this);
    }


    /**
     * Executes the specified action. It firstly calls the onActionStart of the current state, then proceeds to try
     * @param action the action to be executed
     * @return the list of messages to be sent to the clients
     */
    public List<Message> executeAction(Action action){
        if(action == null)
            throw new NullPointerException();

        List<Message> messages;

        //before the action handling
        messages = new ArrayList<>(currentState.onActionStart());

        //action handling
        try {
            messages.addAll(action.acceptHandler(currentState));
        } catch (FSMTransitionFailedException e) {
            messages.addAll(e.getGameMessages());
            messages.add(new Message(Collections.singletonList(action.getSender()), Collections.singletonList(
                    new ErrorPayloadComponent(Optional.ofNullable(e.getMessage()).orElse("generic error")))));
            currentState.resetNextState();
        }

        //on action exit
        messages.addAll(currentState.onActionEnd());

        //if the transition happened, call the onExit method of the current state and the onEntry method of the next state
        if(currentState.getNextState() != null){
            messages.addAll(currentState.onExit());
            currentState.removeListener();

            currentState = currentState.getNextState();

            currentState.setListener(this);
            messages.addAll(currentState.onEntry());
        }

        return messages;
    }

    /**
     * Returns the current state
     * @return the current state
     */
    public State getCurrentState(){
        return currentState;
    }

    /**
     * Puts the specified action at the head of the dequeue
     * @param interrupt the action to be put to the head of the deque
     * @throws NullPointerException if interrupt is null
     */
    @Override
    public void launchInterrupt(Action interrupt, int priority) {
        actionQueue.addAction(interrupt, priority);
    }

    /**
     * Returns the context og the game
     * @return the context og the game
     */
    public GameContext getGameContext() {
        return gameContext;
    }
}
