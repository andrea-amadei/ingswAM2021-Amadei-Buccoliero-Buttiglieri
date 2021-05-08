package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.InfoPayloadComponent;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.exceptions.IllegalCupboardException;
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
 * action moves resources between a player's shelves
 */
@Deprecated(forRemoval = true)
public class MoveFromShelfToShelfAction implements Action{

    private final String player;
    private final ResourceSingle resourceToMove;
    private final int amount;
    private final String formerShelfID;
    private final String destinationShelfID;

    /**
     * MoveFromShelfToShelf action constructor
     * @param player the player performing the action
     * @param resourceToMove the resource that is being moved
     * @param amount the amount of said resource
     * @param formerShelfID the ID of the shelf the resource is being removed from
     * @param destinationShelfID the ID of the self the resource is being moved to
     * @throws NullPointerException iff pointer to player, resource to move, or one of the shelves is null
     * @throws IllegalArgumentException iff amount of resources to move is negative or zero
     */
    public MoveFromShelfToShelfAction(String player, ResourceSingle resourceToMove, int amount, String formerShelfID, String destinationShelfID){
        this.player = player;
        this.resourceToMove = resourceToMove;
        this.amount = amount;
        this.formerShelfID = formerShelfID;
        this.destinationShelfID = destinationShelfID;
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
     * @throws NullPointerException iff pointer to gameContext is null
     */
    @Override
    public List<Message> execute(GameContext gameContext) throws IllegalActionException {

        if(gameContext == null)
            throw new NullPointerException();

        GameModel model = gameContext.getGameModel();
        Player currentPlayer;
        Shelf formerShelf, destinationShelf;

        try {
            currentPlayer = model.getPlayerById(player);
        }catch(NoSuchElementException e){
            throw new IllegalActionException(e.getMessage());
        }

        try {
            formerShelf = currentPlayer.getBoard().getStorage().getCupboard().getShelfById(formerShelfID);
        }catch(NoSuchElementException e){
            throw new IllegalActionException(e.getMessage());
        }

        try {
            destinationShelf = currentPlayer.getBoard().getStorage().getCupboard().getShelfById(destinationShelfID);
        }catch(NoSuchElementException e){
            throw new IllegalActionException(e.getMessage());
        }

        try{
            currentPlayer.getBoard().getStorage().getCupboard().moveBetweenShelves(formerShelf, destinationShelf, amount);
        }catch(IllegalCupboardException e){
            throw new IllegalActionException(e.getMessage());
        }

        List<String> destinations = model.getPlayers()
                .stream()
                .map(Player::getUsername)
                .collect(Collectors.toList());

        PayloadComponent payload = new InfoPayloadComponent(amount + " of "
                + resourceToMove
                + " have been moved from "
                + currentPlayer.getUsername() + "'s "
                + formerShelf.getId()+ " to their " + destinationShelf.getId()) ;

        return Collections.singletonList(new Message(destinations, Collections.singletonList(payload)));
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
        if(player == null || resourceToMove == null || formerShelfID == null || destinationShelfID == null)
            throw new NullPointerException();

        if(amount <= 0)
            throw new IllegalArgumentException("Amount cannot be negative or zero");
    }
}
