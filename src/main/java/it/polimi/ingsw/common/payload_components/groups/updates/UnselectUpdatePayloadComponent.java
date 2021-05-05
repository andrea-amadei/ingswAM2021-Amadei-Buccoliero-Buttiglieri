package it.polimi.ingsw.common.payload_components.groups.updates;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.annotations.SerializedType;
import it.polimi.ingsw.common.payload_components.groups.SpecificUpdatePayloadComponent;

@SerializedType("unselect")
public class UnselectUpdatePayloadComponent extends SpecificUpdatePayloadComponent {

    @SerializedName("section")
    private String section;

    /**
     * This constructor shall only be used to instance payloads through reflection
     */
    public UnselectUpdatePayloadComponent() { }

    public UnselectUpdatePayloadComponent(String player, String section) {
        super(player);

        if(section == null)
            throw new NullPointerException();

        this.section = section;
    }

    public String getSection() {
        return section;
    }
}
