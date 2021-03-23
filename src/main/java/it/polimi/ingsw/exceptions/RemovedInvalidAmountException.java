package it.polimi.ingsw.exceptions;

public class RemovedInvalidAmountException extends RuntimeException{
    public RemovedInvalidAmountException(String message) {
        super(message);
    }
}
