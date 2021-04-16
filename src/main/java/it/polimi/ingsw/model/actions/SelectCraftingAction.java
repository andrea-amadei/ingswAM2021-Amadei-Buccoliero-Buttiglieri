package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.common.InfoPayload;
import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.PayloadComponent;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.production.Production;

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

        if(index <= 0)
            throw new IllegalArgumentException("Index must be positive");

        this.player = player;
        this.craftingType = craftingType;
        this.index = index;
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
        Production production;
        Player currentPlayer;

        try {
            currentPlayer = model.getPlayerById(player);
        } catch(NoSuchElementException e) {
            throw new IllegalActionException(e.getMessage());
        }

        try {
            currentPlayer.getBoard().getProduction().selectUpgradableCrafting(craftingType, index);
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalActionException(e.getMessage());
        }

        //send the message
        List<String> targets = Collections.singletonList(player);
        PayloadComponent info = new InfoPayload(player + " selected the crafting at (" + craftingType + ", " + index + ")");
        Message message = new Message(targets, Collections.singletonList(info));

        return Collections.singletonList(message);
    }
}
