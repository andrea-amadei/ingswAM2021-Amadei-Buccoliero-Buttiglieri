package it.polimi.ingsw.client.cli.elements;

import it.polimi.ingsw.client.cli.OutputHandler;
import it.polimi.ingsw.exceptions.UnableToDrawElementException;
import it.polimi.ingsw.utils.BackgroundColor;
import it.polimi.ingsw.utils.ForegroundColor;

public class FlagBoxWithAmount extends FlagBox {
    private int amount;

    private boolean showX;
    private ForegroundColor foregroundColor;
    private BackgroundColor backgroundColor;
    private boolean foregroundColorVisible;
    private boolean backgroundColorVisible;

    public FlagBoxWithAmount(String name, int row, int column, int level, String color, int amount) {
        super(name, row, column, level, color);

        setAmount(amount);
        setShowX(true);

        setForegroundColorVisible(true);
        setBackgroundColorVisible(true);

        setForegroundColor(OutputHandler.getDefaultForegroundColor());
        setBackgroundColor(OutputHandler.getDefaultBackgroundColor());
    }

    public FlagBoxWithAmount(String name, int row, int column, int level, String color, int amount, ForegroundColor foregroundColor, BackgroundColor backgroundColor) {
        super(name, row, column, level, color);

        setAmount(amount);
        setShowX(true);

        setForegroundColorVisible(true);
        setBackgroundColorVisible(true);

        setForegroundColor(foregroundColor);
        setBackgroundColor(backgroundColor);
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isShowX() {
        return showX;
    }

    public void setShowX(boolean showX) {
        this.showX = showX;
    }


    public ForegroundColor getForegroundColor() {
        return foregroundColor;
    }

    public boolean isForegroundColorVisible() {
        return foregroundColorVisible;
    }

    public BackgroundColor getBackgroundColor() {
        return backgroundColor;
    }

    public boolean isBackgroundColorVisible() {
        return backgroundColorVisible;
    }

    public void setForegroundColor(ForegroundColor foregroundColor) {
        if(foregroundColor == null)
            throw new NullPointerException();

        this.foregroundColor = foregroundColor;
    }

    public void setForegroundColorVisible(boolean foregroundColorVisible) {
        this.foregroundColorVisible = foregroundColorVisible;
    }

    public void setBackgroundColor(BackgroundColor backgroundColor) {
        if(backgroundColor == null)
            throw new NullPointerException();

        this.backgroundColor = backgroundColor;
    }

    public void setBackgroundColorVisible(boolean backgroundColorVisible) {
        this.backgroundColorVisible = backgroundColorVisible;
    }

    @Override
    public void draw(OutputHandler outputHandler) throws UnableToDrawElementException {
        if(!isVisible())
            return;

        super.draw(outputHandler);

        StringBuilder str = new StringBuilder(Integer.toString(getAmount()));

        if(isShowX())
            str.insert(0, "x ");

        TextBox temp = new TextBox("temp", getStartingRow() + 1, getStartingColumn() + 4, str.toString(), getForegroundColor(), getBackgroundColor());
        temp.setForegroundColorVisible(isForegroundColorVisible());
        temp.setBackgroundColorVisible(isBackgroundColorVisible());

        temp.draw(outputHandler);
    }
}
