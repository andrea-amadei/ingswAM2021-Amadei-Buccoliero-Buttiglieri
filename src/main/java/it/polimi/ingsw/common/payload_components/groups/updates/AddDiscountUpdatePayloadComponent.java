package it.polimi.ingsw.common.payload_components.groups.updates;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.common.annotations.SerializedType;
import it.polimi.ingsw.common.payload_components.groups.SpecificUpdatePayloadComponent;

@SerializedType("add_discount")
public class AddDiscountUpdatePayloadComponent extends SpecificUpdatePayloadComponent {

    @SerializedName("resource")
    private String resource;

    @SerializedName("discount")
    private int discount;

    /**
     * This constructor shall only be used to instance payloads through reflection
     */
    public AddDiscountUpdatePayloadComponent() { }

    public AddDiscountUpdatePayloadComponent(String player, String resource, int discount) {
        super(player);

        if(resource == null)
            throw new NullPointerException();

        this.resource = resource;
        this.discount = discount;
    }

    public String getResource() {
        return resource;
    }

    public int getDiscount() {
        return discount;
    }
}
