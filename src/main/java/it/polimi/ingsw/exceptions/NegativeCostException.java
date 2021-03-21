package it.polimi.ingsw.exceptions;

public class NegativeCostException extends RuntimeException {
    public NegativeCostException(String message) {
        super(message);
    }
}
