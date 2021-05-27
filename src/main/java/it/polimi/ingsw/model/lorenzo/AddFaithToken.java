package it.polimi.ingsw.model.lorenzo;

import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.model.fsm.GameContext;

import java.util.List;

public class AddFaithToken extends Token{
    private final int amount;

    public AddFaithToken(int amount){
        super();
        this.amount = amount;
    }

    /**
     * Executes the token and returns the list of changes
     *
     * @param gameContext the game context
     * @return the list of changes
     */
    @Override
    public List<PayloadComponent> execute(GameContext gameContext) {
        return gameContext.getGameModel().getFaithPath().executeLorenzoMovement(amount);
    }
}
