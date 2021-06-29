package it.polimi.ingsw.common.payload_components.groups.updates;

import it.polimi.ingsw.common.annotations.SerializedGroup;
import it.polimi.ingsw.common.annotations.SerializedType;
import it.polimi.ingsw.common.payload_components.groups.UpdatePayloadComponent;

@SerializedGroup("update")
@SerializedType("add_lorenzo_faith")
public class AddLorenzoFaithUpdatePayloadComponent implements UpdatePayloadComponent {
    private final int amount;

    public AddLorenzoFaithUpdatePayloadComponent(int faith){
        this.amount = faith;
    }

    public int getFaith() {
        return amount;
    }
}
