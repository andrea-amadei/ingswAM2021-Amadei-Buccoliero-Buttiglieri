package it.polimi.ingsw.model.lorenzo;

import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.InfoPayloadComponent;
import it.polimi.ingsw.model.fsm.GameContext;

import java.util.ArrayList;
import java.util.List;

public class ShuffleToken extends Token{

    private final int amount;

    public ShuffleToken(int amount){
        this.amount = amount;
    }

    /**
     * Adds the specified amount of faith points to Lorenzo
     *
     * @param gameContext the game context
     * @return the list of changes
     */
    @Override
    public List<PayloadComponent> execute(GameContext gameContext) {
        List<PayloadComponent> payload = new ArrayList<>(gameContext.getGameModel().getFaithPath().executeLorenzoMovement(amount));
        gameContext.getGameModel().shuffleTokens();

        payload.add(new InfoPayloadComponent("Lorenzo Move: Lorenzo got " + amount + " faith points and all tokens are shuffled"));
        return payload;
    }
}
