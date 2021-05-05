package it.polimi.ingsw.common.payload_components.groups.updates;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.annotations.SerializedType;
import it.polimi.ingsw.common.payload_components.groups.SpecificUpdatePayloadComponent;

@SerializedType("add_faith")
public class AddFaithUpdatePayloadComponent extends SpecificUpdatePayloadComponent {

    @SerializedName("amount")
    private int amount;

    /**
     * This constructor shall only be used to instance payloads through reflection
     */
    public AddFaithUpdatePayloadComponent() { }

    public AddFaithUpdatePayloadComponent(String player, int amount) {
        super(player);
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}
