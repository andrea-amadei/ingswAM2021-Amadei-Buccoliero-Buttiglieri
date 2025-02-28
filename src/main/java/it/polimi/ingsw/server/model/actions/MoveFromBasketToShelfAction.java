package it.polimi.ingsw.server.model.actions;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.common.exceptions.IllegalActionException;
import it.polimi.ingsw.common.exceptions.IllegalCupboardException;
import it.polimi.ingsw.server.model.basetypes.ResourceSingle;
import it.polimi.ingsw.server.model.global.GameModel;
import it.polimi.ingsw.server.model.global.Player;
import it.polimi.ingsw.server.model.fsm.ActionHandler;
import it.polimi.ingsw.server.model.fsm.GameContext;
import it.polimi.ingsw.server.model.storage.Shelf;
import it.polimi.ingsw.common.parser.raw.RawStorage;
import it.polimi.ingsw.common.utils.PayloadFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class implements interface Action. This action moves resources from a player's basket to one of their shelves.
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
        this.player = player;
        this.resourceToMove = resourceToMove;
        this.amount = amount;
        this.shelfID = shelfID;
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

        if(!player.equals(gameContext.getCurrentPlayer().getUsername()))
            throw new IllegalActionException("It is not your turn");

        currentPlayer = gameContext.getCurrentPlayer();

        try {
            shelf = currentPlayer.getBoard().getStorage().getCupboard().getShelfById(shelfID);
        }catch(NoSuchElementException e){
            throw new IllegalActionException(e.getMessage());
        }

        try{
            currentPlayer.getBoard().getStorage().getCupboard().addResourceFromContainer(
                    currentPlayer.getBoard().getStorage().getMarketBasket(),
                    shelf, resourceToMove, amount
            );
        }catch(IllegalCupboardException e){
            throw new IllegalActionException(e.getMessage());
        }

        List<String> destinations = model.getPlayers()
                .stream()
                .map(Player::getUsername)
                .collect(Collectors.toList());


        List<PayloadComponent> payload = new ArrayList<>();

        payload.add(PayloadFactory.changeResources(currentPlayer.getUsername(),
                new RawStorage(currentPlayer.getBoard().getStorage().getMarketBasket().getId(), new HashMap<>(){{
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
        if(player == null || resourceToMove == null || shelfID == null)
            throw new NullPointerException();

        if(amount <= 0)
            throw new IllegalArgumentException("Amount cannot be negative or zero");
    }

    /**
     * returns the resource to move.
     * @return the resource to move
     */
    public ResourceSingle getResourceToMove() {
        return resourceToMove;
    }

    /**
     * returns the amount of resources to move.
     * @return the amount of resources to move
     */
    public int getAmount() {
        return amount;
    }

    /**
     * returns the ID of the shelf to move the resource to.
     * @return the ID of the destination shelf
     */
    public String getShelfID() {
        return shelfID;
    }
}
