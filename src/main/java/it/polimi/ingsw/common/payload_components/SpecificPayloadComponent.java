package it.polimi.ingsw.common.payload_components;

import com.google.gson.annotations.SerializedName;

/**
 * A component of a message that targets a specified player
 */
public abstract class SpecificPayloadComponent implements PayloadComponent {

    @SerializedName("player")
    private String player;

    /**
     * This constructor shall only be used to instance payloads through reflection
     */
    public SpecificPayloadComponent() { }

    /**
     * Creates a new component of the specified group, type and player
     *
     * @param player the targeted player of this payload
     * @throws NullPointerException if either group or type is null
     */
    public SpecificPayloadComponent(String player) {
        if(player == null)
            throw new NullPointerException();

        this.player = player;
    }

    public String getPlayer() {
        return player;
    }
}
