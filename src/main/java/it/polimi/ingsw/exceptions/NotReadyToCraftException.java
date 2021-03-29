package it.polimi.ingsw.exceptions;

public class NotReadyToCraftException extends RuntimeException {
    public NotReadyToCraftException(String message) {
        super(message);
    }
}
