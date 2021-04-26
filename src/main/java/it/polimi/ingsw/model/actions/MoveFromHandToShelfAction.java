package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.common.InfoPayload;
import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.PayloadComponent;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.exceptions.IllegalResourceTransferException;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.fsm.ActionHandler;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.storage.Shelf;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * This action moves resources from the hand to the shelf of a player
 */
@Deprecated(forRemoval = true)
public class MoveFromHandToShelfAction implements Action{

    private final String player;
    private final ResourceSingle resourceToMove;
    private final int amount;
    private final String shelfId;

    /**
     * Creates the action with all information needed to be performed
     * @param player the player that wants to move the resources
     * @param resourceToMove the resource type to move
     * @param amount the amount of resources to move
     * @param shelfId the id of the shelf into which the resources will be moved
     * @throws NullPointerException if at least one of the parameters is null
     * @throws IllegalArgumentException if the amount of resources to move is negative
     */
    public MoveFromHandToShelfAction(String player, ResourceSingle resourceToMove, int amount, String shelfId){
        if(player == null || resourceToMove == null || shelfId == null)
            throw new NullPointerException();
        if(amount <= 0)
            throw new IllegalArgumentException("Amount can't be negative");

        this.player = player;
        this.resourceToMove = resourceToMove;
        this.amount = amount;
        this.shelfId = shelfId;
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
     * Executes this action
     * @param gameContext the current context of the game
     * @return a list of messages with the information about what has changed
     * @throws IllegalActionException if the action cannot be performed
     * @throws NullPointerException if the gameContext is null
     */
    @Override
    public List<Message> execute(GameContext gameContext) throws IllegalActionException {
        if(gameContext == null)
            throw new NullPointerException();

        GameModel model = gameContext.getGameModel();
        Player currentPlayer;
        Shelf shelf;

        try {
            currentPlayer = model.getPlayerById(player);
        }catch(NoSuchElementException e){
            throw new IllegalActionException(e.getMessage());
        }

        try {
            shelf = currentPlayer.getBoard().getStorage().getCupboard().getShelfById(shelfId);
        }catch(NoSuchElementException e){
            throw new IllegalActionException(e.getMessage());
        }

        try{
            currentPlayer.getBoard().getStorage().getHand().moveTo(shelf, resourceToMove, amount);
        }catch(IllegalResourceTransferException e){
            throw new IllegalActionException(e.getMessage());
        }

        List<String> destinations = model.getPlayers()
                                         .stream()
                                         .map(Player::getUsername)
                                         .collect(Collectors.toList());

        PayloadComponent payload = new InfoPayload(amount + " of "
                                                  + resourceToMove
                                                  + " have been moved from "
                                                  + currentPlayer.getUsername() + "'s hand to "
                                                  + shelf.getId());

        return Collections.singletonList(new Message(destinations, Collections.singletonList(payload)));
    }
}
