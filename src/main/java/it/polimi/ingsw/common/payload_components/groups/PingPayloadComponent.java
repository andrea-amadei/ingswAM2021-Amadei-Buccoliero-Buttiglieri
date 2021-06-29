package it.polimi.ingsw.common.payload_components.groups;

import it.polimi.ingsw.common.annotations.SerializedGroup;
import it.polimi.ingsw.common.annotations.SerializedType;
import it.polimi.ingsw.common.payload_components.PayloadComponent;

@SerializedGroup("ping")
@SerializedType("ping")
public class PingPayloadComponent implements PayloadComponent {
}
