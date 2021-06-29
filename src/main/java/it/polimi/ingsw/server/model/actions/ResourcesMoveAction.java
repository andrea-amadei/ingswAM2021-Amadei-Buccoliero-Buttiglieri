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
import it.polimi.ingsw.server.model.storage.ResourceContainer;
import it.polimi.ingsw.server.model.storage.Shelf;
import it.polimi.ingsw.server.model.storage.Storage;
import it.polimi.ingsw.common.parser.raw.RawStorage;
import it.polimi.ingsw.common.utils.PayloadFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class implements interface Action. This action transfers resources from origin to destination. If transfer cannot
 * be applied, nothing changes.
 * Origin and destination can only be the player's hand or shelves.
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
        this.player = player;
        this.origin = origin;
        this.destination = destination;
        this.resourceToMove = resourceToMove;
        this.amount = amount;
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

        if(!player.equals(gameContext.getCurrentPlayer().getUsername()))
            throw new IllegalActionException("It is not your turn");

        currentPlayer = gameContext.getCurrentPlayer();


        storage = currentPlayer.getBoard().getStorage();


        String correctOrigin;
        String correctDestination;


        //if the origin is the hand
        if(origin.equalsIgnoreCase(currentPlayer.getBoard().getStorage().getHand().getId())){
            correctOrigin = currentPlayer.getBoard().getStorage().getHand().getId();

            //using the correct case for origin
            Shelf destinationShelf;
            try {
                destinationShelf = storage.getCupboard().getShelfById(destination);
                correctDestination = storage.getCupboard().getShelfById(destination).getId();
            }catch (NoSuchElementException e){
                throw new IllegalActionException(e.getMessage());
            }
            try{
                storage.getCupboard().addResourceFromContainer(storage.getHand(), destinationShelf, resourceToMove, amount);
            }catch(IllegalCupboardException e){
                throw new IllegalActionException(e.getMessage());
            }
        }

        //if the destination is the hand and the origin is a shelf
        else if(destination.equalsIgnoreCase(currentPlayer.getBoard().getStorage().getHand().getId())){
            correctDestination = currentPlayer.getBoard().getStorage().getHand().getId();

            Shelf originShelf;
            ResourceContainer hand = storage.getHand();
            try {
                originShelf = storage.getCupboard().getShelfById(origin);
                correctOrigin = storage.getCupboard().getShelfById(origin).getId();
            }catch (NoSuchElementException e){
                throw new IllegalActionException(e.getMessage());
            }
            try{
                storage.getCupboard().moveResourceToContainer(hand, originShelf, resourceToMove, amount);
            }catch(IllegalCupboardException e){
                throw new IllegalActionException(e.getMessage());
            }
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

            if(originShelf.getCurrentType() == null || !originShelf.getCurrentType().equals(resourceToMove)){
                throw new IllegalActionException("Cannot move resources");
            }

            correctOrigin = storage.getCupboard().getShelfById(origin).getId();
            correctDestination = storage.getCupboard().getShelfById(destination).getId();

            try{
                currentPlayer.getBoard().getStorage().getCupboard().moveBetweenShelves(originShelf, destinationShelf, amount);
            }catch(IllegalCupboardException e){
                throw new IllegalActionException("cannot move resources between these shelves");
            }

        }

        List<PayloadComponent> payload = new ArrayList<>();
        List<String> allPlayers = model.getPlayers()
                .stream()
                .map(Player::getUsername)
                .collect(Collectors.toList());

        //removing from origin payload
        payload.add(PayloadFactory.changeResources(
                currentPlayer.getUsername(),
                new RawStorage(correctOrigin,
                        new HashMap<>(){{
                            put(resourceToMove.toString().toLowerCase(), -amount);
                        }})
        ));
        payload.add(PayloadFactory.changeResources(
                currentPlayer.getUsername(),
                new RawStorage(correctDestination,
                        new HashMap<>(){{
                            put(resourceToMove.toString().toLowerCase(), amount);
                        }})
        ));

        Message message = new Message(allPlayers, payload);
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
        if(player == null ||origin == null || destination == null || resourceToMove == null)
            throw new NullPointerException();
        if(amount<=0)
            throw new IllegalArgumentException("Amount of resources to move cannot be negative or zero");
    }

    /**
     * returns the origin storage.
     * @return the origin storage.
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * returns the destination storage.
     * @return the destination storage.
     */
    public String getDestination() {
        return destination;
    }

    /**
     * returns the resources to move.
     * @return the resources to move.
     */
    public ResourceSingle getResourceToMove() {
        return resourceToMove;
    }

    /**
     * returns the amount of resources to move.
     * @return the amount of resources to move.
     */
    public int getAmount() {
        return amount;
    }
}
