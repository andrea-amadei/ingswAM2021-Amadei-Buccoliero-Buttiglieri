package it.polimi.ingsw.model.fsm.states;

import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.fsm.State;

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
