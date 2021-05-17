package it.polimi.ingsw.common.payload_components.groups.actions;

import it.polimi.ingsw.annotations.SerializedType;
import it.polimi.ingsw.model.production.Production;

@SerializedType("select_crafting")
public class SelectCraftingActionPayloadComponent extends ActionPayloadComponent{

    private final Production.CraftingType craftingType;
    private final Integer index;

    public SelectCraftingActionPayloadComponent(String player, Production.CraftingType craftingType, int index) {
        super(player);
        this.craftingType = craftingType;
        this.index = index;
    }

    public Production.CraftingType getCraftingType() {
        return craftingType;
    }

    public int getIndex() {
        return index;
    }
}
