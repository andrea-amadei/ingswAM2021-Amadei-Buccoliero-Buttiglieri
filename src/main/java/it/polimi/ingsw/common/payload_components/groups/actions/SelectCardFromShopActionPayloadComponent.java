package it.polimi.ingsw.common.payload_components.groups.actions;

import it.polimi.ingsw.common.annotations.SerializedType;

@SerializedType("select_card_from_shop")
public class SelectCardFromShopActionPayloadComponent extends ActionPayloadComponent{

    private final Integer row;
    private final Integer col;
    private final Integer upgradableCraftingId;

    public SelectCardFromShopActionPayloadComponent(String player, int row, int col, int upgradableCraftingId) {
        super(player);
        this.row = row;
        this.col = col;
        this.upgradableCraftingId = upgradableCraftingId;
    }

    public Integer getRow() {
        return row;
    }

    public Integer getCol() {
        return col;
    }

    public Integer getUpgradableCraftingId() {
        return upgradableCraftingId;
    }
}
