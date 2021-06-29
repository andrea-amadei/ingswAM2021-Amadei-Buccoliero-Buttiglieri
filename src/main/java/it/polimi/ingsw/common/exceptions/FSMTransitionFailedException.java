package it.polimi.ingsw.common.exceptions;

import it.polimi.ingsw.common.Message;

import java.util.ArrayList;
import java.util.List;

public class FSMTransitionFailedException extends Exception{

    private final List<Message> gameMessages;
    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public FSMTransitionFailedException() {
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
    public FSMTransitionFailedException(String message) {
        super(message);
        gameMessages = new ArrayList<>();
    }

    public FSMTransitionFailedException(List<Message> gameMessages){
        this.gameMessages = gameMessages;
    }

    public FSMTransitionFailedException(List<Message> gameMessages, String message){
        super(message);
        this.gameMessages = gameMessages;
    }

    public List<Message> getGameMessages() {return gameMessages;}
}
