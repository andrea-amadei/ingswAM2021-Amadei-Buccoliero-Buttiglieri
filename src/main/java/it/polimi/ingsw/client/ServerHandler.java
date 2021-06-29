package it.polimi.ingsw.client;

import it.polimi.ingsw.client.cli.framework.CliFramework;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.client.network.ServerNetworkObject;
import it.polimi.ingsw.client.ping.Ping;
import it.polimi.ingsw.client.updates.Update;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.PingPayloadComponent;
import it.polimi.ingsw.exceptions.UnableToDrawElementException;
import it.polimi.ingsw.parser.JSONParser;
import it.polimi.ingsw.parser.JSONSerializer;
import it.polimi.ingsw.server.Logger;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ServerHandler extends Thread{
    private Socket serverSocket;

    private PrintWriter out;
    private BufferedReader in;

    private final ClientModel client;
    private final CliFramework framework;

    private boolean connected;
    private boolean pingReceived;

    public ServerHandler(String host, int port, ClientModel client, CliFramework framework) throws IOException {
        try {
            serverSocket = new Socket(host, port);
        } catch (IOException e) {
            throw new IOException("Cannot connect to the server");
        }

        try {
            out = new PrintWriter(serverSocket.getOutputStream(), true);
        } catch (IOException e) {
            throw new IOException("Cannot create the output stream");
        }

        try {
            in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
        } catch (IOException e) {
            throw new IOException("Cannot create the input stream");
        }

        this.connected = true;
        this.pingReceived = false;
        this.client = client;
        this.framework = framework;
    }

    @Override
    public void run(){
        List<ServerNetworkObject> readObjects;
        while(true){
            try {
                String line = in.readLine();
                if(line == null)
                    throw new IOException("Connection terminated");
                readObjects = JSONParser.getServerNetworkObjects(line);
                boolean needRefresh = false;
                for(ServerNetworkObject readObject : readObjects){
                    needRefresh = false;

                    if(readObject instanceof Update) {
                        ((Update) readObject).apply(client);
                        needRefresh = true;
                    }
                    else if(readObject instanceof Ping){
                        sendPayload(new PingPayloadComponent());
                        setPingReceived(true);
                    }
                }
                if(framework != null && needRefresh)
                    framework.renderActiveFrame();
            } catch (IOException | UnableToDrawElementException e) {
                Logger.log("The client detected the disconnection with the reader");
                break;
            }
        }
    }

    public synchronized void sendPayload(PayloadComponent payloadComponent){
        out.println(JSONSerializer.toJson(payloadComponent));
    }

    public synchronized String getUsername(){
        return client.getPersonalData().getUsername();
    }

    public synchronized ClientModel getClient() {
        return client;
    }

    public synchronized boolean isConnected(){
        return connected;
    }

    public synchronized void setConnected(boolean connected){
        this.connected = connected;
    }
    public synchronized CliFramework getFramework(){return this.framework;}

    public synchronized void setPingReceived(boolean pingReceived){
        this.pingReceived = pingReceived;
    }

    public synchronized boolean isPingReceived(){
        return this.pingReceived;
    }

    public synchronized void disconnect() throws IOException {
        Logger.log("The client has been disconnected from the server");
        serverSocket.close();
        this.connected = false;
    }
}
