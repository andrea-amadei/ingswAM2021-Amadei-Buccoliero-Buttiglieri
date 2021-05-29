package it.polimi.ingsw.client;

import it.polimi.ingsw.client.cli.framework.CliFramework;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.client.network.ServerNetworkObject;
import it.polimi.ingsw.client.ping.Ping;
import it.polimi.ingsw.client.updates.Update;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.PingPayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.setup.SetUsernameSetupPayloadComponent;
import it.polimi.ingsw.exceptions.UnableToDrawElementException;
import it.polimi.ingsw.parser.JSONParser;
import it.polimi.ingsw.parser.JSONSerializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ServerHandler extends Thread{
    private Socket serverSocket;

    private PrintWriter out;
    private BufferedReader in;

    private final ClientModel client;
    private final CliFramework framework;

    public ServerHandler(String host, int port, ClientModel client, CliFramework framework){
        try {
            serverSocket = new Socket(host, port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            out = new PrintWriter(serverSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.client = client;
        this.framework = framework;
    }

    @Override
    public void run(){
        List<ServerNetworkObject> readObjects;
        while(true){
            try {
                readObjects = JSONParser.getServerNetworkObjects(in.readLine());
                boolean needRefresh = false;
                for(ServerNetworkObject readObject : readObjects){
                    needRefresh = false;

                    if(readObject instanceof Update) {
                        ((Update) readObject).apply(client);
                        needRefresh = true;
                    }
                    else if(readObject instanceof Ping){
                        sendPayload(new PingPayloadComponent());
                    }
                }
                if(needRefresh)
                    framework.renderActiveFrame();
            } catch (IOException | UnableToDrawElementException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public void sendPayload(PayloadComponent payloadComponent){
        out.println(JSONSerializer.toJson(payloadComponent));
    }

    public String getUsername(){
        return client.getPersonalData().getUsername();
    }

    public ClientModel getClient() {
        return client;
    }
}
