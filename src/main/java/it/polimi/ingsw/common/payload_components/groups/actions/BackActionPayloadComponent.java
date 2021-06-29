package it.polimi.ingsw.common.payload_components.groups.actions;

import it.polimi.ingsw.common.annotations.SerializedType;

@SerializedType("back")
public class BackActionPayloadComponent extends ActionPayloadComponent{
    public BackActionPayloadComponent(String player) {
        super(player);
    }
}
