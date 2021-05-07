package it.polimi.ingsw.server.clienthandling;

import it.polimi.ingsw.common.ActionQueue;
import it.polimi.ingsw.exceptions.DuplicateUsernameException;
import it.polimi.ingsw.exceptions.GameNotInLobbyException;
import it.polimi.ingsw.exceptions.GameNotReadyException;
import it.polimi.ingsw.exceptions.GameNotStartedException;
import it.polimi.ingsw.model.fsm.StateMachine;
import it.polimi.ingsw.server.DummyBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class contains all information about the match.
 * It also makes sure that the actionQueue and the stateMachine are available
 * to the client handlers and to the controller handler
 */
public class Match {

    public enum MatchState{LOBBY, READY, PLAYING, ENDED}

    private StateMachine stateMachine;
    private final ActionQueue actionQueue;

    private final List<String> usernames;
    private final String gameName;
    private final int matchSize;
    private final boolean isSinglePlayer;

    private MatchState currentState;

    //TODO: add json files sent by the host client


    //TODO: we may also need to add the socket instances here

    public Match(String gameName, String hostUsername, int matchSize, boolean isSinglePlayer){

        //TODO: add checks for gameName etc...

        stateMachine = null;
        actionQueue = new ActionQueue();

        usernames = new ArrayList<>();
        usernames.add(hostUsername);

        this.gameName = gameName;
        this.matchSize = matchSize;
        this.isSinglePlayer = isSinglePlayer;
        this.currentState = MatchState.LOBBY;

    }

    public void addPlayer(String username) throws DuplicateUsernameException, GameNotInLobbyException {
        if(username == null)
            throw new NullPointerException();

        if(!currentState.equals(MatchState.LOBBY))
            throw new GameNotInLobbyException("Cannot connect to the game, it is not in lobby anymore");
        if(usernames.contains(username))
            throw new DuplicateUsernameException("Another player chose the same inserted username");

        usernames.add(username);
        if(usernames.size() == matchSize){
            currentState = MatchState.READY;
        }
    }

    public void startGame() throws GameNotReadyException {
        if(!currentState.equals(MatchState.READY))
            throw new GameNotReadyException("The game is not ready");

        currentState = MatchState.PLAYING;

        //TODO: use the real builder to build the state machine (it automatically creates the game model and game
        //      context. Launch a new thread that will handle the queue consumption
        this.stateMachine = DummyBuilder.buildController(usernames, new Random(), isSinglePlayer, actionQueue);

    }

    public void endGame() throws GameNotStartedException{
        if(!currentState.equals(MatchState.PLAYING))
            throw new GameNotStartedException("Can't terminate the game if it hasn't started");

        currentState = MatchState.ENDED;
    }

    public String getGameName() {
        return gameName;
    }

    public StateMachine getStateMachine(){
        return stateMachine;
    }

    public ActionQueue getActionQueue(){
        return actionQueue;
    }

    public List<String> getUsernames() {
        return new ArrayList<>(usernames);
    }
    public int getMatchSize() {
        return matchSize;
    }

    public boolean isSinglePlayer() {
        return isSinglePlayer;
    }

    public MatchState getCurrentState() {
        return currentState;
    }
}
