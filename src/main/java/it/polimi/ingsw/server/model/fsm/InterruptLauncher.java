package it.polimi.ingsw.server.model.fsm;

import it.polimi.ingsw.server.model.actions.Action;

/**
 * Interface that defines methods for classes who can launch interrupts.
 */
public interface InterruptLauncher {

    /**
     * Sets the listener that will receive the interrupt.
     */
    void setListener(InterruptListener listener);

    /**
     * Removes the listener.
     */
    void removeListener();

    /**
     * Launches the interrupt.
     * @param interrupt the interrupt action.
     * @param priority the priority of the action.
     */
    void launchInterrupt(Action interrupt, int priority);
}
