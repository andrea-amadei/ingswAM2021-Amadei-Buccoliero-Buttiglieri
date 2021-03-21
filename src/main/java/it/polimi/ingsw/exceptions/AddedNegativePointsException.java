package it.polimi.ingsw.exceptions;

public class AddedNegativePointsException extends RuntimeException {
    public AddedNegativePointsException() {
        super("Added a negative amount of points");
    }

    public AddedNegativePointsException(String message) {
        super(message);
    }
}
