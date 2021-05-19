package it.polimi.ingsw.client.cli.framework.elements;

import it.polimi.ingsw.client.cli.framework.MutablePositionedElement;
import it.polimi.ingsw.client.cli.framework.OutputHandler;
import it.polimi.ingsw.exceptions.UnableToDrawElementException;
import it.polimi.ingsw.parser.raw.RawFaithPathGroup;
import it.polimi.ingsw.parser.raw.RawFaithPathTile;
import it.polimi.ingsw.utils.BackgroundColor;
import it.polimi.ingsw.utils.ForegroundColor;

import java.util.Comparator;
import java.util.List;

public class FaithPath implements MutablePositionedElement {
    private final String name;
    private boolean visible;
    private int row;
    private int column;
    private int zIndex;

    private List<RawFaithPathTile> tiles;
    private List<RawFaithPathGroup> groups;
    private int activeTile;

    public FaithPath(String name, int row, int column, List<RawFaithPathTile> tiles, List<RawFaithPathGroup> groups) {
        if(name == null)
            throw new NullPointerException();

        if(name.length() == 0)
            throw new IllegalArgumentException("Name cannot be empty");

        this.name = name;

        setStartingRow(row);
        setStartingColumn(column);

        setVisible(true);
        setZIndex(1);

        setTiles(tiles);
        setGroups(groups);
        setActiveTile(0);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public int getStartingRow() {
        return row;
    }

    @Override
    public int getStartingColumn() {
        return column;
    }

    @Override
    public void setStartingRow(int row) {
        if(row < 0)
            throw new IllegalArgumentException("Row must cannot be negative");

        this.row = row;
    }

    @Override
    public void setStartingColumn(int column) {
        if(column < 0)
            throw new IllegalArgumentException("Row must cannot be negative");

        this.column = column;
    }

    @Override
    public void setStartingPosition(int row, int column) {
        setStartingRow(row);
        setStartingColumn(column);
    }

    @Override
    public void setZIndex(int ZIndex) {
        this.zIndex = ZIndex;
    }

    @Override
    public int getZIndex() {
        return zIndex;
    }

    public List<RawFaithPathTile> getTiles() {
        return tiles;
    }

    public List<RawFaithPathGroup> getGroups() {
        return groups;
    }

    public void setTiles(List<RawFaithPathTile> tiles) {
        if(tiles == null)
            throw new NullPointerException();

        if(tiles.size() == 0)
            throw new IllegalArgumentException("Tiles is empty");

        this.tiles = tiles;
    }

    public void setGroups(List<RawFaithPathGroup> groups) {
        if(groups == null)
            throw new NullPointerException();

        if(groups.size() == 0)
            throw new IllegalArgumentException("Groups is empty");

        this.groups = groups;
    }

    public int getActiveTile() {
        return activeTile;
    }

    public void setActiveTile(int activeTile) {
        this.activeTile = activeTile;
    }

    @Override
    public void draw(OutputHandler outputHandler) throws UnableToDrawElementException {
        int maxHeight = getTiles().stream().max(Comparator.comparing(RawFaithPathTile::getY)).orElseThrow().getY() * 3;
        int maxLength = getTiles().stream().max(Comparator.comparing(RawFaithPathTile::getX)).orElseThrow().getX() * 6;

        String points;
        String order;
        String group;
        BackgroundColor bg;
        ForegroundColor fg;

        for(RawFaithPathTile i : getTiles()) {
            if(i.getVictoryPoints() == 0)
                points = "  ";
            else
                points = "+" + i.getVictoryPoints();

            if(i.getPopeGroup() == 0)
                group = "  ";
            else
                group = "G" + i.getPopeGroup();

            if(i.getOrder() < 10)
                order = "  " + i.getOrder();
            else if(i.getOrder() < 100)
                order = " " + i.getOrder();
            else
                order = String.valueOf(i.getOrder());

            if(i.isPopeCheck()) {
                bg = BackgroundColor.RED;
                fg = ForegroundColor.WHITE_BRIGHT;
            }
            else if(i.getVictoryPoints() != 0) {
                bg = BackgroundColor.YELLOW_BRIGHT;
                fg = ForegroundColor.BLACK_BRIGHT;
            }
            else if(i.getOrder() % 2 == 0) {
                bg = BackgroundColor.BLACK_BRIGHT;
                fg = ForegroundColor.WHITE_BRIGHT;
            }
            else {
                bg = BackgroundColor.WHITE;
                fg = ForegroundColor.BLACK_BRIGHT;
            }

            if(i.getOrder() == getActiveTile()) {
                fg = ForegroundColor.BLUE_BRIGHT;
            }

            outputHandler.setBackgroundColorRectangle(
                    getStartingRow() + maxHeight - i.getY() * 3,
                    getStartingColumn() + i.getX() * 6,
                    getStartingRow() + maxHeight - i.getY() * 3 + 2,
                    getStartingColumn() + i.getX() * 6 + 5,
                    bg
            );

            outputHandler.setString(
                    getStartingRow() + maxHeight - i.getY() * 3,
                    getStartingColumn() + i.getX() * 6,
                    points,
                    fg
            );

            outputHandler.setString(
                    getStartingRow() + maxHeight - i.getY() * 3 + 1,
                    getStartingColumn() + i.getX() * 6 + 1,
                    order,
                    fg
            );

            outputHandler.setString(
                    getStartingRow() + maxHeight - i.getY() * 3 + 2,
                    getStartingColumn() + i.getX() * 6,
                    group,
                    fg
            );
        }

        for(RawFaithPathGroup i : getGroups()) {
            if(i.getGroup() % 3 == 1)
                bg = BackgroundColor.YELLOW;
            else if(i.getGroup() % 3 == 2)
                bg = BackgroundColor.RED_BRIGHT;
            else
                bg = BackgroundColor.RED;

            outputHandler.setBackgroundColorRectangle(
                    getStartingRow() + (i.getGroup() - 1) * 3 + 1,
                    getStartingColumn() + maxLength + 8,
                    getStartingRow() + (i.getGroup() - 1) * 3 + 2,
                    getStartingColumn() + maxLength + 9 + 18,
                    bg
            );

            outputHandler.setString(
                    getStartingRow() + (i.getGroup() - 1) * 3 + 1,
                    getStartingColumn() + maxLength + 9,
                    "G" + i.getGroup() + ": Pope Group " + i.getGroup(),
                    ForegroundColor.WHITE_BRIGHT
            );

            outputHandler.setString(
                    getStartingRow() + (i.getGroup() - 1) * 3 + 2,
                    getStartingColumn() + maxLength + 9,
                    "+" + i.getPoints() + " points",
                    ForegroundColor.WHITE_BRIGHT
            );

            //TODO: dynamically change status
        }
    }
}
