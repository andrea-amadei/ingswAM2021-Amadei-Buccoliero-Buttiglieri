package it.polimi.ingsw.common.payload_components.groups.actions;

import it.polimi.ingsw.annotations.SerializedType;

@SerializedType("confirm_tidy")
public class ConfirmTidyActionPayloadComponent extends ActionPayloadComponent{
    public ConfirmTidyActionPayloadComponent(String player) {
        super(player);
    }
}
