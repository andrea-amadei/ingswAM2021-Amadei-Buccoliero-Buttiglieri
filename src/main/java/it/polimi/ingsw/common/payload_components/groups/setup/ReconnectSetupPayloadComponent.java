package it.polimi.ingsw.common.payload_components.groups.setup;

import it.polimi.ingsw.common.annotations.SerializedType;

@SerializedType("reconnect")
public class ReconnectSetupPayloadComponent extends SetupPayloadComponent{
    private final String username;

    public ReconnectSetupPayloadComponent(String username){
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
