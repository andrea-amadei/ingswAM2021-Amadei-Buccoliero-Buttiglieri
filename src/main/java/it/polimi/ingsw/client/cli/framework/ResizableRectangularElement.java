package it.polimi.ingsw.client.cli.framework;

public interface ResizableRectangularElement extends RectangularElement {
    void setEndingRow(int row);
    void setEndingColumn(int column);
    void setEndingPosition(int row, int column);
}
