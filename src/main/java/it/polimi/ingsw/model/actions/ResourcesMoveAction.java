package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.common.InfoPayload;
import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.PayloadComponent;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.exceptions.IllegalCupboardException;
import it.polimi.ingsw.exceptions.IllegalResourceTransferException;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.fsm.ActionHandler;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.storage.ResourceContainer;
import it.polimi.ingsw.model.storage.Shelf;
import it.polimi.ingsw.model.storage.Storage;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Transfers resources from origin to destination. If transfer cannot be applied, nothing changes.
 * Origin and destination can only be hand or shelves.
 */
public class ResourcesMoveAction implements Action{

    private final String player;
    private final String origin;
    private final String destination;
    private final ResourceSingle resourceToMove;
    private final int amount;

    /**
     * ResourcesMoveAction constructor
     * @param player the player performing this action
     * @param origin the resource container from which resources are taken
     * @param destination the resource container to which resources are moved
     * @param resourceToMove the resource to move
     * @param amount the amount of said resource to move
     * @throws NullPointerException iff at least one among player, origin, destination or resourceToMove is null
     * @throws IllegalArgumentException iff amount of resources to move is negative or zero
     */
    public ResourcesMoveAction (String player, String origin, String destination, ResourceSingle resourceToMove, int amount){
        if(player == null ||origin == null || destination == null || resourceToMove == null)
            throw new NullPointerException();
        if(amount<=0)
            throw new IllegalArgumentException("Amount of resources to move cannot be negative or zero");

        this.player = player;
        this.origin = origin;
        this.destination = destination;
        this.resourceToMove = resourceToMove;
        this.amount = amount;
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
     * Transfers resources from origin to destination. If transfer cannot be applied, nothing changes.
     * Origin and destination can only be hand or shelves.
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
        Player currentPlayer;
        Storage storage;

        try {
            currentPlayer = model.getPlayerById(player);
        }catch(NoSuchElementException e){
            throw new IllegalActionException(e.getMessage());
        }
        storage = currentPlayer.getBoard().getStorage();

        List<String> destinations = model.getPlayers()
                .stream()
                .map(Player::getUsername)
                .collect(Collectors.toList());

        //TODO: add ID to chest, hand, ecc...
        if(origin.equals("Hand")){
            Shelf destinationShelf;
            try {
                destinationShelf = storage.getCupboard().getShelfById(destination);
            }catch (NoSuchElementException e){
                throw new IllegalActionException(e.getMessage());
            }
            try{
                storage.getHand().moveTo(destinationShelf, resourceToMove, amount);
            }catch(IllegalResourceTransferException e){
                throw new IllegalActionException(e.getMessage());
            }
            PayloadComponent payload = new InfoPayload(amount + " of "
                    + resourceToMove
                    + " have been moved from "
                    + currentPlayer.getUsername() + "'s hand to "
                    + destinationShelf.getId());

            return Collections.singletonList(new Message(destinations, Collections.singletonList(payload)));
        }

        else if(destination.equals("Hand")){
            Shelf originShelf;
            ResourceContainer hand = storage.getHand();
            try {
                originShelf = storage.getCupboard().getShelfById(origin);
            }catch (NoSuchElementException e){
                throw new IllegalActionException(e.getMessage());
            }
            try{
                storage.getCupboard().moveResourceToContainer(hand, originShelf, resourceToMove, amount);
            }catch(IllegalCupboardException e){
                throw new IllegalActionException(e.getMessage());
            }
            PayloadComponent payload = new InfoPayload(amount + " of " + resourceToMove
                    + " have been moved from "
                    + currentPlayer.getUsername() + "'s " + originShelf.getId() + " to their hand");

            return Collections.singletonList(new Message(destinations, Collections.singletonList(payload)));
        }

        else{
            Shelf originShelf;
            Shelf destinationShelf;
            try {
                originShelf = storage.getCupboard().getShelfById(origin);
            }catch (NoSuchElementException e){
                throw new IllegalActionException(e.getMessage());
            }
            try {
                destinationShelf = storage.getCupboard().getShelfById(destination);
            }catch (NoSuchElementException e){
                throw new IllegalActionException(e.getMessage());
            }
            try{
                currentPlayer.getBoard().getStorage().getCupboard().moveBetweenShelves(originShelf, destinationShelf, amount);
            }catch(IllegalCupboardException e){
                throw new IllegalActionException(e.getMessage());
            }

            PayloadComponent payload = new InfoPayload(amount + " of "
                    + resourceToMove
                    + " have been moved from "
                    + currentPlayer.getUsername() + "'s "
                    + originShelf.getId()+ " to their " + destinationShelf.getId()) ;

            return Collections.singletonList(new Message(destinations, Collections.singletonList(payload)));
        }


    }
}
