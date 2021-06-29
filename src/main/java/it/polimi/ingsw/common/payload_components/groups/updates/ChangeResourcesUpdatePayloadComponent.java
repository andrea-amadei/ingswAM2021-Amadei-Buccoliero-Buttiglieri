package it.polimi.ingsw.common.payload_components.groups.updates;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.common.annotations.SerializedType;
import it.polimi.ingsw.common.payload_components.groups.SpecificUpdatePayloadComponent;
import it.polimi.ingsw.common.parser.raw.RawStorage;

@SerializedType("change_resources")
public class ChangeResourcesUpdatePayloadComponent extends SpecificUpdatePayloadComponent {

    @SerializedName(value = "delta", alternate = {"delta_resources", "deltaResources"})
    private RawStorage delta;

    /**
     * This constructor shall only be used to instance payloads through reflection
     */
    public ChangeResourcesUpdatePayloadComponent() { }

    public ChangeResourcesUpdatePayloadComponent(String player, RawStorage delta) {
        super(player);

        if(delta == null)
            throw new NullPointerException();

        this.delta = delta;
    }

    public RawStorage getDelta() {
        return delta;
    }
}
