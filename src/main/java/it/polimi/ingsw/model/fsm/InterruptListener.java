package it.polimi.ingsw.model.fsm;

import it.polimi.ingsw.model.actions.Action;

public interface InterruptListener {
    void launchInterrupt(Action interrupt);
}
