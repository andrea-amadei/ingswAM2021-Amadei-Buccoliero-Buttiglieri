package it.polimi.ingsw.exceptions;

public class UnsupportedLeaderShelfOperation extends RuntimeException{
    public UnsupportedLeaderShelfOperation() {
    }

    public UnsupportedLeaderShelfOperation(String message) {
        super(message);
    }
}
