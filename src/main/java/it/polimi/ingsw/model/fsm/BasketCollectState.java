package it.polimi.ingsw.model.fsm;

public class BasketCollectState extends State{
    /**
     * A new State is created with the provided game context. The nextState and the interruptListener
     * are both initially null
     *
     * @param gameContext the context of the game
     * @throws NullPointerException if gameContext is null
     */
    public BasketCollectState(GameContext gameContext) {
        super(gameContext);
    }
}
