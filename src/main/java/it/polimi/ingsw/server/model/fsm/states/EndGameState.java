package it.polimi.ingsw.server.model.fsm.states;

import it.polimi.ingsw.server.model.fsm.GameContext;
import it.polimi.ingsw.server.model.fsm.State;

/**
 * Extends State and represents a concrete state of the state machine.
 * In this state the game is ended.
 */
public class EndGameState extends State {
    /**
     * A new State is created with the provided game context. The nextState and the interruptListener
     * are both initially null
     *
     * @param gameContext the context of the game
     * @throws NullPointerException if gameContext is null
     */
    public EndGameState(GameContext gameContext) {
        super(gameContext);
    }
}
