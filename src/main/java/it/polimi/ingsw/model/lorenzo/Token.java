package it.polimi.ingsw.model.lorenzo;

import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.model.actions.Action;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.fsm.InterruptLauncher;
import it.polimi.ingsw.model.fsm.InterruptListener;

import java.util.List;

public abstract class Token implements InterruptLauncher {

    private InterruptListener interruptListener;

    public Token(){
        interruptListener = null;
    }

    /**
     * Executes the token and returns the list of changes
     * @param gameContext the game context
     * @return the list of changes
     */
    public abstract List<PayloadComponent> execute(GameContext gameContext);


    /**
     * Sets the listener of the interrupts launched
     * @param listener the listener
     * @throws NullPointerException if listener is null
     */
    @Override
    public void setListener(InterruptListener listener) {
        if(listener == null)
            throw new NullPointerException("The listener is null");

        this.interruptListener = listener;
    }

    /**
     * Removes the listener (set to null)
     */
    @Override
    public void removeListener() {
        this.interruptListener = null;
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
