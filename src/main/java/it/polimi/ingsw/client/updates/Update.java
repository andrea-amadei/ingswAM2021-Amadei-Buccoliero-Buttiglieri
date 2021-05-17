package it.polimi.ingsw.client.updates;

import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.client.network.ServerNetworkObject;

/**
 * An update to be applied to the client
 */
public interface Update extends ServerNetworkObject {
    void apply(ClientModel client);

    void checkFormat();
}
