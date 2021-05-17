package it.polimi.ingsw.common.payload_components.groups.actions;

import it.polimi.ingsw.annotations.SerializedType;

@SerializedType("activate_leader")
public class ActivateLeaderActionPayloadComponent extends ActionPayloadComponent{

    private final Integer leaderID;

    public ActivateLeaderActionPayloadComponent(String player, int leaderID) {
        super(player);
        this.leaderID = leaderID;
    }

    public Integer getLeaderID() {
        return leaderID;
    }
}
