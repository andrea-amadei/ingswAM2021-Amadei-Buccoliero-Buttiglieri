package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.common.InfoPayload;
import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.PayloadComponent;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.exceptions.IllegalSelectionException;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.fsm.ActionHandler;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.storage.ResourceContainer;
import it.polimi.ingsw.model.storage.Storage;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

public class SelectResourcesAction implements Action{

    private final String player;
    private final String containerId;
    private final ResourceSingle resource;
    private final int amount;

    /**
     * Creates a new SelectResourcesAction with the specified parameters
     * @param player the player that wants to select a resource
     * @param containerId the container from which the resource is selected
     * @param resource the ResourceSingle selected
     * @param amount the amount of resources selected
     * @throws NullPointerException if one among player, containerId and resource is null
     * @throws IllegalArgumentException if amount <= 0
     */
    public SelectResourcesAction(String player, String containerId, ResourceSingle resource, int amount){
        if(player == null || containerId == null || resource == null)
            throw new NullPointerException();
        if(amount <= 0)
            throw new IllegalArgumentException("Amount must be positive");

        this.player = player;
        this.containerId = containerId;
        this.resource = resource;
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
     * Adds the specified resources to the current selection. The action cannot be performed if the selected resources
     * are not contained in the specified ResourceContainer
     *
     * @param gameContext the current context of the game
     * @return a list of messages that contains information about the change applied to the model
     * @throws IllegalActionException if the action cannot be performed on the model
     * @throws NullPointerException   if gameContext is null
     */
    @Override
    public List<Message> execute(GameContext gameContext) throws IllegalActionException {
        if(gameContext == null)
            throw new NullPointerException();

        Player currentPlayer;
        GameModel model = gameContext.getGameModel();

        try{
            currentPlayer = model.getPlayerById(player);
        }catch(NoSuchElementException e ){
            throw new IllegalActionException(e.getMessage());
        }

        Storage storage = currentPlayer.getBoard().getStorage();

        ResourceContainer container;
        try {
            container = storage.getResourceContainerById(containerId);
        }catch(NoSuchElementException e){
            throw new IllegalActionException(e.getMessage());
        }

        try{
            storage.addToSelection(container, resource, amount);
        }catch(IllegalSelectionException e){
            throw new IllegalActionException(e.getMessage());
        }

        //building the message
        List<String> targets = Collections.singletonList(player);
        PayloadComponent payloadComponent = new InfoPayload(player + " has selected "
                                                            + amount + " resources of type"
                                                            + resource + " from "
                                                            + container);

        Message message = new Message(targets, Collections.singletonList(payloadComponent));
        return Collections.singletonList(message);
    }
}
