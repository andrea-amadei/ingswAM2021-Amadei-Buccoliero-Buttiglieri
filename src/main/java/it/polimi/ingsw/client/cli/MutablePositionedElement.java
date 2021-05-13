package it.polimi.ingsw.client.cli;

public interface MutablePositionedElement extends PositionedElement {
    void setStartingRow(int row);
    void setStartingColumn(int column);
    void setStartingPosition(int row, int column);
}
