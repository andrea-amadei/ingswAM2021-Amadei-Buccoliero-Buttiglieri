package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.common.InfoPayload;
import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.PayloadComponent;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.fsm.GameContext;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class ConfirmAction implements Action {

    private final String player;

    /**
     * Creates a new ConfirmAction. It is used to confirm a change or a previous action
     * @param player the username of the player that wants to confirm
     * @throws NullPointerException if player is null
     */
    public ConfirmAction(String player){
        if(player == null)
            throw new NullPointerException();

        this.player = player;
    }

    /**
     * The player requests to go confirm a pending action. It is used to confirm a purchase in the shop
     * or market and to confirm all resources have benn picked while crafting.
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
        PayloadComponent payload = new InfoPayload("Player " + player + " confirmed");
        Message message = new Message(targets, Collections.singletonList(payload));
        return Collections.singletonList(message);
    }
}
