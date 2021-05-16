package it.polimi.ingsw.clientproto.model;

import it.polimi.ingsw.clientproto.observables.Listener;
import it.polimi.ingsw.clientproto.observables.Observable;
import it.polimi.ingsw.parser.raw.RawCraftingCard;

import java.util.ArrayList;
import java.util.List;

public class ClientShop implements Observable<ClientShop> {

    private final List<Listener<ClientShop>> listeners;

    private final int rowSize;
    private final int colSize;
    private final RawCraftingCard [][] grid;
    private Integer selectedCardRow;
    private Integer selectedCardCol;

    /**
     * Creates a new ClientShop. At the beginning all elements of the grid are null
     * @param rowSize the rowSize of this shop
     * @param colSize the colSize of this shop
     */
    public ClientShop(int rowSize, int colSize){
        this.rowSize = rowSize;
        this.colSize = colSize;
        this.grid = new RawCraftingCard[rowSize][colSize];

        this.selectedCardRow = null;
        this.selectedCardCol = null;

        listeners = new ArrayList<>();
    }

    public void changeCard(int row, int col, RawCraftingCard card){
        grid[row][col] = card;
        update();
    }

    public void removeCard(int row, int col){
        grid[row][col] = null;
        update();
    }

    public void selectCard(int row, int col){
        selectedCardRow = row;
        selectedCardCol = col;
        update();
    }

    public void unselect(){
        selectedCardRow = null;
        selectedCardCol = null;
        update();
    }

    public int getRowSize() {
        return rowSize;
    }

    public int getColSize() {
        return colSize;
    }

    public RawCraftingCard[][] getGrid() {
        return grid;
    }

    public Integer getSelectedCardRow() {
        return selectedCardRow;
    }

    public Integer getSelectedCardCol() {
        return selectedCardCol;
    }

    @Override
    public void addListener(Listener<ClientShop> listener) {
        listeners.add(listener);
    }

    @Override
    public void update() {
        for(Listener<ClientShop> l : listeners)
            l.update(this);
    }
}
