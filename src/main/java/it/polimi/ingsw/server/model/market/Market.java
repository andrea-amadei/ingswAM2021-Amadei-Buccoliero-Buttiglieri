package it.polimi.ingsw.server.model.market;

import it.polimi.ingsw.common.parser.SerializableObject;
import it.polimi.ingsw.common.parser.raw.RawMarket;
import it.polimi.ingsw.server.Logger;

import java.util.*;

/**
 * The market of the game. It handles all the marbles and the operations related to the market (pick row/column).
 * Every time a row or column is picked, the selected marbles are stored in a list.
 * The market is composed of a grid of marbles and the exceeding marble.
 */
public class Market implements SerializableObject<RawMarket> {
    private final Marble [][] grid;
    private Marble oddOne;
    private final transient int rowSize;
    private final transient int colSize;
    private List<Marble> selectedMarbles;

    /**
     * A new market is created with the specified configuration.
     * The selected marbles list is initially empty.
     * TThe builder will use this constructor.
     * @param grid an ordered list of all marbles to be added to the market
     * @param oddOne the outer marble
     * @param rowSize the rows size
     * @param colSize the columns size
     */
    public Market(List<Marble> grid, Marble oddOne, int rowSize, int colSize){
        selectedMarbles = new ArrayList<>();
        this.rowSize = rowSize;
        this.colSize = colSize;
        this.grid = new Marble[rowSize][colSize];

        if(grid.size() != rowSize * colSize)
            throw new IllegalArgumentException("The market grid must contain " + rowSize*colSize + "(" + rowSize + "*" + colSize
                    + ") marbles, but only " + grid.size() + " were provided");

        // add marbles to the grid
        Iterator<Marble> marbleIterator = grid.listIterator(0);

        for(int i = 0; i < rowSize; i++)
            for(int j = 0; j < colSize; j++)
                this.grid[i][j] = marbleIterator.next();

        // add the odd one
        this.oddOne = oddOne;
    }

    /**
     * Returns the number of rows of the market.
     * @return the number of rows of the market.
     */
    public int getRowSize(){
        return rowSize;
    }

    /**
     * Returns the number of columns of the market.
     * @return the number of columns of the market.
     */
    public int getColSize(){
        return colSize;
    }

    /**
     * Gets the marble corresponding to the specified coordinates (0-based)
     * @param row the specified row
     * @param col the specified column
     * @return the marble corresponding to the specified coordinates (0-based)
     * @throws IndexOutOfBoundsException if either the specified row or column is negative or too big
     */
    public Marble getMarble(int row, int col){
        if(row < 0 || row > rowSize - 1 || col < 0 || col > colSize - 1)
            throw new IndexOutOfBoundsException();

        return grid[row][col];
    }

    /**
     * Gets the odd marble (the one outside of the grid)
     * @return the odd marble
     */
    public Marble getOddOne() {
        return oddOne;
    }

    /**
     * Returns a copy of the list of marbles selected by a pickRow/pickCol operation
     * @return a copy of the list of marbles selected by a pickRow/pickCol operation
     */
    public List<Marble> getSelectedMarbles(){
        return new ArrayList<>(selectedMarbles);
    }

    /**
     * Clears the selected marbles
     */
    public void resetSelectedMarbles(){
        selectedMarbles.clear();
    }

    /**
     * Performs a pickRow operation. Marbles of the specified row are left-shifted.
     * The exceeding marble becomes the new extra marble.
     * The old extra marble becomes the right-first marble of the specified row.
     * The selectedMarbles list contains the marble of the specified row.
     * @param row the target row of the operation. 0-based
     * @throws IndexOutOfBoundsException if the specified row is negative or too big
     */
    public void pickRow(int row){
        if(row < 0 || row > rowSize - 1)
            throw new IndexOutOfBoundsException();

        selectedMarbles = new ArrayList<>();

        Marble tmp1 = grid[row][colSize-1];
        selectedMarbles.add(tmp1);
        Marble tmp2;
        for(int i = colSize - 2; i >= 0; i--){
            tmp2 = grid[row][i];
            grid[row][i] = tmp1;
            tmp1 = tmp2;
            selectedMarbles.add(tmp1);
        }

        grid[row][colSize - 1] = oddOne;
        oddOne = tmp1;
    }
    /**
     * Performs a pickCol operation. Marbles of the specified column are up-shifted.
     * The exceeding marble becomes the new extra marble.
     * The old extra marble becomes the bottom-first marble of the specified column.
     * The selectedMarbles list contains the marble of the specified column.
     * @param col the target column of the operation. 0-based
     * @throws IndexOutOfBoundsException if the specified column is negative or too big
     */
    public void pickCol(int col){
        if(col < 0 || col > colSize - 1)
            throw new IndexOutOfBoundsException();

        selectedMarbles = new ArrayList<>();

        Marble tmp1 = grid[rowSize - 1][col];
        selectedMarbles.add(tmp1);
        Marble tmp2;
        for(int i = rowSize - 2; i >= 0; i--){
            tmp2 = grid[i][col];
            grid[i][col] = tmp1;
            tmp1 = tmp2;
            selectedMarbles.add(tmp1);
        }

        grid[rowSize-1][col] = oddOne;
        oddOne = tmp1;
    }

    /**
     * Returns a representation of the grid. Example:
     *         P
     *         W B W Y
     *         P Y W G
     *         B G R W
     * @return a representation of the grid.
     */
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(oddOne.getColor().name().charAt(0)).append("\n");
        for(int i = 0; i < rowSize; i++){
            for(int j = 0; j < colSize; j++){
                sb.append(grid[i][j].getColor().name().charAt(0));
                if(j < colSize-1)
                    sb.append(" ");
            }
            if(i < rowSize - 1)
                sb.append("\n");
        }

        return sb.toString();
    }

    @Override
    public RawMarket toRaw() {
        return new RawMarket(this);
    }

    @Override
    public void printDebugInfo() {
        StringBuilder str;

        Logger.log(String.valueOf(oddOne.getColor().name().charAt(0)));

        for (int i = 0; i < rowSize; i++) {
            str = new StringBuilder();

            for (int j = 0; j < colSize; j++)
                    str.append(grid[i][j].getColor().name().charAt(0)).append(" ");

            Logger.log(str.toString());
        }
    }
}
