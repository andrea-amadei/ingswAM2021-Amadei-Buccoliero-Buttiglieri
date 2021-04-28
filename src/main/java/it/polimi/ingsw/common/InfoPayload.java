package it.polimi.ingsw.common;


/**
 * An information the server suggests to show to the clients.
 */
public class InfoPayload extends PayloadComponent{

    private final String message;

    /**
     * Creates a new InfoPayload with the specified message
     * @param message the message of this InfoPayload
     * @throws NullPointerException if the message is null
     */
    public InfoPayload(String message) {
        super("message", "normal_message");
        if(message == null)
            throw new NullPointerException();
        this.message = message;
    }

    /**
     * Returns the representation of this InfoPayload
     * @return the representation of this InfoPayload
     */
    @Override
    public String toString(){
        return "INFO: " + message;
    }
}
