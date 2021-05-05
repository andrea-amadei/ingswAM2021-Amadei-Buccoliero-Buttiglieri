package it.polimi.ingsw.common.payload_components.groups;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.annotations.SerializedGroup;
import it.polimi.ingsw.common.payload_components.SpecificPayloadComponent;

@SerializedGroup("player_status")
public class PlayerStatusPayloadComponent extends SpecificPayloadComponent {

    @SerializedName("message")
    private String message;

    /**
     * This constructor shall only be used to instance payloads through reflection
     */
    public PlayerStatusPayloadComponent() { }

    /**
     * Creates a new player status payload
     *
     * @param message the attached message of the status update
     * @throws NullPointerException if any attribute is null
     */
    public PlayerStatusPayloadComponent(String message) {
        if(message == null)
            throw new NullPointerException();

        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
