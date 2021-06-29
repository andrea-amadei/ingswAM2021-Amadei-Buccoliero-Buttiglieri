package it.polimi.ingsw.server.model.actions;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.InfoPayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.updates.ChangePossibleConversionsUpdatePayloadComponent;
import it.polimi.ingsw.common.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.common.exceptions.IllegalActionException;
import it.polimi.ingsw.server.model.basetypes.ResourceSingle;
import it.polimi.ingsw.server.model.global.GameModel;
import it.polimi.ingsw.server.model.global.Player;
import it.polimi.ingsw.server.model.global.Shop;
import it.polimi.ingsw.server.model.fsm.ActionHandler;
import it.polimi.ingsw.server.model.fsm.GameContext;
import it.polimi.ingsw.server.model.production.Production;
import it.polimi.ingsw.server.model.storage.Storage;
import it.polimi.ingsw.common.parser.raw.RawStorage;
import it.polimi.ingsw.common.utils.PayloadFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class implements interface Action. This action notifies the disconnection of a player and makes the right changes
 * to the model.
 */
public class DisconnectPlayerAction implements Action{

    private final String target;

    public DisconnectPlayerAction(String target){
        this.target = target;
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
     * Executes the action on the provided game context
     *
     * @param gameContext the current context of the game
     * @return a list of messages that contains information about the change applied to the model
     * @throws IllegalActionException if the action cannot be performed on the model
     * @throws NullPointerException   if gameContext is null
     */
    @Override
    public List<Message> execute(GameContext gameContext) throws IllegalActionException {
        List<PayloadComponent> payload = new ArrayList<>();

        if(target.equals(gameContext.getCurrentPlayer().getUsername())) {
            GameModel model = gameContext.getGameModel();
            Player currentPlayer = gameContext.getCurrentPlayer();

            Production production = currentPlayer.getBoard().getProduction();
            Shop shop = model.getShop();
            Storage storage = currentPlayer.getBoard().getStorage();

            //Sending payload to reset possible conversions
            payload.add(new ChangePossibleConversionsUpdatePayloadComponent(target, new ArrayList<>(), new ArrayList<>()));

            //Resetting shop
            shop.resetSelectedCard();
            payload.add(PayloadFactory.unselect(currentPlayer.getUsername(), "shop"));

            //Resetting storage
            storage.resetSelection();
            payload.add(PayloadFactory.unselect(currentPlayer.getUsername(), "storage"));

            //Emptying market basket if not already empty
            if(storage.getMarketBasket().totalAmountOfResources() > 0) {
                Map<ResourceSingle, Integer> resourcesInMarketBasket = storage.getMarketBasket().getAllResources();
                Map<String, Integer> resourcesToRemove =
                        resourcesInMarketBasket.entrySet()
                                               .stream()
                                               .collect(Collectors.toMap(e -> e.getKey().getId(), e -> -e.getValue()));

                storage.getMarketBasket().reset();
                payload.add(PayloadFactory.changeResources(currentPlayer.getUsername(),
                        new RawStorage(storage.getMarketBasket().getId(), resourcesToRemove)));
            }

            //Reset selected output(model only) and crafting status
            payload.addAll(production.craftingTotalReset(currentPlayer));


        }
        gameContext.getGameModel().getPlayerById(target).setConnected(false);
        payload.add(new InfoPayloadComponent("Player \"" + target + "\" left the game"));


        return Collections.singletonList(new Message(gameContext.getGameModel().getPlayerNames(),
                payload));
    }

    /**
     * Returns the sender of this action
     *
     * @return the sender of this action
     */
    @Override
    public String getSender() {
        return "AI";
    }

    /**
     * Checks if all attributes are set and have meaningful values.
     * In case they are not, this throws the appropriate RuntimeException.
     * It needs to be used since this class can be created by deserialization
     */
    @Override
    public void checkFormat() {

    }

    /**
     * returns the target of the disconnection
     * @return the target of the disconnection
     */
    public String getTarget() {
        return target;
    }
}
