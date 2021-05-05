package it.polimi.ingsw.common.payload_components.groups.updates;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.annotations.SerializedType;
import it.polimi.ingsw.common.payload_components.groups.SpecificUpdatePayloadComponent;
import it.polimi.ingsw.parser.raw.RawLevelFlag;

@SerializedType("add_flag")
public class AddFlagUpdatePayloadComponent extends SpecificUpdatePayloadComponent {

    @SerializedName("flag")
    private RawLevelFlag flag;

    /**
     * This constructor shall only be used to instance payloads through reflection
     */
    public AddFlagUpdatePayloadComponent() { }

    public AddFlagUpdatePayloadComponent(String player, RawLevelFlag flag) {
        super(player);

        if(flag == null)
            throw new NullPointerException();

        this.flag = flag;
    }

    public RawLevelFlag getFlag() {
        return flag;
    }
}
