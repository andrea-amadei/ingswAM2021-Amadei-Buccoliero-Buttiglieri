package it.polimi.ingsw.clientproto;

import it.polimi.ingsw.clientproto.model.ClientModel;
import it.polimi.ingsw.clientproto.network.ServerNetworkObject;
import it.polimi.ingsw.clientproto.parser.ClientDeserializer;
import it.polimi.ingsw.clientproto.updates.Update;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.setup.SetUsernameSetupPayloadComponent;
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

    public ServerHandler(int port, ClientModel client){
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

    }

    @Override
    public void run(){
        ServerNetworkObject readObject;
        while(true){
            try {
                readObject = ClientDeserializer.getServerNetworkObject(in.readLine());

                if(readObject instanceof Update){
                    ((Update) readObject).apply(client);
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public void sendPayload(PayloadComponent payloadComponent){
        out.println(JSONSerializer.toJson(payloadComponent));
    }

    public void sendSetUsernameCommand(String username){
        sendPayload(new SetUsernameSetupPayloadComponent(username));
    }
}
