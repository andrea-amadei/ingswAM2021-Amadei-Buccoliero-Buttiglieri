package it.polimi.ingsw.common.payload_components.groups.actions;

import it.polimi.ingsw.annotations.SerializedType;

@SerializedType("next_turn")
public class NextTurnActionPayloadComponent extends ActionPayloadComponent{
    public NextTurnActionPayloadComponent(String player) {
        super(player);
    }
}
