package it.polimi.ingsw.common;

import it.polimi.ingsw.model.actions.Action;

import java.util.*;

//TODO: needs to be synchronized (concurrent access of controller and client handlers)
public class ActionQueue {

    /**
     * This enum defines different priorities that action can take
     */
    public enum Priority{CLIENT_ACTION, SERVER_ACTION}

    /**
     * This class wraps an action and it is used to keep the FIFO ordering
     * for action with same priority
     */
    private static class ActionWrapper implements Comparable<ActionWrapper>{

        private final long sequenceNumber;
        private final int priority;
        private final Action action;


        ActionWrapper(Action action, long sequenceNumber, int priority){
            this.sequenceNumber = sequenceNumber;
            this.action = action;
            this.priority = priority;
        }

        public int getPriority() {
            return priority;
        }

        public long getSequenceNumber() {
            return sequenceNumber;
        }

        public Action getAction() {
            return action;
        }

        @Override
        public int compareTo(ActionWrapper o) {
            return (this.priority > o.getPriority() || (this.priority == o.getPriority() && this.sequenceNumber > o.getSequenceNumber())) ? 1 : -1 ;
        }
    }


    private final PriorityQueue<ActionWrapper> actions;
    private long currentSequenceNumber;


    public ActionQueue(){
        actions = new PriorityQueue<>();
        currentSequenceNumber = 0;
    }

    /**
     * Adds a new action with the specified priority. Order of insertion is preserved
     * @param action the action to be added to the queue
     * @param priority the priority of this action
     */
    public void addAction(Action action, int priority){
        ActionWrapper wrapper = new ActionWrapper(action, currentSequenceNumber, priority);
        actions.add(wrapper);
        currentSequenceNumber++;
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

        return actions.poll().getAction();
    }

    /**
     * Returns true if the queue is empty
     * @return true if the queue is empty
     */
    public boolean isEmpty() {
        return actions.isEmpty();
    }
}

