package it.polimi.ingsw.model.fsm;

import it.polimi.ingsw.common.ActionQueue;
import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.actions.*;
import it.polimi.ingsw.model.fsm.states.EndGameState;
import it.polimi.ingsw.model.fsm.states.EndTurnState;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract State. A subclass of this class represents a concrete state of the state machine.
 * A state can handle multiple actions. By default the state throws an exception when handling an action.
 * Every concrete state can override some handleAction overloads to properly handle the acceptable transitions for
 * that particular concrete state.
 */
public abstract class State implements InterruptLauncher, ActionHandler{
    private final GameContext gameContext;
    private State nextState;
    private InterruptListener interruptListener;

    /**
     * A new State is created with the provided game context. The nextState and the interruptListener
     * are both initially null
     * @param gameContext the context of the game
     * @throws NullPointerException if gameContext is null
     */
    public State(GameContext gameContext){
        if(gameContext == null)
            throw new NullPointerException();
        this.gameContext = gameContext;
        nextState = null;
        interruptListener = null;
    }

    //Some default action handling override

    /**
     * If the action can be performed, this method returns the list of messages that need to be sent to the client.
     * If the game needs to be ended immediately, a new EndGameAction is queued.
     * The next state is the current state.
     * @param popeCheckAction the action to be executed
     * @return the list of messages that need to be sent to the clients
     * @throws NullPointerException if backAction is null
     * @throws FSMTransitionFailedException if the action cannot be executed
     */
    @Override
    public List<Message> handleAction(PopeCheckAction popeCheckAction) throws FSMTransitionFailedException {
        if(popeCheckAction == null)
            throw new NullPointerException();

        List<Message> messages;
        try{
            messages = new ArrayList<>(popeCheckAction.execute(getGameContext()));
        }catch(IllegalActionException e){
            throw new FSMTransitionFailedException(e.getMessage());
        }

        if(getGameContext().isHardEndTriggered())
            launchInterrupt(new EndGameAction(), ActionQueue.Priority.SERVER_ACTION.ordinal());

        setNextState(this);
        return messages;
    }

    /**
     * If the action can be performed, this method returns the list of messages that need to be sent to the client.
     * The next state is the end state.
     * @param endGameAction the action to be executed
     * @return the list of messages that need to be sent to the clients
     * @throws NullPointerException if backAction is null
     * @throws FSMTransitionFailedException if the action cannot be executed
     */
    @Override
    public List<Message> handleAction(EndGameAction endGameAction) throws FSMTransitionFailedException {
        if(endGameAction == null)
            throw new NullPointerException();

        List<Message> messages;
        try{
            messages = new ArrayList<>(endGameAction.execute(getGameContext()));
        }catch(IllegalActionException e){
            throw new FSMTransitionFailedException(e.getMessage());
        }

        setNextState(new EndGameState(getGameContext()));
        return messages;
    }

    /**
     * Disconnects a player.
     * @param disconnectPlayerAction the action to be executed
     * @return the list of messages that need to be sent to the clients
     * @throws FSMTransitionFailedException iff the action cannot be executed
     */
    @Override
    public List<Message> handleAction(DisconnectPlayerAction disconnectPlayerAction) throws FSMTransitionFailedException {
        List<Message> messages;
        try {
            messages = new ArrayList<>(disconnectPlayerAction.execute(getGameContext()));
        }catch(IllegalActionException e){
            throw new FSMTransitionFailedException(e.getMessage());
        }

        if(!disconnectPlayerAction.getTarget().equals(getGameContext().getCurrentPlayer().getUsername())){
            resetNextState();
        }else{
            setNextState(new EndTurnState(getGameContext()));
        }

        return messages;
    }

    /**
     * Reconnects a player
     * @param reconnectPlayerAction the action to be executed
     * @return the list of messages that need to be sent to the clients
     * @throws FSMTransitionFailedException iff the action cannot be executed
     */
    @Override
    public List<Message> handleAction(ReconnectPlayerAction reconnectPlayerAction) throws FSMTransitionFailedException {
        try {
            resetNextState();
            return reconnectPlayerAction.execute(getGameContext());
        }catch(IllegalActionException e){
            throw new FSMTransitionFailedException(e.getMessage());
        }
    }

    /**
     * This method will be executed every time this state is entered from a different state
     * @return the list of messages to be sent to the client
     */
    public List<Message> onEntry(){return new ArrayList<>();}

    /**
     * This method will be executed before the execution of the current action (weather or not the action is valid)
     * @return the list of messages to be sent to the client
     */
    public List<Message> onActionStart(){return new ArrayList<>();}

    /**
     * This method will be executed after the execution of the current action (only if the action is valid)
     * @return the list of messages to be sent to the client
     */
    public List<Message> onActionEnd(){return new ArrayList<>();}

    /**
     * This method will be executed every time the state machine exits from this state and enters in a different one
     * @return the list of messages to be sent to the client
     */
    public List<Message> onExit(){return new ArrayList<>();}

    /**
     * Returns the next state. Null if the next state is not set
     * @return the next state. Null if the next state is not set
     */
    public State getNextState(){
        return nextState;
    }

    /**
     * Sets the next state to null
     */
    public void resetNextState(){
        nextState = null;
    }

    /**
     * Sets the next state
     * @param nextState the nextState
     * @throws NullPointerException if nextState is null
     */
    protected void setNextState(State nextState){
        if(nextState == null)
            throw new NullPointerException();
        this.nextState = nextState;
    }

    /**
     * Returns the current game context
     * @return the current game context
     */
    protected GameContext getGameContext(){
        return gameContext;
    }

    /**
     * Sets the listener of the interrupts launched
     * @param listener the listener
     * @throws NullPointerException if listener is null
     */
    @Override
    public void setListener(InterruptListener listener) {
        if(listener == null)
            throw new NullPointerException();

        interruptListener = listener;
    }

    /**
     * Removes the listener (set to null)
     */
    @Override
    public void removeListener() {
        interruptListener = null;
    }

    /**
     * Launches the interrupt to the listener (if the listener is not null)
     * @param interrupt the interrupt to be launched
     * @throws NullPointerException if the interrupt is null
     */
    @Override
    public void launchInterrupt(Action interrupt, int priority) {
        if(interrupt == null)
            throw new NullPointerException();
        if(interruptListener != null)
            interruptListener.launchInterrupt(interrupt, priority);
    }


}
