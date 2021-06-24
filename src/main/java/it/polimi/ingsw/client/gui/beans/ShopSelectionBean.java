package it.polimi.ingsw.client.gui.beans;

public class ShopSelectionBean {
    private int row;
    private int col;
    private int upgradableIndex;
    private int upgradableSlotsCount;

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getUpgradableIndex() {
        return upgradableIndex;
    }

    public void setUpgradableIndex(int upgradableIndex) {
        this.upgradableIndex = upgradableIndex;
    }

    public int getUpgradableSlotsCount() {
        return upgradableSlotsCount;
    }

    public void setUpgradableSlotsCount(int upgradableSlotsCount) {
        this.upgradableSlotsCount = upgradableSlotsCount;
    }
}
