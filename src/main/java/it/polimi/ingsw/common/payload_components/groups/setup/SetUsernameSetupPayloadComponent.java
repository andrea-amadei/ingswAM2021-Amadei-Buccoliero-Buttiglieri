package it.polimi.ingsw.common.payload_components.groups.setup;

import it.polimi.ingsw.common.annotations.SerializedType;

@SerializedType("set_username")
public class SetUsernameSetupPayloadComponent extends SetupPayloadComponent{
    private final String username;

    public SetUsernameSetupPayloadComponent(String username){
        if(username == null)
            throw new NullPointerException();
        this.username = username;
    }

    public String getUsername() {return username;}
}
