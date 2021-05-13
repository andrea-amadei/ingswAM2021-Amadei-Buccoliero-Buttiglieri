package it.polimi.ingsw.clientproto.updates;

import it.polimi.ingsw.clientproto.model.ClientModel;
import it.polimi.ingsw.clientproto.network.ServerNetworkObject;

/**
 * An update to be applied to the client
 */
public interface Update extends ServerNetworkObject {
    void apply(ClientModel client);

    void checkFormat();
}
