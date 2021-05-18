package it.polimi.ingsw.client.cli.framework;

import it.polimi.ingsw.exceptions.UnableToDrawElementException;

public interface VisibleElement extends CliElement {
    boolean isVisible();
    int getZIndex();

    void setVisible(boolean visible);
    void setZIndex(int ZIndex);

    void draw(OutputHandler outputHandler) throws UnableToDrawElementException;
}
