package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.fsm.ActionHandler;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.production.Crafting;
import it.polimi.ingsw.model.production.Production;
import it.polimi.ingsw.utils.PayloadFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

public class SelectCraftingAction implements Action {

    private final String player;
    private final Production.CraftingType craftingType;
    private final int index;

    public SelectCraftingAction(String player, Production.CraftingType craftingType, int index) {
        if(player == null || craftingType == null)
            throw new NullPointerException();

        if(index < 0)
            throw new IllegalArgumentException("Index must be positive");

        this.player = player;
        this.craftingType = craftingType;
        this.index = index;
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

        GameModel model = gameContext.getGameModel();
        Player currentPlayer;
        List<PayloadComponent> payload = new ArrayList<>();

        try {
            currentPlayer = model.getPlayerById(player);
        } catch(NoSuchElementException e) {
            throw new IllegalActionException(e.getMessage());
        }

        Production production = currentPlayer.getBoard().getProduction();

        try {
            production.selectCrafting(craftingType, index);
            payload.add(PayloadFactory.selectedCrafting(currentPlayer.getUsername(), craftingType, index));
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalActionException(e.getMessage());
        }

        Crafting crafting;
        try{
            crafting = production.getSelectedCrafting();
        } catch (IndexOutOfBoundsException e){
            throw new IllegalActionException(e.getMessage());
        }

        if(crafting == null)
            throw new IllegalActionException("Cannot select an empty crafting slot");

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
}
