package it.polimi.ingsw.common;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.exceptions.IllegalRawConversionException;

import java.util.Map;

public class UpdatePayload extends PayloadComponent {

    @SerializedName("player")
    private final String player;

    @SerializedName("content")
    private final Map<String, Object> content;

    /**
     * Creates a new update payload
     *
     * @param type the type of this component (update_shelf, update_hand, ...)
     * @param player the specified player
     * @param content a map containing all changes of the specified type and related to the given player
     * @throws NullPointerException if any attribute is null
     */
    public UpdatePayload(String type, String player, Map<String, Object> content) {
        super("update", type);

        if(content == null || player == null)
            throw new NullPointerException();

        if(content.size() == 0)
            throw new IllegalArgumentException("Content size cannot be zero");

        this.player = player;
        this.content = content;
    }

    @Override
    public PayloadComponent toPayloadComponent() throws IllegalRawConversionException {
        return null;
    }
}
