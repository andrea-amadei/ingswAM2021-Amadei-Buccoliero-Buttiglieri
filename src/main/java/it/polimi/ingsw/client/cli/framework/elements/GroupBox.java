package it.polimi.ingsw.client.cli.framework.elements;

import it.polimi.ingsw.client.cli.framework.MutableResizableRectangularElement;
import it.polimi.ingsw.client.cli.framework.OutputHandler;
import it.polimi.ingsw.client.cli.framework.TextElement;
import it.polimi.ingsw.client.cli.framework.VisibleElement;
import it.polimi.ingsw.exceptions.UnableToDrawElementException;
import it.polimi.ingsw.utils.BackgroundColor;
import it.polimi.ingsw.utils.ForegroundColor;

import java.util.List;

public class GroupBox extends Group implements MutableResizableRectangularElement, TextElement {
    private int startingRow;
    private int startingColumn;
    private int endingRow;
    private int endingColumn;

    private String text;
    private ForegroundColor foregroundColor;
    private BackgroundColor backgroundColor;
    private boolean foregroundColorVisible;
    private boolean backgroundColorVisible;

    private boolean doubleBorder;
    private boolean alignLeft;

    public GroupBox(String name, int startingRow, int startingColumn, int endingRow, int endingColumn, String text, ForegroundColor foregroundColor, BackgroundColor backgroundColor) {
        super(name);

        setStartingRow(startingRow);
        setStartingColumn(startingColumn);
        setEndingRow(endingRow);
        setEndingColumn(endingColumn);
        setText(text);

        setForegroundColor(foregroundColor);
        setBackgroundColor(backgroundColor);

        setForegroundColorVisible(true);
        setBackgroundColorVisible(true);

        setAlignLeft(true);
        setDoubleBorder(false);
    }

    public GroupBox(String name, int startingRow, int startingColumn, int endingRow, int endingColumn, String text, ForegroundColor foregroundColor, BackgroundColor backgroundColor, List<VisibleElement> elements) {
        super(name, elements);

        setStartingRow(startingRow);
        setStartingColumn(startingColumn);
        setEndingRow(endingRow);
        setEndingColumn(endingColumn);
        setText(text);

        setForegroundColor(foregroundColor);
        setBackgroundColor(backgroundColor);

        setForegroundColorVisible(true);
        setBackgroundColorVisible(true);

        setAlignLeft(true);
        setDoubleBorder(false);
    }

    @Override
    public void setStartingRow(int row) {
        if(row < 0)
            throw new IllegalArgumentException("Row cannot be negative");

        startingRow = row;
    }

    @Override
    public void setStartingColumn(int column) {
        if(column < 0)
            throw new IllegalArgumentException("Column cannot be negative");

        startingColumn = column;
    }

    @Override
    public void setStartingPosition(int row, int column) {
        setStartingRow(row);
        setStartingColumn(column);
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
    public void setEndingRow(int row) {
        if(row < 0)
            throw new IllegalArgumentException("Row cannot be negative");

        endingRow = row;
    }

    @Override
    public void setEndingColumn(int column) {
        if(column < 0)
            throw new IllegalArgumentException("Column cannot be negative");

        endingColumn = column;
    }

    @Override
    public void setEndingPosition(int row, int column) {
        setStartingRow(row);
        setStartingColumn(column);
    }

    @Override
    public int getEndingRow() {
        return endingRow;
    }

    @Override
    public int getEndingColumn() {
        return endingColumn;
    }

    public boolean isDoubleBorder() {
        return doubleBorder;
    }

    public void setDoubleBorder(boolean doubleBorder) {
        this.doubleBorder = doubleBorder;
    }

    @Override
    public String getText() {
        return text;
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
    public void setText(String text) {
        if(text == null)
            throw new NullPointerException();

        if(text.length() == 0)
            throw new IllegalArgumentException("Text cannot be empty");

        this.text = text;
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

    @Override
    public void draw(OutputHandler outputHandler) throws UnableToDrawElementException {
        if(!isVisible())
            return;

        StringBuilder str = new StringBuilder(getText());
        int textOffset = 1;

        if(getText().length() > (getEndingColumn() - getStartingColumn() + 1) - 2)
            throw new UnableToDrawElementException("Text cannot fit the group box");

        if(getText().length() <= (getEndingColumn() - getStartingColumn() + 1) - 3)
            if(isAlignLeft())
                str.insert(0, ' ');
            else
                str.append(' ');

        if(getText().length() <= (getEndingColumn() - getStartingColumn() + 1) - 4)
            if(isAlignLeft())
                str.append(' ');
            else
                str.insert(0, ' ');

        if(getText().length() <= (getEndingColumn() - getStartingColumn() + 1) - 5)
            textOffset = 2;



        if(isForegroundColorVisible())
            outputHandler.setForegroundColorRectangle(getStartingRow(), getStartingColumn(), getEndingRow(), getEndingColumn(), getForegroundColor());

        if(isBackgroundColorVisible())
            outputHandler.setBackgroundColorRectangle(getStartingRow(), getStartingColumn(), getEndingRow(), getEndingColumn(), getBackgroundColor());

        if(isDoubleBorder())
            outputHandler.setDoubleFancyBox(getStartingRow(), getStartingColumn(), getEndingRow(), getEndingColumn());
        else
            outputHandler.setSingleFancyBox(getStartingRow(), getStartingColumn(), getEndingRow(), getEndingColumn());

        if(isAlignLeft())
            outputHandler.setString(getStartingRow(), getStartingColumn() + textOffset, str.toString());
        else
            outputHandler.setString(getStartingRow(), getEndingColumn() - getText().length() - textOffset - 1, str.toString());

        super.draw(outputHandler);
    }
}
