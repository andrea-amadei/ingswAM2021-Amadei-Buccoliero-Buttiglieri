package it.polimi.ingsw.client.cli.framework.elements;

import it.polimi.ingsw.client.cli.framework.CliRenderer;
import it.polimi.ingsw.client.cli.framework.OutputHandler;
import it.polimi.ingsw.client.cli.framework.VisibleElement;
import it.polimi.ingsw.exceptions.UnableToDrawElementException;
import it.polimi.ingsw.utils.BackgroundColor;
import it.polimi.ingsw.utils.ForegroundColor;

import java.util.List;

public class Frame extends Group {
    private ForegroundColor foregroundColor;
    private BackgroundColor backgroundColor;

    public Frame(String name) {
        super(name);

        setForegroundColor(OutputHandler.getDefaultForegroundColor());
        setBackgroundColor(OutputHandler.getDefaultBackgroundColor());
    }

    public Frame(String name, List<VisibleElement> elements) {
        super(name, elements);

        setForegroundColor(OutputHandler.getDefaultForegroundColor());
        setBackgroundColor(OutputHandler.getDefaultBackgroundColor());
    }

    public ForegroundColor getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(ForegroundColor foregroundColor) {
        if(foregroundColor == null)
            throw new NullPointerException();

        this.foregroundColor = foregroundColor;
    }

    public BackgroundColor getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(BackgroundColor backgroundColor) {
        if(backgroundColor == null)
            throw new NullPointerException();

        this.backgroundColor = backgroundColor;
    }

    @Override
    public void draw(OutputHandler outputHandler) throws UnableToDrawElementException {
        outputHandler.resetAll();

        outputHandler.setForegroundColorRectangle(0, 0,
                outputHandler.getHeight() - 1, outputHandler.getWidth() - 1, foregroundColor);

        outputHandler.setBackgroundColorRectangle(0, 0,
                outputHandler.getHeight() - 1, outputHandler.getWidth() - 1, backgroundColor);

        CliRenderer.render(outputHandler, getAllElements());
    }
}
