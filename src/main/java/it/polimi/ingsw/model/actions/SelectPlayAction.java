package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.common.InfoPayload;
import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.PayloadComponent;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.fsm.ActionHandler;
import it.polimi.ingsw.model.fsm.GameContext;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class SelectPlayAction implements Action {

    public enum Play {
        MARKET,
        CRAFTING,
        SHOP
    }

    private final Play play;
    private final String player;

    /**
     * Creates a new SelectPlayAction. It is used to select which path to take during any turn:
     * <ul>
     *     <li>Buy a card from the shop</li>
     *     <li>Take marbles from the market</li>
     *     <li>Activate one or more crafting</li>
     * </ul>
     *
     * @param player the username of the player that selected the play
     * @param play   the selected play
     * @throws NullPointerException if player is null
     */
    public SelectPlayAction(String player, Play play) {
        if(player == null || play == null)
            throw new NullPointerException();

        this.play = play;
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
     * The player requests to make a certain play. All available actions change consequently.
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
        }catch(NoSuchElementException e){
            throw new IllegalActionException(e.getMessage());
        }

        if(!currentPlayer.equals(gameContext.getCurrentPlayer()))
            throw new IllegalActionException("The current player doesn't match the executor player");

        List<String> targets = model.getPlayers().stream().map(Player::getUsername).collect(Collectors.toList());
        PayloadComponent payload = new InfoPayload("Player " + player + " has chosen to " + play);
        Message message = new Message(targets, Collections.singletonList(payload));
        return Collections.singletonList(message);
    }

    public Play getPlay() {
        return play;
    }
}
