package it.polimi.ingsw.client;

import it.polimi.ingsw.client.cli.framework.CliFramework;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.client.network.ServerNetworkObject;
import it.polimi.ingsw.client.updates.Update;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.setup.SetUsernameSetupPayloadComponent;
import it.polimi.ingsw.exceptions.UnableToDrawElementException;
import it.polimi.ingsw.parser.JSONParser;
import it.polimi.ingsw.parser.JSONSerializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerHandler extends Thread{
    private Socket serverSocket;

    private PrintWriter out;
    private BufferedReader in;

    private final ClientModel client;
    private final CliFramework framework;

    public ServerHandler(int port, ClientModel client, CliFramework framework){
        try {
            serverSocket = new Socket("localhost", port);
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
        ServerNetworkObject readObject;
        while(true){
            try {
                readObject = JSONParser.getServerNetworkObject(in.readLine());

                if(readObject instanceof Update){
                    ((Update) readObject).apply(client);

                framework.renderActiveFrame();
                }
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
}
