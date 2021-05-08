package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.fsm.ActionHandler;
import it.polimi.ingsw.model.fsm.GameContext;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
        this.play = play;
        this.player = player;
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

        //TODO: choose how to communicate the selected play
        return new ArrayList<>();
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
        if(player == null || play == null)
            throw new NullPointerException();

    }

    public Play getPlay() {
        return play;
    }
}
