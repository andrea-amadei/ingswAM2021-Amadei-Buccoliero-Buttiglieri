package it.polimi.ingsw.client.cli.framework.elements;

import it.polimi.ingsw.client.cli.framework.MutableRectangularElement;
import it.polimi.ingsw.client.cli.framework.OutputHandler;
import it.polimi.ingsw.client.cli.framework.TextElement;
import it.polimi.ingsw.common.exceptions.UnableToDrawElementException;
import it.polimi.ingsw.common.utils.BackgroundColor;
import it.polimi.ingsw.common.utils.ForegroundColor;

public class FixedTextBox implements TextElement, MutableRectangularElement {
    private final String name;
    private boolean visible;
    private int startingRow;
    private int startingColumn;
    private int zIndex;
    private String text;
    private ForegroundColor foregroundColor;
    private BackgroundColor backgroundColor;
    private boolean foregroundColorVisible;
    private boolean backgroundColorVisible;

    private final int size;
    private boolean alignLeft;

    public FixedTextBox(String name, int startingRow, int startingColumn, int size, String text, ForegroundColor foregroundColor, BackgroundColor backgroundColor) {
        if(name == null)
            throw new NullPointerException();

        if(name.length() == 0)
            throw new IllegalArgumentException("Name cannot be empty");

        this.name = name;

        if(size <= 0)
            throw new IllegalArgumentException("Size must be positive");

        this.size = size;

        setStartingRow(startingRow);
        setStartingColumn(startingColumn);
        setText(text);
        setForegroundColor(foregroundColor);
        setBackgroundColor(backgroundColor);

        setVisible(true);
        setBackgroundColorVisible(true);
        setForegroundColorVisible(true);
        setZIndex(1);

        setAlignLeft(true);
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
        return startingRow;
    }

    @Override
    public int getStartingColumn() {
        return startingColumn;
    }

    @Override
    public int getEndingRow() {
        return startingRow;
    }

    @Override
    public int getEndingColumn() {
        return startingColumn + size - 1;
    }

    @Override
    public void setStartingRow(int row) {
        if(row < 0)
            throw new IllegalArgumentException("Row must cannot be negative");

        this.startingRow = row;
    }

    @Override
    public void setStartingColumn(int column) {
        if(column < 0)
            throw new IllegalArgumentException("Row must cannot be negative");

        this.startingColumn = column;
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

        if(text.length() > size)
            throw new IllegalArgumentException("Text length cannot be bigger than the size of the text box");

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

    public boolean isAlignLeft() {
        return alignLeft;
    }

    public void setAlignLeft(boolean alignLeft) {
        this.alignLeft = alignLeft;
    }

    public int getSize() {
        return size;
    }

    @Override
    public void draw(OutputHandler outputHandler) throws UnableToDrawElementException {
        if(!visible)
            return;

        try {
            if(alignLeft) {
                outputHandler.setString(
                        getStartingRow(),
                        getStartingColumn(),
                        getText());

                if(getSize() - getText().length() > 0)
                    outputHandler.setString(
                            getStartingRow(),
                            getStartingColumn() + getText().length(),
                            String.valueOf(outputHandler.getBlank()).repeat(getSize() - getText().length()));
            }
            else {
                outputHandler.setString(
                        getStartingRow(),
                        getStartingColumn() + (getSize() - getText().length()),
                        getText());

                if(getSize() - getText().length() > 0)
                    outputHandler.setString(
                            getStartingRow(),
                            getStartingColumn(),
                            String.valueOf(outputHandler.getBlank()).repeat(getSize() - getText().length()));
            }
        } catch(IllegalArgumentException e) {
            throw new UnableToDrawElementException("Unable to draw element " + getName() + ": " + e.getMessage());
        }

        if(foregroundColorVisible)
            outputHandler.setForegroundColorRectangle(  getStartingRow(), getStartingColumn(),
                    getStartingRow(), getStartingColumn() + getSize() - 1,
                    getForegroundColor());

        if(backgroundColorVisible)
            outputHandler.setBackgroundColorRectangle(  getStartingRow(), getStartingColumn(),
                    getStartingRow(), getStartingColumn() + getSize() - 1,
                    getBackgroundColor());
    }
}
