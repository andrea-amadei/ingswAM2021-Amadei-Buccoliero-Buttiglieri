package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.fsm.ActionHandler;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.production.Crafting;
import it.polimi.ingsw.server.Logger;

import java.util.*;

public class SelectCraftingOutputAction implements Action {

    private final String player;
    private final Map<ResourceSingle, Integer> conversion;

    public SelectCraftingOutputAction(String player, Map<ResourceSingle, Integer> conversion) {
        this.player = player;
        this.conversion = conversion;
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

    @Override
    public List<Message> execute(GameContext gameContext) throws IllegalActionException {
        if(gameContext == null)
            throw new NullPointerException();

        GameModel model = gameContext.getGameModel();
        Player currentPlayer;
        Crafting crafting;

        try {
            currentPlayer = model.getPlayerById(player);
        } catch(NoSuchElementException e) {
            throw new IllegalActionException(e.getMessage());
        }

        try {
            crafting = currentPlayer.getBoard().getProduction().getSelectedCrafting();
        } catch (NoSuchElementException e) {
            throw new IllegalActionException(e.getMessage());
        }

        try {
            crafting.setGroupConversion(ResourceTypeSingleton.getInstance().getAnyResource(), conversion);
        } catch (IllegalArgumentException e) {
            //TODO: communicate to the client that there is the need to select the output again
            throw new IllegalActionException(e.getMessage());
        }

        if(crafting.getUndecidedOutputs().size() != 0)
            Logger.log("Logic failed at SelectCraftingOutputAction: after selecting conversion, there are still undecided outputs!",
                    Logger.Severity.ERROR, Logger.Format.RED);

        //TODO: we need to create a payload to communicate the selected output to the player.
        return new ArrayList<>();
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
        if(player == null || conversion == null)
            throw new NullPointerException();
    }
}
