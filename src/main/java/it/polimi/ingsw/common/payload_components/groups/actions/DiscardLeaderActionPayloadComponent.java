package it.polimi.ingsw.common.payload_components.groups.actions;

import it.polimi.ingsw.annotations.SerializedType;

@SerializedType("discard_leader")
public class DiscardLeaderActionPayloadComponent extends ActionPayloadComponent{

    private final Integer leaderID;

    public DiscardLeaderActionPayloadComponent(String player, int leaderID) {
        super(player);
        this.leaderID = leaderID;
    }

    public Integer getLeaderID() {
        return leaderID;
    }
}
