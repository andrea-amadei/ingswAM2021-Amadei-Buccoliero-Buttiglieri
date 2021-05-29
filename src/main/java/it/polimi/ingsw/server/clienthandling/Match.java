package it.polimi.ingsw.server.clienthandling;

import it.polimi.ingsw.common.ActionQueue;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.fsm.StateMachine;
import it.polimi.ingsw.server.DummyBuilder;
import it.polimi.ingsw.server.ServerBuilder;
import it.polimi.ingsw.utils.Pair;

import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
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



    public Match(String gameName, Pair<String, ClientHandler> host, int matchSize, boolean isSinglePlayer){

        //TODO: add checks for gameName etc...

        stateMachine = null;
        actionQueue = new ActionQueue();

        clientHub = new ClientHub();

        this.gameName = gameName;
        this.matchSize = matchSize;
        this.isSinglePlayer = isSinglePlayer;

        this.currentState = MatchState.LOBBY;
        try {
            addPlayer(host);
        } catch (DuplicateUsernameException | GameNotInLobbyException e) {
            e.printStackTrace();
        }




    }

    public synchronized void addPlayer(Pair<String, ClientHandler> client) throws DuplicateUsernameException, GameNotInLobbyException {
        if(client == null)
            throw new NullPointerException();

        if(!currentState.equals(MatchState.LOBBY))
            throw new GameNotInLobbyException("Cannot connect to the game, it is not in lobby anymore");
        if(clientHub.contains(client.getFirst()))
            throw new DuplicateUsernameException("Another player chose the same inserted username");

        clientHub.addClient(client.getFirst(), client.getSecond());
        if(clientHub.size() == matchSize){
            currentState = MatchState.READY;
            try {
                startGame();
            } catch (GameNotReadyException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void startGame() throws GameNotReadyException {
        if(!currentState.equals(MatchState.READY))
            throw new GameNotReadyException("The game is not ready");

        currentState = MatchState.PLAYING;

        //TODO: we will change how these files are passed (also exceptions)
        String config;
        String crafting;
        String faith;
        String leaders;

        try {
            config = Files.readString(Path.of("src/main/config.json"));
            crafting = Files.readString(Path.of("src/main/crafting.json"));
            faith = Files.readString(Path.of("src/main/faith.json"));
            leaders = Files.readString(Path.of("src/main/leaders.json"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot read from json files while starting the game");
        }
        try {
            this.stateMachine = ServerBuilder.buildStateMachine(config, crafting, faith, leaders, clientHub.getUsernames(), new Random(3), isSinglePlayer, actionQueue);
        } catch (ParserException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not parse the json files");
        }
        new Controller(stateMachine, actionQueue, clientHub).start();
    }

    public synchronized void endGame() throws GameNotStartedException{
        if(!currentState.equals(MatchState.PLAYING))
            throw new GameNotStartedException("Can't terminate the game if it hasn't started");

        currentState = MatchState.ENDED;
    }

    public synchronized String getGameName() {
        return gameName;
    }

    public synchronized StateMachine getStateMachine(){
        return stateMachine;
    }

    public synchronized ActionQueue getActionQueue(){
        return actionQueue;
    }

    public synchronized List<String> getUsernames() {
        return clientHub.getUsernames();
    }
    public synchronized int getMatchSize() {
        return matchSize;
    }

    public synchronized boolean isSinglePlayer() {
        return isSinglePlayer;
    }

    public synchronized MatchState getCurrentState() {
        return currentState;
    }
}
