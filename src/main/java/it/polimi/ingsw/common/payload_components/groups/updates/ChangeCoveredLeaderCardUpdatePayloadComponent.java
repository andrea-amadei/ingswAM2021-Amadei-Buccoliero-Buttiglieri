package it.polimi.ingsw.common.payload_components.groups.updates;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.common.annotations.SerializedType;
import it.polimi.ingsw.common.payload_components.groups.SpecificUpdatePayloadComponent;

@SerializedType("change_covered_leader_card")
public class ChangeCoveredLeaderCardUpdatePayloadComponent extends SpecificUpdatePayloadComponent {

    @SerializedName("delta")
    private int delta;

    /**
     * This constructor shall only be used to instance payloads through reflection
     */
    public ChangeCoveredLeaderCardUpdatePayloadComponent() { }

    public ChangeCoveredLeaderCardUpdatePayloadComponent(String player, int delta) {
        super(player);
        this.delta = delta;
    }

    public int getDelta() {
        return delta;
    }
}
