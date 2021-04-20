package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.common.InfoPayload;
import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.PayloadComponent;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Shop;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.production.CraftingCard;
import it.polimi.ingsw.model.production.Production;
import it.polimi.ingsw.model.production.UpgradableCrafting;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

public class SelectCardFromShopAction implements Action{

    private final String player;
    private final int row;
    private final int col;
    private final int upgradableCraftingId;


    /**
     * Creates a new SelectedCardFromShopAction.
     * @param player the player that wants to select the card
     * @param row the row of the shop
     * @param col the col of the shop
     * @param upgradableCraftingId the id of the upgradable crafting to which the player wants to put the card
     * @throws NullPointerException if player is null
     * @throws IllegalArgumentException if row, col and upgradableCraftingId are not non negative
     */
    public SelectCardFromShopAction(String player, int row, int col, int upgradableCraftingId){
        if(player == null)
            throw new NullPointerException();

        if(row < 0 || col < 0 || upgradableCraftingId < 0)
            throw new IllegalArgumentException("row, col and upgradableCraftingId can't be negative");

        this.player = player;
        this.row = row;
        this.col = col;
        this.upgradableCraftingId = upgradableCraftingId;
    }

    /**
     * Selects a card in the market and a slot in the production.
     * Once this action is executed, the player needs to select some resources in order to buy the card.
     * The action can be executed only if row and col params are valid, if there is a card at the specified location
     * and if the selected card can be inserted in the selected upgradable crafting.
     * @param gameContext the current context of the game
     * @return a list of messages that contains information about the change applied to the model
     * @throws IllegalActionException if the action cannot be performed on the model
     * @throws NullPointerException   if gameContext is null
     */
    @Override
    public List<Message> execute(GameContext gameContext) throws IllegalActionException {
        if(gameContext == null)
            throw new NullPointerException();

        GameModel model = gameContext.getGameModel();
        Shop shop = model.getShop();
        Player currentPlayer;

        //try to retrieve the current player
        try{
            currentPlayer = model.getPlayerById(player);
        }catch(NoSuchElementException e){
            throw new IllegalActionException(e.getMessage());
        }

        Production production = currentPlayer.getBoard().getProduction();
        CraftingCard card;

        //try to select the card from the shop
        try{
            card = shop.selectCard(row, col);
        }catch(IllegalArgumentException | NoSuchElementException e){
            throw new IllegalActionException(e.getMessage());
        }

        //try to select the target upgradable crafting
        try{
            production.selectCrafting(Production.CraftingType.UPGRADABLE, upgradableCraftingId);
        }catch(IndexOutOfBoundsException e){
            shop.resetSelectedCard();
            throw new IllegalActionException(e.getMessage());
        }

        Integer upgradableCraftingIndex = production.getSelectedCraftingIndex();
        UpgradableCrafting upgradableCrafting = production.getUpgradableCrafting(upgradableCraftingIndex);

        //check if the new crafting can be put on the selected one
        if(upgradableCrafting == null) {
            if(card.getFlag().getLevel() != 1){
                shop.resetSelectedCard();
                production.resetCraftingSelection();
                throw new IllegalActionException("Tried to add a card of level " + card.getFlag().getLevel()
                                                 + " upon an empty slot");
            }
        }else{
            if(card.getFlag().getLevel() != upgradableCrafting.getLevel() + 1){
                shop.resetSelectedCard();
                production.resetCraftingSelection();
                throw new IllegalActionException("Tried to add a card of level " + card.getFlag().getLevel()
                                                 + " upon a slot of level " + upgradableCrafting.getLevel());
            }
        }

        //send the message
        List<String> targets = Collections.singletonList(player);
        PayloadComponent info = new InfoPayload(player + " wants to buy the card at (" + row + ", " + col + ")"
                                                + " and wants to put it in the slot " + upgradableCraftingIndex);

        Message message = new Message(targets, Collections.singletonList(info));

        return Collections.singletonList(message);
    }
}
