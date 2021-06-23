package it.polimi.ingsw.client.gui.beans;

import it.polimi.ingsw.model.production.Production;

public class CraftingSelectionBean {
    private Production.CraftingType craftingType;
    private int index;

    public Production.CraftingType getCraftingType() {
        return craftingType;
    }

    public void setCraftingType(Production.CraftingType craftingType) {
        this.craftingType = craftingType;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
