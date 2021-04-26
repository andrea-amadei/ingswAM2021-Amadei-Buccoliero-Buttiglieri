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
import it.polimi.ingsw.model.leader.LeaderCard;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class DiscardLeaderAction implements Action{

    private final String player;
    private final int leaderID;

    /**
     * Activate Leader action constructor
     * @param player the player who discards the leader
     * @param leaderID the ID number of the leader card to discard
     * @throws NullPointerException iff pointer to player is null
     * @throws IllegalArgumentException iff leader ID is negative or zero
     */
    public DiscardLeaderAction(String player, int leaderID){
        if(player == null)
            throw new NullPointerException();
        if( leaderID <= 0)
            throw new IllegalArgumentException("Leader ID cannot be negative or zero");

        this.player = player;
        this.leaderID = leaderID;
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
     * method executes the action
     * @param gameContext the current context of the game
     * @return the list of messages with info about the changes to the game
     * @throws IllegalActionException iff the action cannot be performed
     * @throws NullPointerException iff pointer to gameContext is null
     */
    @Override
    public List<Message> execute(GameContext gameContext) throws IllegalActionException {

        if(gameContext == null)
            throw new NullPointerException();

        GameModel model = gameContext.getGameModel();
        Player currentPlayer;
        LeaderCard leaderCard;

        try {
            currentPlayer = model.getPlayerById(player);
        }catch(NoSuchElementException e){
            throw new IllegalActionException(e.getMessage());
        }

        try {
            leaderCard = currentPlayer.getBoard().getLeaderCardByID(leaderID);
        }catch(NoSuchElementException e){
            throw new IllegalActionException(e.getMessage());
        }

        try {
            currentPlayer.getBoard().getLeaderCards().remove(leaderCard);
        }catch(NoSuchElementException e){
            throw new IllegalActionException(e.getMessage());
        }

        gameContext.getGameModel().getFaithPath().executeMovement(1, currentPlayer);

        List<String> destinations = model.getPlayers()
                .stream()
                .map(Player::getUsername)
                .collect(Collectors.toList());

        PayloadComponent payload = new InfoPayload( currentPlayer.getUsername() +
                " has discarded leader "
                + leaderCard.getName()) ;

        return Collections.singletonList(new Message(destinations, Collections.singletonList(payload)));

    }
}
