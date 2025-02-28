package it.polimi.ingsw.server.clienthandling;

import it.polimi.ingsw.common.ActionQueue;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.ErrorPayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.setup.SetGameNameSetupPayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.setup.SetUsernameSetupPayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.setup.TextSetupPayloadComponent;
import it.polimi.ingsw.common.exceptions.DuplicateUsernameException;
import it.polimi.ingsw.common.exceptions.GameNotInLobbyException;
import it.polimi.ingsw.server.model.actions.Action;
import it.polimi.ingsw.server.model.actions.DisconnectPlayerAction;
import it.polimi.ingsw.server.model.actions.ReconnectPlayerAction;
import it.polimi.ingsw.common.parser.JSONParser;
import it.polimi.ingsw.common.parser.JSONSerializer;
import it.polimi.ingsw.server.Logger;
import it.polimi.ingsw.server.ServerManager;
import it.polimi.ingsw.server.clienthandling.ping.Ping;
import it.polimi.ingsw.server.clienthandling.setupactions.SetupAction;
import it.polimi.ingsw.common.utils.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

public class ClientHandler implements Runnable{

    private final Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    private String username;
    private Match currentMatch;
    private boolean freshClient;

    private final ServerManager serverManager;
    private final DisconnectionManager disconnectionManager;

    public ClientHandler(Socket clientSocket, ServerManager serverManager, DisconnectionManager disconnectionManager){
        this.clientSocket = clientSocket;
        this.serverManager = serverManager;
        if(clientSocket.isConnected()) {

            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.disconnectionManager = disconnectionManager;
        serverManager.addUnregisteredHandler(this);
        freshClient = true;
    }


    @Override
    public void run() {
        String line;
        while (true) {
            try {
                if ((line = in.readLine()) == null) break;
                ClientNetworkObject clientNetworkObject = JSONParser.getClientNetworkObject(line);

                if(clientNetworkObject instanceof SetupAction){
                    Logger.log("Client sent: " + line);
                    try {
                        ((SetupAction) clientNetworkObject).checkFormat();
                    }catch(RuntimeException e){
                        sendPayload(new ErrorPayloadComponent("Invalid request"));
                        continue;
                    }
                    ((SetupAction)clientNetworkObject).execute(this);
                }else if(clientNetworkObject instanceof Action){
                    Logger.log("Client sent: " + line);
                    try {
                        ((Action) clientNetworkObject).checkFormat();
                    }catch(RuntimeException e){
                        sendPayload(new ErrorPayloadComponent("Invalid request"));
                        continue;
                    }
                    if(!((Action) clientNetworkObject).getSender().equals(username)){
                        sendPayload(new ErrorPayloadComponent("Sender doesn't match the username"));
                    }else {
                        if(currentMatch != null && currentMatch.getCurrentState().equals(Match.MatchState.PLAYING))
                            currentMatch.getActionQueue().addAction((Action) clientNetworkObject, ActionQueue.Priority.CLIENT_ACTION.ordinal());
                        else
                            sendPayload(new TextSetupPayloadComponent("Cannot send an action if the game is not started"));
                    }
                }else if(clientNetworkObject instanceof Ping){
                    if(username != null) {
                        disconnectionManager.ack(username);
                    }else{
                        disconnectionManager.ackUnregistered(this);
                    }
                }

            } catch (IOException e) {
                //e.printStackTrace();
                Logger.log("The client with username \"" + Optional.ofNullable(username).orElse("Unknown") + "\" disconnected due to IOException in ClientHandler.run");
                break;
            }
        }
    }

    public synchronized void sendPayload(PayloadComponent payloadComponent){
        sendPayload(Collections.singletonList(payloadComponent));
    }

    public synchronized void sendPayload(List<PayloadComponent> payloads){
        String json = JSONSerializer.toJson(payloads);
        out.println(json);
    }


    public synchronized void setUsername(String username){
        if(username == null){
            sendPayload(new TextSetupPayloadComponent("Missing username"));
            return;
        }
        if(this.username != null){
            sendPayload(new TextSetupPayloadComponent("Already chose an username"));
            return;
        }

        if(username.equalsIgnoreCase("Unknown")){
            sendPayload(new TextSetupPayloadComponent("Username \"" + username + "\" is invalid"));
            return;
        }

        if(username.length() <= 2 || username.length() >= 24){
            sendPayload(new TextSetupPayloadComponent("Username length must be between 2 and 24"));
            return;
        }
        try{
            serverManager.registerUsername(username, this);
        }catch(IllegalArgumentException ex){
            sendPayload(new TextSetupPayloadComponent("Someone already registered the username \"" + username + "\""));
            return;
        }

        this.username = username;
        serverManager.removeUnregisteredClient(this);
        sendPayload(new SetUsernameSetupPayloadComponent(username));
    }

    public synchronized void createMatch(String matchName, int playerCount, boolean isSinglePlayer){

        boolean rightConfirmed = checkMatchCreationRights(matchName, playerCount, isSinglePlayer);
        if(!rightConfirmed)
            return;


        Match match = new Match(matchName, new Pair<>(username, this), playerCount, isSinglePlayer, serverManager);

        try {
            serverManager.addMatch(match);
        }catch(IllegalArgumentException e){
            sendPayload(new TextSetupPayloadComponent(e.getMessage()));
            return;
        }

        this.currentMatch = match;
        sendPayload(new SetGameNameSetupPayloadComponent(matchName));
        if(playerCount > 1) {
            sendPayload(new TextSetupPayloadComponent("You created the game! Waiting for other players..."));
        }
    }

    public synchronized void createCustomMatch(String matchName, int playerCount, boolean isSinglePlayer,
                                               String userConfig, String userCrafting, String userFaith, String userLeader){

        boolean rightConfirmed = checkMatchCreationRights(matchName, playerCount, isSinglePlayer);
        if(!rightConfirmed)
            return;

        if(userConfig == null || userCrafting == null || userFaith == null || userLeader == null){
            sendPayload(new TextSetupPayloadComponent("Custom config files were not sent. Try again"));
            return;
        }

        Match match;
        try {
           match = new Match(matchName, new Pair<>(username, this), playerCount, isSinglePlayer, serverManager, userConfig,
                    userCrafting, userFaith, userLeader);
        }catch(RuntimeException e){
            Logger.log("The game cannot be created because config files where not valid");
            sendPayload(new TextSetupPayloadComponent("Cannot create the match because config files were not valid"));
            return;
        }

        try {
            serverManager.addMatch(match);
        }catch(IllegalArgumentException e){
            sendPayload(new TextSetupPayloadComponent(e.getMessage()));
            return;
        }

        this.currentMatch = match;
        sendPayload(new SetGameNameSetupPayloadComponent(matchName));
        if(playerCount > 1) {
            sendPayload(new TextSetupPayloadComponent("You created the game! Waiting for other players..."));
        }

    }

    private boolean checkMatchCreationRights(String matchName, int playerCount, boolean isSinglePlayer) {
        if(username == null){
            sendPayload(new TextSetupPayloadComponent("Set your username first"));
            return false;
        }
        if(this.currentMatch != null){
            sendPayload(new TextSetupPayloadComponent("Already in a match"));
            return false;
        }
        if(matchName == null){
            sendPayload(new TextSetupPayloadComponent("Match name not valid. Choose a valid one"));
            return false;
        }
        if(playerCount <= 0 || playerCount > 4){
            sendPayload(new TextSetupPayloadComponent("Number of players must be between 1 and 4"));
            return false;
        }
        if(isSinglePlayer && playerCount > 1){
            sendPayload(new TextSetupPayloadComponent("In single player mode there must be only 1 player"));
            return false;
        }
        if(!isSinglePlayer && playerCount < 2){
            sendPayload(new TextSetupPayloadComponent("In multiplayer mode there must be at leas 2 players"));
            return false;
        }
        if(serverManager.alreadyExistentGameName(matchName)){
            sendPayload(new TextSetupPayloadComponent("There is another match with name \"" + matchName + "\""));
            return false;
        }
        return true;
    }

    public synchronized void joinMatch(String matchName){
        if(username == null){
            sendPayload(new TextSetupPayloadComponent("Set your username first"));
            return;
        }
       if(matchName == null){
           sendPayload(new TextSetupPayloadComponent("Match name not inserted"));
           return;
       }
       if(this.currentMatch != null){
           sendPayload(new TextSetupPayloadComponent("You are already in a match"));
           return;
       }
       if(!serverManager.alreadyExistentGameName(matchName)){
           sendPayload(new TextSetupPayloadComponent("Match \"" + matchName + "\" does not exist in the server"));
           return;
       }

       Match match = serverManager.getMatchByName(matchName);

        try {
            match.addPlayer(new Pair<>(username, this));
        } catch (DuplicateUsernameException | GameNotInLobbyException e) {
            sendPayload(new TextSetupPayloadComponent("The match is not in lobby state"));
            return;
        }
        this.currentMatch = match;
        sendPayload(new TextSetupPayloadComponent("Joined the match! Waiting for other players..."));
        sendPayload(new SetGameNameSetupPayloadComponent(matchName));
    }

    public synchronized void disconnect() throws IOException {
        Logger.log("Disconnecting the client \"" + Optional.ofNullable(username).orElse("Unknown") + "\" with the disconnect() method of ClientHandler");
        if (username == null){
            clientSocket.close();
            serverManager.removeUnregisteredClient(this);
            return;
        }

        serverManager.disconnectPlayer(username);

        if(currentMatch == null){
            clientSocket.close();
            return;
        }

        currentMatch.disconnectPlayer(username);
        if(currentMatch.getCurrentState().equals(Match.MatchState.PLAYING)) {
            currentMatch.getActionQueue().addAction(new DisconnectPlayerAction(username), 0);
        }
        clientSocket.close();
    }

    public synchronized void reconnect(String username){
        if(username == null) {
            sendPayload(new TextSetupPayloadComponent("Null username, try again"));
            return;
        }
        if(this.username != null){
            sendPayload(new TextSetupPayloadComponent("You are already connected"));
            return;
        }
        if(!serverManager.alreadyExistentUsername(username)) {
            sendPayload(new TextSetupPayloadComponent("There is no registered username \"" + username + "\" in the server. Try again"));
            return;
        }
        if(serverManager.isPlayerConnected(username)) {
            sendPayload(new TextSetupPayloadComponent("Another client is already connected to the server with the same username. Don't cheat :/"));
            return;
        }

        //we can indeed reconnect

        sendPayload(new TextSetupPayloadComponent("You have been reconnected to the server"));

        this.username = username;
        serverManager.removeUnregisteredClient(this);
        sendPayload(new SetUsernameSetupPayloadComponent(username));

        //find the match in which the client is playing (if no match is found, set it to null)
        Set<Match> matches = serverManager.getMatches();
        currentMatch = matches.stream().filter(m -> m.getUsernames().contains(username)).findFirst().orElse(null);

        //update the player status in the server manager
        serverManager.reconnectPlayer(username, this);

        if(currentMatch == null)
            return;

        sendPayload(new SetGameNameSetupPayloadComponent(currentMatch.getGameName()));

        //Reconnect the player to the game.
        //Note that if the player left the lobby, it is not part of the older game anymore. So if a match is found
        //it must be in the play state

        currentMatch.reconnectPlayer(username, this);
        if(currentMatch.getCurrentState().equals(Match.MatchState.PLAYING))
            currentMatch.getActionQueue().addAction(new ReconnectPlayerAction(username), 0);

    }

    public synchronized boolean isFreshClient(){
        return freshClient;
    }

    public synchronized void setNotFreshClient(){
        freshClient = false;
    }
}
