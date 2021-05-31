package it.polimi.ingsw.server.clienthandling;

import it.polimi.ingsw.common.ActionQueue;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.ErrorPayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.setup.SetGameNameSetupPayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.setup.SetUsernameSetupPayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.setup.TextSetupPayloadComponent;
import it.polimi.ingsw.exceptions.DuplicateUsernameException;
import it.polimi.ingsw.exceptions.GameNotInLobbyException;
import it.polimi.ingsw.model.actions.Action;
import it.polimi.ingsw.model.actions.DisconnectPlayerAction;
import it.polimi.ingsw.model.actions.ReconnectPlayerAction;
import it.polimi.ingsw.parser.JSONParser;
import it.polimi.ingsw.parser.JSONSerializer;
import it.polimi.ingsw.server.Logger;
import it.polimi.ingsw.server.ServerManager;
import it.polimi.ingsw.server.clienthandling.ping.Ping;
import it.polimi.ingsw.server.clienthandling.setupactions.SetupAction;
import it.polimi.ingsw.utils.Pair;

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
                    currentMatch.getActionQueue().addAction((Action)clientNetworkObject, ActionQueue.Priority.CLIENT_ACTION.ordinal());
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
        if(this.username != null){
            sendPayload(new TextSetupPayloadComponent("Already chose an username"));
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
        if(username == null){
            sendPayload(new TextSetupPayloadComponent("Set your username first"));
            return;
        }
        if(matchName == null){
            sendPayload(new TextSetupPayloadComponent("Match name not valid. Choose a valid one"));
            return;
        }
        if(playerCount <= 0 || playerCount > 4){
            sendPayload(new TextSetupPayloadComponent("Number of players must be between 1 and 4"));
            return;
        }
        if(isSinglePlayer && playerCount > 1){
            sendPayload(new TextSetupPayloadComponent("In single player mode there must be only 1 player"));
            return;
        }

        Match match = new Match(matchName, new Pair<>(username, this), playerCount, isSinglePlayer, serverManager);

        try {
            serverManager.addMatch(match);
        }catch(IllegalArgumentException e){
            sendPayload(new TextSetupPayloadComponent(e.getMessage()));
        }

        this.currentMatch = match;
        sendPayload(new SetGameNameSetupPayloadComponent(matchName));
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
        currentMatch.getActionQueue().addAction(new DisconnectPlayerAction(username), 0);
        clientSocket.close();
    }

    public synchronized void reconnect(String username){
        if(username == null) {
            sendPayload(new TextSetupPayloadComponent("Null username, try again"));
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
        currentMatch.getActionQueue().addAction(new ReconnectPlayerAction(username), 0);

    }

    public synchronized boolean isFreshClient(){
        return freshClient;
    }

    public synchronized void setNotFreshClient(){
        freshClient = false;
    }
}
