package it.polimi.ingsw.model.fsm;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.model.actions.*;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract State. A subclass of this class represents a concrete state of the state machine.
 * A state can handle multiple actions. By default the state throws an exception when handling an action.
 * Every concrete state can override some handleAction overloads to properly handle the acceptable transitions for
 * that particular concrete state.
 */
public abstract class State implements InterruptLauncher{
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

    /**
     * It handles the specific action required. By default it throws an FSMTransitionFailedException.
     * If a specific state needs to handle the action, this method will be overloaded.
     * If the action is handled, this method will set the nextState.
     * @param activateLeaderAction the action to be executed
     * @return the list of messages to be sent to the client
     * @throws NullPointerException if the action is null
     * @throws FSMTransitionFailedException if the action cannot be handled
     */
    public List<Message> handleAction(ActivateLeaderAction activateLeaderAction) throws FSMTransitionFailedException {
        if(activateLeaderAction == null)
            throw new NullPointerException();
        throw new FSMTransitionFailedException("Cannot execute this command now");
    }

    //TODO: add doc for every overload (?)
    public List<Message> handleAction(BuyFromMarketAction buyFromMarketAction) throws FSMTransitionFailedException {
        if(buyFromMarketAction == null)
            throw new NullPointerException();
        throw new FSMTransitionFailedException("Cannot execute this command now");
    }
    public List<Message> handleAction(ConfirmTidyAction confirmTidyAction) throws FSMTransitionFailedException {
        if(confirmTidyAction == null)
            throw new NullPointerException();
        throw new FSMTransitionFailedException("Cannot execute this command now");
    }
    //TODO: add all other possible actions


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
    public void launchInterrupt(Action interrupt) {
        if(interrupt == null)
            throw new NullPointerException();
        if(interruptListener != null)
            interruptListener.launchInterrupt(interrupt);
    }


}
