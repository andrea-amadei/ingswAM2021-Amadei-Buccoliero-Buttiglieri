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
import it.polimi.ingsw.parser.raw.RawStorage;
import it.polimi.ingsw.utils.PayloadFactory;

import java.util.*;
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

        PayloadComponent _payload = new InfoPayload(amount + " of "
                + resourceToMove
                + " have been moved from "
                + currentPlayer.getUsername() + "'s basket to their " + shelf.getId()) ;

        List<PayloadComponent> payload = new ArrayList<>();

        payload.add(PayloadFactory.changeResources(currentPlayer.getUsername(),
                new RawStorage("MarketBasket", new HashMap<>(){{
                    put(resourceToMove.toString().toLowerCase(), -amount);
                }})
                ));
        payload.add(PayloadFactory.changeResources(currentPlayer.getUsername(),
                new RawStorage(shelf.getId(), new HashMap<>(){{
                    put(resourceToMove.toString().toLowerCase(), amount);
                }})
                ));

        return Collections.singletonList(new Message(destinations, payload));
    }
}
