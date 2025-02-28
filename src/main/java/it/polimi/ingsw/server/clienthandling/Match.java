package it.polimi.ingsw.server.clienthandling;

import it.polimi.ingsw.common.ActionQueue;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.common.exceptions.*;
import it.polimi.ingsw.server.model.fsm.StateMachine;
import it.polimi.ingsw.server.ServerBuilder;
import it.polimi.ingsw.server.ServerManager;
import it.polimi.ingsw.common.utils.Pair;
import it.polimi.ingsw.common.utils.ResourceLoader;

import java.util.Arrays;
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
    private final ServerManager serverManager;

    private final String gameName;
    private final int matchSize;
    private final boolean isSinglePlayer;

    private MatchState currentState;

    private String userConfig;
    private String userCrafting;
    private String userFaith;
    private String userLeader;


    public Match(String gameName, Pair<String, ClientHandler> host, int matchSize, boolean isSinglePlayer, ServerManager serverManager){
        this(gameName, host, matchSize, isSinglePlayer, serverManager, null, null, null, null);
    }

    public Match(String gameName, Pair<String, ClientHandler> host, int matchSize, boolean isSinglePlayer, ServerManager serverManager,
                 String userConfig, String userCrafting, String userFaith, String userLeader){

        stateMachine = null;
        actionQueue = new ActionQueue();

        clientHub = new ClientHub();

        this.serverManager = serverManager;

        this.gameName = gameName;
        this.matchSize = matchSize;
        this.isSinglePlayer = isSinglePlayer;

        this.currentState = MatchState.LOBBY;

        if(userConfig != null && userCrafting != null && userFaith != null && userLeader != null){
            try{
                ServerBuilder.buildCustomStateMachine(userConfig, userCrafting, userFaith, userLeader, Arrays.asList("player1", "player2", "player3", "player4"), new Random(2), isSinglePlayer, new ActionQueue());
                this.userConfig = userConfig;
                this.userCrafting = userCrafting;
                this.userLeader = userLeader;
                this.userFaith = userFaith;
            } catch (ParserException | RuntimeException e) {
                throw new IllegalArgumentException("The config files were not valid");
            }
        }
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


    /**
     * If the match is in Lobby state, then the pair (username, clientHandler) is removed from the clientHub.
     * If the match is in Playing state, then the clientHandler associated with the specified username is set to null in the clientHub
     * @param username the disconnecting player's username
     * @throws RuntimeException if the match is not in Lobby or Playing status
     */
    public synchronized void disconnectPlayer(String username){
        if(currentState.equals(MatchState.LOBBY)) {
            clientHub.hardDisconnectClient(username);
            if(clientHub.getUsernames().size() == 0)
                serverManager.removeMatch(this);
        }
        else if(currentState.equals(MatchState.PLAYING) || currentState.equals(MatchState.ENDED))
            clientHub.disconnectClient(username);
        else
            throw new RuntimeException("The match was in READY state (?.?)");
    }

    public synchronized void reconnectPlayer(String username, ClientHandler handler){
        if(!currentState.equals(MatchState.PLAYING))
            throw new RuntimeException("A player tried to reconnect to a game that was not in playing state");
        clientHub.reconnectClient(username, handler);
    }

    public synchronized void startGame() throws GameNotReadyException {
        if(!currentState.equals(MatchState.READY))
            throw new GameNotReadyException("The game is not ready");

        currentState = MatchState.PLAYING;

        String config;
        String crafting;
        String faith;
        String leaders;

        if(this.userFaith != null && this.userCrafting != null && this.userConfig != null && this.userLeader != null){
            config = this.userConfig;
            crafting = this.userCrafting;
            faith = this.userFaith;
            leaders = this.userLeader;
        }else {
            config = ResourceLoader.loadFile("cfg/config.json");
            crafting = ResourceLoader.loadFile("cfg/crafting.json");
            faith = ResourceLoader.loadFile("cfg/faith.json");
            leaders = ResourceLoader.loadFile("cfg/leaders.json");
        }
        try {
            if(this.userFaith != null && this.userCrafting != null && this.userConfig != null && this.userLeader != null)
                this.stateMachine = ServerBuilder.buildCustomStateMachine(config, crafting, faith, leaders, clientHub.getUsernames(), new Random(3), isSinglePlayer, actionQueue);
            else
                this.stateMachine = ServerBuilder.buildStateMachine(config, crafting, faith, leaders, clientHub.getUsernames(), new Random(3), isSinglePlayer, actionQueue);
        } catch (ParserException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not parse the json files");
        }
        new Controller(stateMachine, actionQueue, clientHub, this, serverManager).start();
    }

    public synchronized void endGame(){
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
