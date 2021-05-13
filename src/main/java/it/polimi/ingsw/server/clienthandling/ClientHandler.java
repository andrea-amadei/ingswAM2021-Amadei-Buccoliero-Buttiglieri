package it.polimi.ingsw.server.clienthandling;

import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.setup.SetUsernameSetupPayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.setup.TextSetupPayloadComponent;
import it.polimi.ingsw.parser.JSONParser;
import it.polimi.ingsw.parser.JSONSerializer;
import it.polimi.ingsw.server.Logger;
import it.polimi.ingsw.server.MatchesManager;
import it.polimi.ingsw.server.clienthandling.setupactions.SetupAction;

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
                    ((SetupAction)clientNetworkObject).checkFormat();
                    ((SetupAction)clientNetworkObject).execute(this);
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
        //sendPayload(new TextSetupPayloadComponent("Your username is \"" + username + "\""));
    }
}
