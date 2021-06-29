package it.polimi.ingsw.common.payload_components.groups;

import it.polimi.ingsw.common.annotations.SerializedGroup;
import it.polimi.ingsw.common.payload_components.SpecificPayloadComponent;

@SerializedGroup("update")
public class SpecificUpdatePayloadComponent extends SpecificPayloadComponent implements UpdatePayloadComponent {
    /**
     * This constructor shall only be used to instance payloads through reflection
     */
    public SpecificUpdatePayloadComponent() { }

    public SpecificUpdatePayloadComponent(String player) {
        super(player);
    }
}
