package it.polimi.ingsw.common.payload_components.groups.updates;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.common.annotations.SerializedType;
import it.polimi.ingsw.common.payload_components.groups.SpecificUpdatePayloadComponent;

@SerializedType("add_bought_card")
public class AddBoughtCardUpdatePayloadComponent extends SpecificUpdatePayloadComponent {

    @SerializedName("amount")
    private int amount;

    /**
     * This constructor shall only be used to instance payloads through reflection
     */
    public AddBoughtCardUpdatePayloadComponent() { }

    public AddBoughtCardUpdatePayloadComponent(String player, int amount) {
        super(player);
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}
