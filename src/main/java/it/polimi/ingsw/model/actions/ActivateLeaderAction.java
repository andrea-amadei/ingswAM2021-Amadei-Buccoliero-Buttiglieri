package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.exceptions.AlreadyActiveException;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.exceptions.RequirementsNotSatisfiedException;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.fsm.ActionHandler;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.leader.LeaderCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class ActivateLeaderAction implements Action{

    private final String player;
    private final int leaderID;

    /**
     * Activate Leader action constructor
     * @param player the player who activate the leader
     * @param leaderID the ID number of the leader card to activate
     * @throws NullPointerException iff pointer to player is null
     * @throws IllegalArgumentException iff leader ID is negative or zero
     */
    public ActivateLeaderAction(String player, int leaderID){
        this.player = player;
        this.leaderID = leaderID;
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
        List<PayloadComponent> payload;

        if(!player.equals(gameContext.getCurrentPlayer().getUsername()))
            throw new IllegalActionException("It is not your turn");

        currentPlayer = gameContext.getCurrentPlayer();

        try {
            leaderCard = currentPlayer.getBoard().getLeaderCardByID(leaderID);
        }catch(NoSuchElementException e){
            throw new IllegalActionException(e.getMessage());
        }

        try {
            payload = new ArrayList<>(leaderCard.activate(currentPlayer));
        }catch(NoSuchElementException | RequirementsNotSatisfiedException | AlreadyActiveException e){
            throw new IllegalActionException(e.getMessage());
        }

        List<String> destinations = model.getPlayers()
                .stream()
                .map(Player::getUsername)
                .collect(Collectors.toList());

        return Collections.singletonList(new Message(destinations, payload));
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

    @Override
    public void checkFormat() {
        if(player == null)
            throw new NullPointerException();
        if(leaderID <= 0)
            throw new IllegalArgumentException("Leader ID cannot be negative or zero");
    }

    public int getLeaderID() {
        return leaderID;
    }
}
