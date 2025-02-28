package it.polimi.ingsw.server.model.actions;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.common.exceptions.IllegalActionException;
import it.polimi.ingsw.server.model.global.Player;
import it.polimi.ingsw.server.model.fsm.ActionHandler;
import it.polimi.ingsw.server.model.fsm.GameContext;
import it.polimi.ingsw.server.model.production.Crafting;
import it.polimi.ingsw.server.model.production.Production;
import it.polimi.ingsw.common.utils.PayloadFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class implements interface Action. This action allows the player to select a crafting from their production, to later
 * activate it.
 */
public class SelectCraftingAction implements Action {

    private final String player;
    private final Production.CraftingType craftingType;
    private final int index;

    /**
     * creates a new SelectCraftingAction.
     *
     * @param player the player who performs the action.
     * @param craftingType the type of the crafting to select (base, upgradable or leader).
     * @param index the index of the crafting.
     */
    public SelectCraftingAction(String player, Production.CraftingType craftingType, int index) {
        this.player = player;
        this.craftingType = craftingType;
        this.index = index;
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
     * Selects a certain crafting owned by a certain player, given the crafting type (base, upgradable or leader) and
     * the index of the crafting. Index (starting from 1) must identify a valid crafting, otherwise an
     * IllegalActionException is thrown.
     * @param gameContext the current context of the game
     * @return a list of messages that contains information about the change applied to the model
     * @throws NullPointerException if gameContext is null
     * @throws IllegalActionException if the action cannot be performed
     */
    @Override
    public List<Message> execute(GameContext gameContext) throws IllegalActionException {
        if(gameContext == null)
            throw new NullPointerException();

        Player currentPlayer;
        List<PayloadComponent> payload = new ArrayList<>();

        if(!player.equals(gameContext.getCurrentPlayer().getUsername()))
            throw new IllegalActionException("It is not your turn");

        currentPlayer = gameContext.getCurrentPlayer();

        Production production = currentPlayer.getBoard().getProduction();

        try {
            production.selectCrafting(craftingType, index);
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalActionException(e.getMessage());
        }

        Crafting crafting;
        try{
            crafting = production.getSelectedCrafting();
        } catch (IndexOutOfBoundsException e){
            throw new IllegalActionException(e.getMessage());
        }

        if(crafting == null) {
            production.resetCraftingSelection();
            throw new IllegalActionException("Cannot select an empty crafting slot");
        }

        if(crafting.readyToCraft()){
            production.resetCraftingSelection();
            throw new IllegalActionException("Cannot select a crafting already prepared");
        }

        //the selected crafting is valid, proceed to inform the client
        payload.add(PayloadFactory.selectedCrafting(currentPlayer.getUsername(), craftingType, index));

        //send the message
        List<String> targets = gameContext.getGameModel().getPlayerNames();
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
        if(player == null || craftingType == null)
            throw new NullPointerException();

        if(index < 0)
            throw new IllegalArgumentException("Index must be positive");
    }

    /**
     * returns the type of the selected crafting.
     * @return the type of the selected crafting.
     */
    public Production.CraftingType getCraftingType() {
        return craftingType;
    }

    /**
     * returns the index of the selected crafting.
     * @return the index of the selected crafting.
     */
    public int getIndex() {
        return index;
    }
}
