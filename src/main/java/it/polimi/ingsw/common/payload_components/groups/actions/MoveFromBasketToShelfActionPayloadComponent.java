package it.polimi.ingsw.common.payload_components.groups.actions;

import it.polimi.ingsw.annotations.SerializedType;

@SerializedType("move_from_basket_to_shelf")
public class MoveFromBasketToShelfActionPayloadComponent extends ActionPayloadComponent {
    private final String resourceToMove;
    private final Integer amount;
    private final String shelfID;

    public MoveFromBasketToShelfActionPayloadComponent(String player, String resourceToMove, int amount, String shelfID) {
        super(player);
        this.resourceToMove = resourceToMove;
        this.amount = amount;
        this.shelfID = shelfID;
    }

    public String getResourceToMove() {
        return resourceToMove;
    }

    public Integer getAmount() {
        return amount;
    }

    public String getShelfID() {
        return shelfID;
    }
}
