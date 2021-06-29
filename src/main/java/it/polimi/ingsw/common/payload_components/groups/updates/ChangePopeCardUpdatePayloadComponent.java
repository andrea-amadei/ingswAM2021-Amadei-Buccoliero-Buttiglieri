package it.polimi.ingsw.common.payload_components.groups.updates;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.common.annotations.SerializedType;
import it.polimi.ingsw.common.payload_components.groups.SpecificUpdatePayloadComponent;
import it.polimi.ingsw.server.model.holder.FaithHolder;

@SerializedType("change_pope_card")
public class ChangePopeCardUpdatePayloadComponent extends SpecificUpdatePayloadComponent {

    @SerializedName("status")
    private FaithHolder.CheckpointStatus status;

    @SerializedName("index")
    private int index;

    /**
     * This constructor shall only be used to instance payloads through reflection
     */
    public ChangePopeCardUpdatePayloadComponent() { }

    public ChangePopeCardUpdatePayloadComponent(String player, FaithHolder.CheckpointStatus status, int index) {
        super(player);

        if(status == null)
            throw new NullPointerException();

        this.status = status;
        this.index = index;
    }

    public FaithHolder.CheckpointStatus getStatus() {
        return status;
    }

    public int getIndex() {
        return index;
    }
}
