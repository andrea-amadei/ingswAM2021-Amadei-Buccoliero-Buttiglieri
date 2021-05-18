package it.polimi.ingsw.client.cli.framework.elements;

import it.polimi.ingsw.client.cli.framework.MutablePositionedElement;
import it.polimi.ingsw.client.cli.framework.OutputHandler;
import it.polimi.ingsw.exceptions.UnableToDrawElementException;
import it.polimi.ingsw.utils.BackgroundColor;
import it.polimi.ingsw.utils.ForegroundColor;

import java.util.Arrays;

public class FlagBox implements MutablePositionedElement {

    private enum FlagShape {
        RECTANGLE,
        TRIANGLE,
        SWALLOWTAIL,
        CIRCLE
    }

    private final String name;
    private boolean visible;
    private int row;
    private int column;
    private int zIndex;

    private int level;
    private String color;
    private boolean blockCharactersEnabled;

    private static final String[] LEVELS = {"   ", " I ", "II ", "III"};

    private static final String[] ACCEPTED_COLORS = {"blue", "green", "purple", "yellow"};

    private static final FlagShape[] FLAG_SHAPE = {
            FlagShape.TRIANGLE,
            FlagShape.RECTANGLE,
            FlagShape.SWALLOWTAIL,
            FlagShape.CIRCLE
    };

    private static final ForegroundColor[] FLAG_FOREGROUND_COLOR = {
            ForegroundColor.WHITE_BRIGHT,
            ForegroundColor.WHITE_BRIGHT,
            ForegroundColor.WHITE_BRIGHT,
            ForegroundColor.WHITE_BRIGHT
    };

    private static final BackgroundColor[] FLAG_BACKGROUND_COLOR = {
            BackgroundColor.BLUE_BRIGHT,
            BackgroundColor.GREEN_BRIGHT,
            BackgroundColor.PURPLE,
            BackgroundColor.YELLOW
    };

    public FlagBox(String name, int row, int column, int level, String color) {
        if(name == null)
            throw new NullPointerException();

        if(name.length() == 0)
            throw new IllegalArgumentException("Name cannot be empty");

        this.name = name;

        setStartingRow(row);
        setStartingColumn(column);
        setLevel(level);
        setColor(color);

        setVisible(true);
        setZIndex(1);
        setBlockCharactersEnabled(true);
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        if(color == null)
            throw new NullPointerException();

        if(!Arrays.asList(ACCEPTED_COLORS).contains(color.toLowerCase()))
            throw new IllegalArgumentException(color + " is not an accepted color");

        this.color = color.toLowerCase();
    }

    public boolean isBlockCharactersEnabled() {
        return blockCharactersEnabled;
    }

    public void setBlockCharactersEnabled(boolean blockCharactersEnabled) {
        this.blockCharactersEnabled = blockCharactersEnabled;
    }

    @Override
    public void draw(OutputHandler outputHandler) throws UnableToDrawElementException {
        if(!isVisible())
            return;

        int index = Arrays.asList(ACCEPTED_COLORS).indexOf(getColor());
        String str;

        if(getLevel() < 0 || getLevel() > LEVELS.length - 1 )
            if (getLevel() < 0)
                if(getLevel() > -10)
                    str = " " + getLevel();
                else if(getLevel() > -100)
                    str = String.valueOf(getLevel());
                else
                    throw new UnableToDrawElementException("Level is too small to be drawn");
            else
                if(getLevel() < 10)
                    str = " " + getLevel() + " ";
                else if(getLevel() < 100)
                    str = " " + getLevel();
                else if(getLevel() < 1000)
                    str = String.valueOf(getLevel());
                else
                    throw new UnableToDrawElementException("Level is too big to be drawn");
        else
            str = LEVELS[getLevel()];

        try {
            outputHandler.setBackgroundColorRectangle(
                    getStartingRow(),
                    getStartingColumn(),
                    getStartingRow() + 2,
                    getStartingColumn() + 2,
                    FLAG_BACKGROUND_COLOR[index]
            );

            outputHandler.setString(
                    getStartingRow() + 1,
                    getStartingColumn(),
                    str,
                    FLAG_FOREGROUND_COLOR[index]
            );

            switch(FLAG_SHAPE[index]) {
                case TRIANGLE:
                case CIRCLE:
                    if (isBlockCharactersEnabled()) {
                        outputHandler.setForegroundColor(getStartingRow() + 2, getStartingColumn(), ForegroundColor.BLACK);
                        outputHandler.setChar(getStartingRow() + 2, getStartingColumn(), '▄');

                        outputHandler.setForegroundColor(getStartingRow() + 2, getStartingColumn() + 2, ForegroundColor.BLACK);
                        outputHandler.setChar(getStartingRow() + 2, getStartingColumn() + 2, '▄');
                    } else {
                        outputHandler.setBackgroundColor(getStartingRow() + 2, getStartingColumn(), OutputHandler.getDefaultBackgroundColor());
                        outputHandler.setBackgroundColor(getStartingRow() + 2, getStartingColumn() + 2, OutputHandler.getDefaultBackgroundColor());
                    }

                    break;

                case SWALLOWTAIL:
                    if (isBlockCharactersEnabled()) {
                        outputHandler.setForegroundColor(getStartingRow() + 2, getStartingColumn() + 1, ForegroundColor.BLACK);
                        outputHandler.setChar(getStartingRow() + 2, getStartingColumn() + 1, '▄');
                    } else
                        outputHandler.setBackgroundColor(getStartingRow() + 2, getStartingColumn() + 1, OutputHandler.getDefaultBackgroundColor());

                    break;
            }
        } catch (IndexOutOfBoundsException e) {
            throw new UnableToDrawElementException(e.getMessage());
        }
    }
}
