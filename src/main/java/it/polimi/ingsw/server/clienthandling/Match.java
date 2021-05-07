package it.polimi.ingsw.server.clienthandling;

import it.polimi.ingsw.common.ActionQueue;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.exceptions.DuplicateUsernameException;
import it.polimi.ingsw.exceptions.GameNotInLobbyException;
import it.polimi.ingsw.exceptions.GameNotReadyException;
import it.polimi.ingsw.exceptions.GameNotStartedException;
import it.polimi.ingsw.model.fsm.StateMachine;
import it.polimi.ingsw.server.DummyBuilder;
import it.polimi.ingsw.utils.Pair;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class contains all information about the match.
 * It also makes sure that the actionQueue and the stateMachine are available to the client handlers and vice versa
 */
public class Match {

    public enum MatchState{LOBBY, READY, PLAYING, ENDED}

    private StateMachine stateMachine;
    private final ActionQueue actionQueue;
    private final ClientHub clientHub;

    private final String gameName;
    private final int matchSize;
    private final boolean isSinglePlayer;

    private MatchState currentState;

    //TODO: add json files sent by the host client



    public Match(String gameName, Pair<String, Socket> host, int matchSize, boolean isSinglePlayer){

        //TODO: add checks for gameName etc...

        stateMachine = null;
        actionQueue = new ActionQueue();

        clientHub = new ClientHub();
        clientHub.addClient(host.getFirst(), host.getSecond());

        this.gameName = gameName;
        this.matchSize = matchSize;
        this.isSinglePlayer = isSinglePlayer;
        this.currentState = MatchState.LOBBY;

    }

    public void addPlayer(Pair<String, Socket> client) throws DuplicateUsernameException, GameNotInLobbyException {
        if(client == null)
            throw new NullPointerException();

        if(!currentState.equals(MatchState.LOBBY))
            throw new GameNotInLobbyException("Cannot connect to the game, it is not in lobby anymore");
        if(clientHub.contains(client.getFirst()))
            throw new DuplicateUsernameException("Another player chose the same inserted username");

        clientHub.addClient(client.getFirst(), client.getSecond());
        if(clientHub.size() == matchSize){
            currentState = MatchState.READY;
        }
    }

    public void startGame() throws GameNotReadyException {
        if(!currentState.equals(MatchState.READY))
            throw new GameNotReadyException("The game is not ready");

        currentState = MatchState.PLAYING;

        //TODO: use the real builder to build the state machine (it automatically creates the game model and game
        //      context. Launch a new thread that will handle the queue consumption
        this.stateMachine = DummyBuilder.buildController(clientHub.getUsernames(), new Random(), isSinglePlayer, actionQueue);
        new Controller(stateMachine, actionQueue, clientHub).start();
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
        return clientHub.getUsernames();
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
