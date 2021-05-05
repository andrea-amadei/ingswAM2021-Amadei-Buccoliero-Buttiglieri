package it.polimi.ingsw.common.payload_components.groups.updates;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.annotations.SerializedType;
import it.polimi.ingsw.common.payload_components.groups.SpecificUpdatePayloadComponent;

@SerializedType("discard_leader_card")
public class DiscardLeaderCardUpdatePayloadComponent extends SpecificUpdatePayloadComponent {

    @SerializedName("id")
    private int id;

    /**
     * This constructor shall only be used to instance payloads through reflection
     */
    public DiscardLeaderCardUpdatePayloadComponent() { }

    public DiscardLeaderCardUpdatePayloadComponent(String player, int id) {
        super(player);
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
