package it.polimi.ingsw.common.payload_components.groups.updates;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.common.annotations.SerializedType;
import it.polimi.ingsw.common.payload_components.groups.SpecificUpdatePayloadComponent;

@SerializedType("selected_resource")
public class SelectedResourceUpdatePayloadComponent extends SpecificUpdatePayloadComponent {

    @SerializedName(value = "container_id", alternate = "containerId")
    private String containerId;

    @SerializedName("resource")
    private String resource;

    @SerializedName("amount")
    private int amount;

    /**
     * This constructor shall only be used to instance payloads through reflection
     */
    public SelectedResourceUpdatePayloadComponent() { }

    public SelectedResourceUpdatePayloadComponent(String player, String containerId, String resource, int amount) {
        super(player);

        if(containerId == null || resource == null)
            throw new NullPointerException();

        this.containerId = containerId;
        this.resource = resource;
        this.amount = amount;
    }

    public String getContainerId() {
        return containerId;
    }

    public String getResource() {
        return resource;
    }

    public int getAmount() {
        return amount;
    }
}
