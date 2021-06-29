package it.polimi.ingsw.common.payload_components.groups.actions;

import it.polimi.ingsw.common.annotations.SerializedType;

@SerializedType("select_resources")
public class SelectResourcesActionPayloadComponent extends ActionPayloadComponent{

    private final String containerId;
    private final String resource;
    private final Integer amount;

    public SelectResourcesActionPayloadComponent(String player, String containerId, String resource, int amount) {
        super(player);
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
