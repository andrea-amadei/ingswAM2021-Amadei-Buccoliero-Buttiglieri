package it.polimi.ingsw.model.fsm;

import it.polimi.ingsw.model.actions.Action;

public interface InterruptLauncher {
    void setListener(InterruptListener listener);
    void removeListener();
    void launchInterrupt(Action interrupt);
}
