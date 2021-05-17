package it.polimi.ingsw.client.cli.elements;

import it.polimi.ingsw.client.cli.OutputHandler;
import it.polimi.ingsw.exceptions.UnableToDrawElementException;
import it.polimi.ingsw.utils.BackgroundColor;
import it.polimi.ingsw.utils.ForegroundColor;

public class ResourceBoxWithAmount extends ResourceBox {
    private int amount;

    private boolean showXEnabled;
    private ForegroundColor foregroundColor;
    private BackgroundColor backgroundColor;
    private boolean foregroundColorVisible;
    private boolean backgroundColorVisible;

    public ResourceBoxWithAmount(String name, int row, int column, String resource, int amount) {
        super(name, row, column, resource);

        setAmount(amount);
        setShowXEnabled(true);

        setForegroundColorVisible(true);
        setBackgroundColorVisible(true);

        setForegroundColor(OutputHandler.getDefaultForegroundColor());
        setBackgroundColor(OutputHandler.getDefaultBackgroundColor());
    }

    public ResourceBoxWithAmount(String name, int row, int column, String resource, int amount, ForegroundColor foregroundColor, BackgroundColor backgroundColor) {
        super(name, row, column, resource);

        setAmount(amount);
        setShowXEnabled(true);

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

    public boolean isShowXEnabled() {
        return showXEnabled;
    }

    public void setShowXEnabled(boolean showXEnabled) {
        this.showXEnabled = showXEnabled;
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

        if(isShowXEnabled())
            str.insert(0, "x ");

        TextBox temp = new TextBox("temp", getStartingRow(), getStartingColumn() + 3, str.toString(), getForegroundColor(), getBackgroundColor());
        temp.setForegroundColorVisible(isForegroundColorVisible());
        temp.setBackgroundColorVisible(isBackgroundColorVisible());

        temp.draw(outputHandler);
    }
}
