package it.polimi.ingsw.common;

import it.polimi.ingsw.model.actions.Action;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;

//TODO: needs to be synchronized (concurrent access of controller and client handlers)
public class ActionQueue {
    private final Deque<Action> actions;

    public ActionQueue(){
        actions = new ArrayDeque<>();
    }

    /**
     * Appends a new action to the end of the deque
     * @param action the action to be appended
     * @throws NullPointerException if action is null
     */
    public void appendLast(Action action){
        if(action == null)
            throw new NullPointerException();
        actions.addLast(action);
    }

    /**
     * Appends a new action to the head of the deque
     * @param action the action to be appended
     * @throws NullPointerException if action is null
     */
    public void appendFirst(Action action){
        if(action == null)
            throw new NullPointerException();
        actions.addFirst(action);
    }

    //TODO: the exception will be substituted with a wait statement (the controller is the consumer)
    /**
     * Pops the first action from the deque. The action is removed from the deque.
     * @return the popped action
     * @throws NoSuchElementException if the deque is empty
     */
    public Action pop(){
        if(actions.isEmpty())
            throw new NoSuchElementException("The action deque is empty");

        return actions.pop();
    }
}
