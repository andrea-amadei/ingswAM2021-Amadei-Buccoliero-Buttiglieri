package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
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
import it.polimi.ingsw.utils.PayloadFactory;

import java.util.ArrayList;
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
        this.player = player;
        this.containerId = containerId;
        this.resource = resource;
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
            container = storage.getSpendableResourceContainerById(containerId);
        }catch(NoSuchElementException e){
            throw new IllegalActionException(e.getMessage());
        }

        try{
            storage.addToSelection(container, resource, amount);
        }catch(IllegalSelectionException e){
            throw new IllegalActionException(e.getMessage());
        }

        //building the message
        List<String> targets = gameContext.getGameModel().getPlayerNames();
        List<PayloadComponent> payload = new ArrayList<>();
        payload.add(PayloadFactory
                .selectedResource(currentPlayer.getUsername(), container.getId(), resource.getId().toLowerCase(), amount));

        Message message = new Message(targets, payload);
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
        if(player == null || containerId == null || resource == null)
            throw new NullPointerException();
        if(amount <= 0)
            throw new IllegalArgumentException("Amount must be positive");
    }
}
