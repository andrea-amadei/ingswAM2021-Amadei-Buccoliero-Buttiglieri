package it.polimi.ingsw.common.payload_components.groups.actions;

import it.polimi.ingsw.common.annotations.SerializedType;

@SerializedType("buy_from_market")
public class BuyFromMarketActionPayloadComponent extends ActionPayloadComponent{
    private final Boolean isRow;
    private final Integer index;

    public BuyFromMarketActionPayloadComponent(String player, boolean isRow, int index) {
        super(player);
        this.isRow = isRow;
        this.index = index;
    }

    public boolean isRow() {
        return isRow;
    }

    public Integer getIndex() {
        return index;
    }
}
