package it.polimi.ingsw.common.payload_components.groups.updates;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.annotations.SerializedType;
import it.polimi.ingsw.common.payload_components.groups.SpecificUpdatePayloadComponent;

@SerializedType("add_leader_card")
public class AddLeaderCardUpdatePayloadComponent extends SpecificUpdatePayloadComponent {

    @SerializedName("id")
    private int id;

    /**
     * This constructor shall only be used to instance payloads through reflection
     */
    public AddLeaderCardUpdatePayloadComponent() { }

    public AddLeaderCardUpdatePayloadComponent(String player, int id) {
        super(player);
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
