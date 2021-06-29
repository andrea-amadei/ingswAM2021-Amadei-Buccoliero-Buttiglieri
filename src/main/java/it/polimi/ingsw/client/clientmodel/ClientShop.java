package it.polimi.ingsw.client.clientmodel;

import it.polimi.ingsw.client.observables.Listener;
import it.polimi.ingsw.client.observables.Observable;
import it.polimi.ingsw.common.parser.raw.RawCraftingCard;

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

    public synchronized void changeCard(int row, int col, RawCraftingCard card){
        grid[row][col] = card;
        update();
    }

    public synchronized void removeCard(int row, int col){
        grid[row][col] = null;
        update();
    }

    public synchronized void selectCard(int row, int col){
        selectedCardRow = row;
        selectedCardCol = col;
        update();
    }

    public synchronized void unselect(){
        selectedCardRow = null;
        selectedCardCol = null;
        update();
    }

    public synchronized int getRowSize() {
        return rowSize;
    }

    public synchronized int getColSize() {
        return colSize;
    }

    public synchronized RawCraftingCard[][] getGrid() {
        RawCraftingCard[][] gridClone = new RawCraftingCard[grid.length][];
        for(int i = 0; i < grid.length; i++)
            gridClone[i] = grid[i].clone();

        return gridClone;
    }

    public synchronized Integer getSelectedCardRow() {
        return selectedCardRow;
    }

    public synchronized Integer getSelectedCardCol() {
        return selectedCardCol;
    }

    @Override
    public synchronized void addListener(Listener<ClientShop> listener) {
        listeners.add(listener);
    }

    @Override
    public synchronized void update() {
        for(Listener<ClientShop> l : listeners)
            l.update(this);
    }
}
