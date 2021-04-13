package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.common.InfoPayload;
import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.PayloadComponent;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.exceptions.IllegalResourceTransferException;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.storage.Shelf;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * action moves resources from a player's basket to one of their shelves
 */
public class MoveFromBasketToShelfAction implements Action{

    private final String player;
    private final ResourceSingle resourceToMove;
    private final int amount;
    private final String shelfID;

    /**
     * MoveFromBasketToShelf action constructor
     * @param player the player performing the action
     * @param resourceToMove the resource that is being moved
     * @param amount the amount of said resource
     * @param shelfID the ID of the shelf the resource is being moved to
     * @throws NullPointerException iff pointer to player, resource to move, or shelf is null
     * @throws IllegalArgumentException iff amount of resources to move is negative or zero
     */
    public MoveFromBasketToShelfAction(String player, ResourceSingle resourceToMove, int amount, String shelfID){

        if(player == null || resourceToMove == null || shelfID == null)
            throw new NullPointerException();

        if(amount <= 0)
            throw new IllegalArgumentException("Amount cannot be negative or zero");

        this.player = player;
        this.resourceToMove = resourceToMove;
        this.amount = amount;
        this.shelfID = shelfID;
    }

    /**
     * method executes the action
     * @param gameContext the current context of the game
     * @return the list of messages with info about the changes to the game
     * @throws IllegalActionException iff the action cannot be performed
     * @throws NullPointerException iff pointer to player is null
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
            shelf = currentPlayer.getBoard().getStorage().getCupboard().getShelfById(shelfID);
        }catch(NoSuchElementException e){
            throw new IllegalActionException(e.getMessage());
        }

        try{
            currentPlayer.getBoard().getStorage().getMarketBasket().moveTo(shelf, resourceToMove, amount);
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
                + currentPlayer.getUsername() + "'s basket to their " + shelf.getId()) ;

        return Collections.singletonList(new Message(destinations, Collections.singletonList(payload)));
    }
}
