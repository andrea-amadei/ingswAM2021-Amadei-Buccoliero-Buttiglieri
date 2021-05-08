package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.fsm.ActionHandler;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.leader.LeaderCard;
import it.polimi.ingsw.utils.PayloadFactory;

import java.util.*;
import java.util.stream.Collectors;

public class DiscardLeaderAction implements Action{

    private final String player;
    private final int leaderID;

    /**
     * Discard Leader action constructor
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
     * The player discards a leader card and gets the faith points
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

        List<PayloadComponent> faithUpdates = new ArrayList<>(gameContext.getGameModel().getFaithPath().executeMovement(1, currentPlayer));
        PayloadComponent coveredCardUpdate = PayloadFactory.changeCoveredLeaderCard(currentPlayer.getUsername(), -1);
        PayloadComponent dropCardUpdate = PayloadFactory.discardLeaderCard(currentPlayer.getUsername(), leaderID);

        //List of all usernames except the current player
        List<String> otherPlayersDestination = model.getPlayers()
                .stream()
                .map(Player::getUsername)
                .filter(name -> !name.equals(currentPlayer.getUsername()))
                .collect(Collectors.toList());

        List<String> allDestinations = new ArrayList<>(otherPlayersDestination);
        allDestinations.add(currentPlayer.getUsername());

        return Arrays.asList(
                new Message(allDestinations, faithUpdates),
                new Message(otherPlayersDestination, Collections.singletonList(coveredCardUpdate)),
                new Message(Collections.singletonList(currentPlayer.getUsername()), Collections.singletonList(dropCardUpdate))
        );
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
}
