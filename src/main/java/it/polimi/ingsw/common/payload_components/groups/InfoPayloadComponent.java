package it.polimi.ingsw.common.payload_components.groups;


import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.common.annotations.SerializedGroup;
import it.polimi.ingsw.common.annotations.SerializedType;
import it.polimi.ingsw.common.payload_components.PayloadComponent;

/**
 * An information the server suggests to show to the clients.
 */
@SerializedGroup("update")
@SerializedType("info")
public class InfoPayloadComponent implements PayloadComponent {

    @SerializedName("message")
    private String message;

    /**
     * This constructor shall only be used to instance payloads through reflection
     */
    public InfoPayloadComponent() { }

    /**
     * Creates a new InfoPayloadComponent with the specified message
     * @param message the message of this InfoPayloadComponent
     * @throws NullPointerException if the message is null
     */
    public InfoPayloadComponent(String message) {
        if(message == null)
            throw new NullPointerException();

        this.message = message;
    }

    /**
     * Returns the representation of this InfoPayloadComponent
     * @return the representation of this InfoPayloadComponent
     */
    @Override
    public String toString(){
        return "INFO: " + message;
    }
}
