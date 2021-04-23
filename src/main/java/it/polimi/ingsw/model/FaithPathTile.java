package it.polimi.ingsw.model;

import it.polimi.ingsw.parser.UniqueSerializableObject;
import it.polimi.ingsw.parser.raw.RawFaithPathTile;
import it.polimi.ingsw.server.Console;

/**
 * The FaithPathTile is an immutable class containing all the information about a tile of the faith path
 */
public class FaithPathTile implements UniqueSerializableObject<RawFaithPathTile> {
    private final int x, y;

    private final int order;
    private final int victoryPoints;

    private final int popeGroup;
    private final boolean popeCheck;

    /**
     * Creates a new tile
     * @param x the x coordinate od the tile. Must be positive
     * @param y the y coordinate od the tile. Must be positive
     * @param order the order of the tile, starting from zero
     * @param victoryPoints the victory points granted bu the tile, zero if none
     * @param popeGroup the group the tile belongs to, zero if none
     * @param popeCheck true if the tile is a Pope Check, false otherwise. Must be part of a group if true
     * @throws IllegalArgumentException if any of the parameter's conditions aren't met
     */
    public FaithPathTile(int x, int y, int order, int victoryPoints, int popeGroup, boolean popeCheck) {
        if(x <= 0 || y <= 0)
            throw new IllegalArgumentException("Coordinates must be positive");

        if(order < 0)
            throw new IllegalArgumentException("Order cannot be negative");

        if(victoryPoints < 0)
            throw new IllegalArgumentException("Victory points cannot be negative");

        if(popeGroup < 0)
            throw new IllegalArgumentException("The Pope Group must be positive");

        if(popeGroup == 0 && popeCheck)
            throw new IllegalArgumentException("Since popeCheck is set to true, the tile must be part of  a group");

        this.x = x;
        this.y = y;
        this.order = order;
        this.victoryPoints = victoryPoints;
        this.popeGroup = popeGroup;
        this.popeCheck = popeCheck;
    }

    /**
     * @return the x coordinate of the tile
     */
    public int getX() {
        return x;
    }

    /**
     * @return the y coordinate of the tile
     */
    public int getY() {
        return y;
    }

    /**
     * @return the order of the tile
     */
    public int getOrder() {
        return order;
    }

    /**
     * @return the amount of victory points granted by the tile
     */
    public int getVictoryPoints() {
        return victoryPoints;
    }

    /**
     * @return the Pope Group the tile belongs to
     */
    public int getPopeGroup() {
        return popeGroup;
    }

    /**
     * @return true if the tile is a Pope Check, false otherwise
     */
    public boolean isPopeCheck() {
        return popeCheck;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        str .append(getOrder())
            .append(") (")
            .append(getX())
            .append(", ")
            .append(getY())
            .append(") ")
            .append(getVictoryPoints())
            .append(" pts");

        if(getPopeGroup() != 0)
            str .append(" group ")
                .append(getPopeGroup());

        if(isPopeCheck())
            str.append(" CHECK");

        return str.toString();
    }

    @Override
    public void printDebugInfo() {
        Console.log(toString());
    }

    @Override
    public String getStringId() {
        return String.format("%03d", order);
    }
}
