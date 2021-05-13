package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.exceptions.UnableToDrawElementException;

import java.util.List;

public final class CliRenderer {
    private CliRenderer() { }

    public static void renderWithoutUpdating(OutputHandler outputHandler, List<VisibleElement> elements) throws UnableToDrawElementException {
        for(VisibleElement i : elements)
            i.draw(outputHandler);
    }

    public static void render(OutputHandler outputHandler, List<VisibleElement> elements) throws UnableToDrawElementException {
        renderWithoutUpdating(outputHandler, elements);

        outputHandler.update();
    }
}
