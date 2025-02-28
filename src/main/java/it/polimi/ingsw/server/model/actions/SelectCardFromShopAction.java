package it.polimi.ingsw.server.model.actions;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.common.exceptions.IllegalActionException;
import it.polimi.ingsw.server.model.global.GameModel;
import it.polimi.ingsw.server.model.global.Player;
import it.polimi.ingsw.server.model.global.Shop;
import it.polimi.ingsw.server.model.fsm.ActionHandler;
import it.polimi.ingsw.server.model.fsm.GameContext;
import it.polimi.ingsw.server.model.production.CraftingCard;
import it.polimi.ingsw.server.model.production.Production;
import it.polimi.ingsw.server.model.production.UpgradableCrafting;
import it.polimi.ingsw.common.utils.PayloadFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Class implements interface Action. This action allows the player to select the card they intend to buy from the shop.
 * The player also has to select the slot of their production where they intend to put the card.
 */
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
        this.player = player;
        this.row = row;
        this.col = col;
        this.upgradableCraftingId = upgradableCraftingId;
        checkFormat();
    }

    /**
     * Calls the appropriate method of the handler
     *
     * @param handler the handler that will execute this action
     * @return the list of messages to send to the client
     * @throws NullPointerException         if handler is null
     * @throws FSMTransitionFailedException if the state fails to execute this action
     */
    @Override
    public List<Message> acceptHandler(ActionHandler handler) throws FSMTransitionFailedException {
        if(handler == null)
            throw new NullPointerException();
        return handler.handleAction(this);
    }

    /**
     * Selects a card in the shop and a slot in the production.
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
        if(!player.equals(gameContext.getCurrentPlayer().getUsername()))
            throw new IllegalActionException("It is not your turn");

        currentPlayer = gameContext.getCurrentPlayer();

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
        List<String> targets = gameContext.getGameModel().getPlayerNames();
        List<PayloadComponent> payload = new ArrayList<>();

        //Add the card selection
        payload.add(PayloadFactory.selectedShopCard(currentPlayer.getUsername(), col, row));

        //Add the crafting selection
        payload.add(PayloadFactory.selectedCrafting(currentPlayer.getUsername(), Production.CraftingType.UPGRADABLE, upgradableCraftingIndex));
        Message message = new Message(targets, payload);

        return Collections.singletonList(message);
    }

    /**
     * Returns the sender of this action
     *
     * @return the sender of this action
     */
    @Override
    public String getSender() {
        return player;
    }

    /**
     * Checks if all attributes are set and have meaningful values.
     * In case they are not, this throws the appropriate RuntimeException.
     * It needs to be used since this class can be created by deserialization
     */
    @Override
    public void checkFormat() {
        if(player == null)
            throw new NullPointerException();

        if(row < 0 || col < 0 || upgradableCraftingId < 0)
            throw new IllegalArgumentException("row, col and upgradableCraftingId can't be negative");

    }

    /**
     * returns the row of the shop grid where the card is.
     * @return the row of the shop grid where the card is
     */
    public int getRow() {
        return row;
    }

    /**
     * returns the column of the shop grid where the card is.
     * @return the colums of the shop grid where the card is.
     */
    public int getCol() {
        return col;
    }

    /**
     * returns the index of the production slot where the player wants to put the card.
     * @return the index of the production slot where the player wants to put the card.
     */
    public int getUpgradableCraftingId() {
        return upgradableCraftingId;
    }
}
