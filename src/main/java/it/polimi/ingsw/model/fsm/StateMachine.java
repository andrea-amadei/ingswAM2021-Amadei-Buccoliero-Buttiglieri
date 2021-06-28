package it.polimi.ingsw.model.fsm;

import it.polimi.ingsw.common.ActionQueue;
import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.ErrorPayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.InfoPayloadComponent;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.model.actions.Action;

import java.util.*;

public class StateMachine implements InterruptListener{

    private final ActionQueue actionQueue;
    private final GameContext gameContext;
    private State currentState;

    public StateMachine(ActionQueue actionQueue, GameContext gameContext){
        this.actionQueue = actionQueue;
        this.gameContext = gameContext;

        currentState = null;
    }

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

    public GameContext getGameContext() {
        return gameContext;
    }
}
