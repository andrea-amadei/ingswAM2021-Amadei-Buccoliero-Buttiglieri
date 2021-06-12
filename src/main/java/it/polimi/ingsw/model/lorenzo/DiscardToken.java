package it.polimi.ingsw.model.lorenzo;

import it.polimi.ingsw.common.ActionQueue;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.InfoPayloadComponent;
import it.polimi.ingsw.gamematerials.FlagColor;
import it.polimi.ingsw.model.Shop;
import it.polimi.ingsw.model.actions.EndGameAction;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.utils.PayloadFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class DiscardToken extends Token{

    private final FlagColor color;
    private final int amount;

    public DiscardToken(FlagColor color, int amount){
        super();
        this.color = color;
        this.amount = amount;
    }

    /**
     * Removes the 2 cards of the selected color with the lower level from the shop.
     * If it is not possible to remove the 2 cards, an EndGameAction is thrown.
     * If after removing the 2 cards a column of the shop is empty, an EndGameAction is thrown.
     * @param gameContext the game context
     * @return the list of changes
     */
    @Override
    public List<PayloadComponent> execute(GameContext gameContext) {
        Shop shop = gameContext.getGameModel().getShop();

        //Try to remove the desired amount of cards from the shop. After the while loop either all the required cards
        //are removed or it is not possible to remove any other card (the column is empty)
        List<PayloadComponent> payload = new ArrayList<>();
        int removedCardCount = 0;
        int level = 1;
        while(level <= shop.getRowSize()){
            try{
                shop.removeCard(level, color);
                int discoveredCardId;
                try {
                    discoveredCardId = shop.getTopCard(level, color).getId();
                }catch(NoSuchElementException e1){
                    discoveredCardId = -1;
                }

                payload.add(PayloadFactory.changeShop(color.ordinal(), level - 1, discoveredCardId));

                removedCardCount++;
                if(removedCardCount == amount)
                    break;
            }catch(NoSuchElementException e){
                level++;
            }
        }

        if(shop.isColumnEmpty(color.ordinal())) {
            gameContext.setHardEnd();
            gameContext.setLorenzoWon(true);
            launchInterrupt(new EndGameAction(), ActionQueue.Priority.SERVER_ACTION.ordinal());
        }

        payload.add(new InfoPayloadComponent("Lorenzo Move: Lorenzo discards " + amount + " " + color.name().toLowerCase() + " cards"));
        return payload;
    }
}
