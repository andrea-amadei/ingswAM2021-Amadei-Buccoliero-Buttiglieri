package it.polimi.ingsw.client.cli.framework.elements;

import it.polimi.ingsw.client.cli.framework.MutablePositionedElement;
import it.polimi.ingsw.client.cli.framework.OutputHandler;
import it.polimi.ingsw.client.cli.framework.TextElement;
import it.polimi.ingsw.exceptions.UnableToDrawElementException;
import it.polimi.ingsw.utils.BackgroundColor;
import it.polimi.ingsw.utils.ForegroundColor;

public class TextBox implements TextElement, MutablePositionedElement {
    private final String name;
    private boolean visible;
    private int row;
    private int column;
    private int zIndex;
    private String text;
    private ForegroundColor foregroundColor;
    private BackgroundColor backgroundColor;
    private boolean foregroundColorVisible;
    private boolean backgroundColorVisible;

    public TextBox(String name, int row, int column, String text, ForegroundColor foregroundColor, BackgroundColor backgroundColor) {
        if(name == null)
            throw new NullPointerException();

        if(name.length() == 0)
            throw new IllegalArgumentException("Name cannot be empty");

        this.name = name;

        setStartingRow(row);
        setStartingColumn(column);
        setText(text);
        setForegroundColor(foregroundColor);
        setBackgroundColor(backgroundColor);

        setVisible(true);
        setBackgroundColorVisible(true);
        setForegroundColorVisible(true);
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

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        if(text == null)
            throw new NullPointerException();

        if(text.length() == 0)
            throw new IllegalArgumentException("Text cannot be empty");

        this.text = text;
    }

    @Override
    public ForegroundColor getForegroundColor() {
        return foregroundColor;
    }

    @Override
    public boolean isForegroundColorVisible() {
        return foregroundColorVisible;
    }

    @Override
    public BackgroundColor getBackgroundColor() {
        return backgroundColor;
    }

    @Override
    public boolean isBackgroundColorVisible() {
        return backgroundColorVisible;
    }

    @Override
    public void setForegroundColor(ForegroundColor foregroundColor) {
        if(foregroundColor == null)
            throw new NullPointerException();

        this.foregroundColor = foregroundColor;
    }

    @Override
    public void setForegroundColorVisible(boolean foregroundColorVisible) {
        this.foregroundColorVisible = foregroundColorVisible;
    }

    @Override
    public void setBackgroundColor(BackgroundColor backgroundColor) {
        if(backgroundColor == null)
            throw new NullPointerException();

        this.backgroundColor = backgroundColor;
    }

    @Override
    public void setBackgroundColorVisible(boolean backgroundColorVisible) {
        this.backgroundColorVisible = backgroundColorVisible;
    }

    @Override
    public void draw(OutputHandler outputHandler) throws UnableToDrawElementException {
        if(!visible)
            return;

        try {
            outputHandler.setString(getStartingRow(), getStartingColumn(), getText());
        } catch(IllegalArgumentException e) {
            throw new UnableToDrawElementException("Unable to draw element " + getName() + ": " + e.getMessage());
        }

        if(foregroundColorVisible)
            outputHandler.setForegroundColorRectangle(  getStartingRow(), getStartingColumn(),
                                                        getStartingRow(), getStartingColumn() + getText().length() - 1,
                                                        getForegroundColor());

        if(backgroundColorVisible)
            outputHandler.setBackgroundColorRectangle(  getStartingRow(), getStartingColumn(),
                                                        getStartingRow(), getStartingColumn() + getText().length() - 1,
                                                        getBackgroundColor());
    }
}
