package it.polimi.ingsw.clientproto;

import it.polimi.ingsw.clientproto.model.ClientModel;

/**
 * This component is responsible for modifying the state of the client due to
 * internal stimuli
 */
public class CommandExecutor {

    private final ClientModel client;

    public CommandExecutor(ClientModel client){
        this.client = client;
    }


}
