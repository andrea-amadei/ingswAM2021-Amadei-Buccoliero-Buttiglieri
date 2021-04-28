package it.polimi.ingsw.common;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.exceptions.IllegalRawConversionException;

import java.util.Map;

public class PlayerStatusPayload extends PayloadComponent {

    @SerializedName("player")
    private final String player;

    @SerializedName("content")
    private final String message;

    /**
     * Creates a new player status payload
     *
     * @param type the type of this component (join, leave, ...)
     * @param player the specified player
     * @param message the attached message of the status update
     * @throws NullPointerException if any attribute is null
     */
    public PlayerStatusPayload(String type, String player, String message) {
        super("update", type);

        if(message == null || player == null)
            throw new NullPointerException();

        this.player = player;
        this.message = message;
    }

    public String getPlayer() {
        return player;
    }

    public String getMessage() {
        return message;
    }
}
