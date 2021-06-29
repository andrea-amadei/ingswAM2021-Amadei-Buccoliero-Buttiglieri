package it.polimi.ingsw.common.exceptions;

import it.polimi.ingsw.common.Message;

import java.util.ArrayList;
import java.util.List;

public class IllegalActionException extends Exception{

    private final List<Message> gameMessages;
    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public IllegalActionException() {
        gameMessages = new ArrayList<>();
    }

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public IllegalActionException(String message) {
        super(message);
        gameMessages = new ArrayList<>();
    }

    public IllegalActionException(List<Message> gameMessages){
        this.gameMessages = gameMessages;
    }

    public IllegalActionException(List<Message> gameMessages, String message){
        super(message);
        this.gameMessages = gameMessages;
    }

    public List<Message> getGameMessages(){
        return gameMessages;
    }


}
