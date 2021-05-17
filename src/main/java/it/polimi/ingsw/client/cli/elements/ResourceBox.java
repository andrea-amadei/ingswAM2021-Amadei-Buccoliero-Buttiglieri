package it.polimi.ingsw.client.cli.elements;

import it.polimi.ingsw.client.cli.MutablePositionedElement;
import it.polimi.ingsw.client.cli.OutputHandler;
import it.polimi.ingsw.exceptions.UnableToDrawElementException;
import it.polimi.ingsw.utils.BackgroundColor;
import it.polimi.ingsw.utils.ForegroundColor;

import java.util.Arrays;

public class ResourceBox implements MutablePositionedElement {
    private final String name;
    private boolean visible;
    private int row;
    private int column;
    private int zIndex;

    private String resource;

    private static final String[] ACCEPTED_RESOURCES = {"gold", "stone", "shield", "servant", "faith", "any"};
    private static final String[] RESOURCE_TEXT =      {"GO",   "ST",    "SH",     "SE",      "FA",    "??"};

    private static final ForegroundColor[] RESOURCE_FOREGROUND_COLOR = {
            ForegroundColor.WHITE_BRIGHT,
            ForegroundColor.WHITE_BRIGHT,
            ForegroundColor.WHITE_BRIGHT,
            ForegroundColor.WHITE_BRIGHT,
            ForegroundColor.WHITE_BRIGHT,
            ForegroundColor.BLACK
    };

    private static final BackgroundColor[] RESOURCE_BACKGROUND_COLOR = {
            BackgroundColor.YELLOW,
            BackgroundColor.BLACK_BRIGHT,
            BackgroundColor.BLUE_BRIGHT,
            BackgroundColor.PURPLE,
            BackgroundColor.RED,
            BackgroundColor.WHITE_BRIGHT
    };

    public ResourceBox(String name, int row, int column, String resource) {
        if(name == null)
            throw new NullPointerException();

        if(name.length() == 0)
            throw new IllegalArgumentException("Name cannot be empty");

        this.name = name;

        setStartingRow(row);
        setStartingColumn(column);
        setResource(resource);

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

    public void setResource(String resource) {
        if(resource == null)
            throw new NullPointerException();

        if(!Arrays.asList(ACCEPTED_RESOURCES).contains(resource.toLowerCase()))
            throw new IllegalArgumentException(resource + " is not an accepted resource");

        this.resource = resource.toLowerCase();
    }

    public String getResource() {
        return resource;
    }

    @Override
    public void draw(OutputHandler outputHandler) throws UnableToDrawElementException {
        if(!isVisible())
            return;

        int index = Arrays.asList(ACCEPTED_RESOURCES).indexOf(getResource());

        try {
            outputHandler.setString(getStartingRow(), getStartingColumn(),
                    RESOURCE_TEXT[index],
                    RESOURCE_FOREGROUND_COLOR[index],
                    RESOURCE_BACKGROUND_COLOR[index]);
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            throw new UnableToDrawElementException(e.getMessage());
        }
    }
}
