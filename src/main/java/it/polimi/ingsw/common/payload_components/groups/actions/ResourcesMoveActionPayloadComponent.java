package it.polimi.ingsw.common.payload_components.groups.actions;

import it.polimi.ingsw.common.annotations.SerializedType;

@SerializedType("resources_move")
public class ResourcesMoveActionPayloadComponent extends ActionPayloadComponent{

    private final String origin;
    private final String destination;
    private final String resourceToMove;
    private final Integer amount;

    public ResourcesMoveActionPayloadComponent(String player, String origin, String destination, String resourceToMove, Integer amount) {
        super(player);
        this.origin = origin;
        this.destination = destination;
        this.resourceToMove = resourceToMove;
        this.amount = amount;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getResourceToMove() {
        return resourceToMove;
    }

    public Integer getAmount() {
        return amount;
    }
}
