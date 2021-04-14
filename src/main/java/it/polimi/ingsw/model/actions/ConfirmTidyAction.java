package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.common.InfoPayload;
import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.fsm.GameContext;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

public class ConfirmTidyAction implements Action{


    private final String player;

    /**
     * Creates a new ConfirmedTidyAction with the specified username
     * @param player the username of the player that is performing this action
     * @throws NullPointerException if player is null
     */
    public ConfirmTidyAction(String player){
        if(player == null)
            throw new NullPointerException();
        this.player = player;
    }
    /**
     * The player confirms that he/she has the desired storage configuration.
     * After this action is performed, the player cannot tidy his storage and it will only be possible to perform the
     * next action.
     * In order to be performed, the storage of the player must be in a valid configuration (player's hand must be empty)
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

        try {
            currentPlayer = model.getPlayerById(player);
        }catch(NoSuchElementException e){
            throw new IllegalActionException(e.getMessage());
        }

        if(currentPlayer.getBoard().getStorage().getHand().totalAmountOfResources() > 0)
            throw new IllegalActionException("Trying to confirm tidy, but hand is not empty");

        return Collections.singletonList(new Message(Collections.singletonList(player), Collections.singletonList(
                new InfoPayload(player + " " + "has confirmed tidy"))));
    }
}
