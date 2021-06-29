package it.polimi.ingsw.client;

import it.polimi.ingsw.client.clientmodel.ClientModel;

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
