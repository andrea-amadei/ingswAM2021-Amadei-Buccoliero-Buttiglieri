package it.polimi.ingsw.model.lorenzo;

import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.InfoPayloadComponent;
import it.polimi.ingsw.model.fsm.GameContext;

import java.util.ArrayList;
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
        List<PayloadComponent> payload = new ArrayList<>(gameContext.getGameModel().getFaithPath().executeLorenzoMovement(amount));
        payload.add(new InfoPayloadComponent("Lorenzo Move: Lorenzo got " + amount + " faith points"));

        return payload;
    }
}
