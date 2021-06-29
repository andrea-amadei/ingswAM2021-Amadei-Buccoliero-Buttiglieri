package it.polimi.ingsw.server.model.fsm;

import it.polimi.ingsw.server.model.actions.Action;

/**
 * Interface that defines methods for classes who can handle the interrupts.
 */
public interface InterruptListener {

    /**
     * Launches the interrupt.
     * @param interrupt the interrupt action.
     * @param priority the priority of the action.
     */
    void launchInterrupt(Action interrupt, int priority);
}
