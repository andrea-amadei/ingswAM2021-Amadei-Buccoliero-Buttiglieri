package it.polimi.ingsw.common.payload_components.groups.actions;

import it.polimi.ingsw.annotations.SerializedType;

@SerializedType("confirm")
public class ConfirmActionPayloadComponent extends ActionPayloadComponent{
    public ConfirmActionPayloadComponent(String player) {
        super(player);
    }
}
