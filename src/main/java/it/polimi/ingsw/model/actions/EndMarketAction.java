package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.InfoPayloadComponent;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.FaithPath;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.fsm.ActionHandler;
import it.polimi.ingsw.model.fsm.GameContext;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Deprecated
public class EndMarketAction implements Action{

    private final String player;

    /**
     * Creates a new EndMarketAction. For its purpose see the method execute
     * @param player the username of the player that wants to end his market turn
     * @throws NullPointerException if player is null
     */
    public EndMarketAction(String player){
        if(player == null)
            throw new NullPointerException();
        this.player = player;
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
     * The player confirms that his market action is ended. All remaining resources in the market basket are discarded
     * and a faith point is added to all the other players for each discarded resource
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
        List<Player> otherPlayers;
        GameModel model = gameContext.getGameModel();
        FaithPath faithPath = model.getFaithPath();

        try{
            currentPlayer = model.getPlayerById(player);
        }catch(NoSuchElementException e){
            throw new IllegalActionException(e.getMessage());
        }

        otherPlayers = model.getPlayers().stream()
                                         .filter(x->!x.getUsername().equals(currentPlayer.getUsername()))
                                         .collect(Collectors.toList());

        int droppedResources = currentPlayer.getBoard().getStorage().getMarketBasket().totalAmountOfResources();

        if(droppedResources > 0) {
            currentPlayer.getBoard().getStorage().getMarketBasket().reset();

            for (Player p : otherPlayers)
                faithPath.executeMovement(droppedResources, p);
        }

        //build the message
        List<String> targets = model.getPlayers().stream().map(Player::getUsername).collect(Collectors.toList());
        PayloadComponent payload = new InfoPayloadComponent("Everyone got "+droppedResources+" faith point/s and "
                                                 +  player + " "
                                                 + "has discarded all resources in the market basket");

        Message message = new Message(targets, Collections.singletonList(payload));

        return Collections.singletonList(message);
    }
}
