package it.polimi.ingsw.common.payload_components.groups.actions;

import it.polimi.ingsw.annotations.SerializedGroup;
import it.polimi.ingsw.common.payload_components.PayloadComponent;

@SerializedGroup("action")
public abstract class ActionPayloadComponent implements PayloadComponent {
    private final String player;

    public ActionPayloadComponent(String player){
        this.player = player;
    }

    public String getPlayer() {
        return player;
    }
}
