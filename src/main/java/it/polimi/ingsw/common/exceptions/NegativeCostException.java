package it.polimi.ingsw.common.exceptions;

public class NegativeCostException extends RuntimeException {
    public NegativeCostException(String message) {
        super(message);
    }
}
