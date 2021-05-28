package it.polimi.ingsw.client.cli.framework.elements;

import it.polimi.ingsw.client.cli.framework.MutablePositionedElement;
import it.polimi.ingsw.client.cli.framework.OutputHandler;
import it.polimi.ingsw.client.cli.framework.TextElement;
import it.polimi.ingsw.exceptions.UnableToDrawElementException;
import it.polimi.ingsw.utils.BackgroundColor;
import it.polimi.ingsw.utils.ForegroundColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LongTextBox implements TextElement, MutablePositionedElement {
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

    private final int maxLineSize;
    private final int maxLines;
    private transient ArrayList<String> textInLines;

    private boolean overflowEnabled;

    public LongTextBox(String name, int row, int column, String text, int maxLineSize, int maxLines, ForegroundColor foregroundColor, BackgroundColor backgroundColor) {
        if(name == null)
            throw new NullPointerException();

        if(name.length() == 0)
            throw new IllegalArgumentException("Name cannot be empty");

        this.name = name;

        if(maxLineSize < 1)
            throw new IllegalArgumentException("Size must be 1 or greater");
        this.maxLineSize = maxLineSize;

        if(maxLines < 1)
            throw new IllegalArgumentException("Lines must be 1 or greater");
        this.maxLines = maxLines;

        setStartingRow(row);
        setStartingColumn(column);
        setText(text);
        setForegroundColor(foregroundColor);
        setBackgroundColor(backgroundColor);

        setVisible(true);
        setBackgroundColorVisible(true);
        setForegroundColorVisible(true);
        setZIndex(1);
        setOverflowEnabled(false);
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

        ArrayList<String> newTextInLines = new ArrayList<>();

        List<List<String>> words = Arrays.stream(text.trim().split("[\\r\\n]+"))
                .map(x -> Arrays.stream(x.split("\\s+")).collect(Collectors.toList()))
                .collect(Collectors.toList());

        boolean newLineBreak = true;

        for(int i = 0; i < words.size(); i++) {
            for(String word : words.get(i)) {
                if(newLineBreak || newTextInLines.get(newTextInLines.size() - 1).length() + word.length() + 1 > getMaxLineSize()) {

                    if(newTextInLines.size() - 1 >= getMaxLines())
                        if(isOverflowEnabled())
                            break;
                        else
                            throw new IllegalArgumentException("Text too big to fit");

                    newTextInLines.add(word);
                }
                else
                    newTextInLines.set(newTextInLines.size() - 1, newTextInLines.get(newTextInLines.size() - 1) + " " + word);

                newLineBreak = false;
            }

            if(i == words.size() - 1)
                break;

            if(newTextInLines.size() - 1 >= getMaxLines())
                if(isOverflowEnabled())
                    break;
                else
                    throw new IllegalArgumentException("Text too big to fit");

            newLineBreak = true;
        }

        this.text = text;
        this.textInLines = newTextInLines;
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

    public int getMaxLineSize() {
        return maxLineSize;
    }

    public int getMaxLines() {
        return maxLines;
    }

    public List<String> getLines() {
        return new ArrayList<>(textInLines);
    }

    public boolean isOverflowEnabled() {
        return overflowEnabled;
    }

    public void setOverflowEnabled(boolean overflowEnabled) {
        this.overflowEnabled = overflowEnabled;
    }

    @Override
    public void draw(OutputHandler outputHandler) throws UnableToDrawElementException {
        if(!isVisible())
            return;

        try {
            for(int i = 0; i < textInLines.size(); i++)
                outputHandler.setString(getStartingRow() + i, getStartingColumn(), textInLines.get(i));
        } catch(IllegalArgumentException e) {
            throw new UnableToDrawElementException("Unable to draw element " + getName() + ": " + e.getMessage());
        }

        if(foregroundColorVisible)
            outputHandler.setForegroundColorRectangle(getStartingRow(), getStartingColumn(),
                    getStartingRow() + getMaxLines(), getStartingColumn() + getMaxLineSize(),
                    getForegroundColor());

        if(backgroundColorVisible)
            outputHandler.setBackgroundColorRectangle(getStartingRow(), getStartingColumn(),
                    getStartingRow() + getMaxLines(), getStartingColumn() + getMaxLineSize(),
                    getBackgroundColor());
    }
}
