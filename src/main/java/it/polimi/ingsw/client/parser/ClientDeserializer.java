package it.polimi.ingsw.client.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.client.network.ServerNetworkObject;
import it.polimi.ingsw.client.parser.adapters.ServerNetworkObjectAdapter;
import it.polimi.ingsw.client.parser.adapters.UpdateAdapter;
import it.polimi.ingsw.client.updates.Update;

public class ClientDeserializer {
    public static ServerNetworkObject getServerNetworkObject(String json){
        Gson gson = new GsonBuilder()
                        .registerTypeAdapter(ServerNetworkObject.class, new ServerNetworkObjectAdapter())
                        .registerTypeAdapter(Update.class, new UpdateAdapter())
                        .create();

        return gson.fromJson(json, ServerNetworkObject.class);
    }
}
