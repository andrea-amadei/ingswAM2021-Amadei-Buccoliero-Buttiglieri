package it.polimi.ingsw.client.cli.framework.elements;

import it.polimi.ingsw.client.cli.framework.MutablePositionedElement;
import it.polimi.ingsw.client.cli.framework.OutputHandler;
import it.polimi.ingsw.common.exceptions.UnableToDrawElementException;
import it.polimi.ingsw.server.model.basetypes.MarbleColor;
import it.polimi.ingsw.common.utils.BackgroundColor;
import it.polimi.ingsw.common.utils.ForegroundColor;

import java.util.Arrays;

public class MarbleBox implements MutablePositionedElement {
    private final String name;
    private boolean visible;
    private int row;
    private int column;
    private int zIndex;

    private MarbleColor marble;

    private static final MarbleColor[] ACCEPTED_MARBLES = {MarbleColor.YELLOW, MarbleColor.GREY, MarbleColor.PURPLE, MarbleColor.BLUE,
                                                            MarbleColor.RED, MarbleColor.WHITE};

    private static final String[] MARBLE_TEXT = {"Y", "G", "P", "B", "R", "W"};

    private static final ForegroundColor[] MARBLE_FOREGROUND_COLOR = {
            ForegroundColor.WHITE_BRIGHT,
            ForegroundColor.WHITE_BRIGHT,
            ForegroundColor.WHITE_BRIGHT,
            ForegroundColor.WHITE_BRIGHT,
            ForegroundColor.WHITE_BRIGHT,
            ForegroundColor.BLACK
    };

    private static final BackgroundColor[] MARBLE_BACKGROUND_COLOR = {
            BackgroundColor.YELLOW,
            BackgroundColor.BLACK_BRIGHT,
            BackgroundColor.PURPLE_BRIGHT,
            BackgroundColor.BLUE_BRIGHT,
            BackgroundColor.RED,
            BackgroundColor.WHITE_BRIGHT
    };

    public MarbleBox(String name, int row, int column, MarbleColor marble) {
        if(name == null)
            throw new NullPointerException();

        if(name.length() == 0)
            throw new IllegalArgumentException("Name cannot be empty");

        this.name = name;

        setStartingRow(row);
        setStartingColumn(column);
        setMarble(marble);

        setVisible(true);
        setZIndex(1);
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

    public MarbleColor getMarble() {
        return marble;
    }

    public void setMarble(MarbleColor marble) {
        if(marble == null)
            throw new NullPointerException();

        if(!Arrays.asList(ACCEPTED_MARBLES).contains(marble))
            throw new IllegalArgumentException(marble + " is not an accepted marble");

        this.marble = marble;
    }

    @Override
    public void draw(OutputHandler outputHandler) throws UnableToDrawElementException {
        if(!isVisible())
            return;

        int index = Arrays.asList(ACCEPTED_MARBLES).indexOf(getMarble());

        try {
            outputHandler.setBackgroundColorRectangle(getStartingRow(), getStartingColumn(),
                    getStartingRow() + 2, getStartingColumn() + 6,
                    MARBLE_BACKGROUND_COLOR[index]);

            outputHandler.setString(getStartingRow() + 1, getStartingColumn() + 3,
                    MARBLE_TEXT[index], MARBLE_FOREGROUND_COLOR[index], MARBLE_BACKGROUND_COLOR[index]);

        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            throw new UnableToDrawElementException(e.getMessage());
        }
    }
}
