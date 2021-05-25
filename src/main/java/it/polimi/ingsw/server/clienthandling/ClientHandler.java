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
import it.polimi.ingsw.parser.JSONParser;
import it.polimi.ingsw.parser.JSONSerializer;
import it.polimi.ingsw.server.Logger;
import it.polimi.ingsw.server.MatchesManager;
import it.polimi.ingsw.server.clienthandling.setupactions.SetupAction;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.utils.PayloadFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable{

    private final Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    private String username;
    private Match currentMatch;

    private final MatchesManager matchesManager;

    public ClientHandler(Socket clientSocket, MatchesManager matchesManager){
        this.clientSocket = clientSocket;
        this.matchesManager = matchesManager;
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

    }


    @Override
    public void run() {
        String line;
        while (true) {
            try {
                if ((line = in.readLine()) == null) break;
                Logger.log("Client sent: " + line);
                ClientNetworkObject clientNetworkObject = JSONParser.getClientNetworkObject(line);

                if(clientNetworkObject instanceof SetupAction){
                    try {
                        ((SetupAction) clientNetworkObject).checkFormat();
                    }catch(RuntimeException e){
                        sendPayload(new ErrorPayloadComponent("Invalid request"));
                        continue;
                    }
                    ((SetupAction)clientNetworkObject).execute(this);
                }else if(clientNetworkObject instanceof Action){
                    try {
                        ((Action) clientNetworkObject).checkFormat();
                    }catch(RuntimeException e){
                        sendPayload(new ErrorPayloadComponent("Invalid request"));
                        continue;
                    }
                    currentMatch.getActionQueue().addAction((Action)clientNetworkObject, ActionQueue.Priority.CLIENT_ACTION.ordinal());
                }

            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public void sendPayload(PayloadComponent payloadComponent){
        String json = JSONSerializer.toJson(payloadComponent);
        out.println(json);
    }


    public void setUsername(String username){
        if(this.username != null){
            sendPayload(new TextSetupPayloadComponent("Already chose an username"));
            return;
        }
        try{
            matchesManager.registerUsername(username);
        }catch(IllegalArgumentException ex){
            sendPayload(new TextSetupPayloadComponent("Someone already registered the username \"" + username + "\""));
            return;
        }

        this.username = username;
        sendPayload(new SetUsernameSetupPayloadComponent(username));
    }

    public void createMatch(String matchName, int playerCount, boolean isSinglePlayer){
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

        Match match = new Match(matchName, new Pair<>(username, this), playerCount, isSinglePlayer);

        try {
            matchesManager.addMatch(match);
        }catch(IllegalArgumentException e){
            sendPayload(new TextSetupPayloadComponent(e.getMessage()));
        }

        this.currentMatch = match;
        sendPayload(new SetGameNameSetupPayloadComponent(matchName));
    }

    public void joinMatch(String matchName){
        if(username == null){
            sendPayload(new TextSetupPayloadComponent("Set your username first"));
            return;
        }
       if(matchName == null){
           sendPayload(new TextSetupPayloadComponent("Match name not inserted"));
           return;
       }
       if(!matchesManager.alreadyExistentGameName(matchName)){
           sendPayload(new TextSetupPayloadComponent("Match \"" + matchName + "\" does not exist in the server"));
           return;
       }

       Match match = matchesManager.getMatchByName(matchName);

        try {
            match.addPlayer(new Pair<>(username, this));
        } catch (DuplicateUsernameException | GameNotInLobbyException e) {
            e.printStackTrace();
        }
        this.currentMatch = match;
        sendPayload(new SetGameNameSetupPayloadComponent(matchName));


    }
}
