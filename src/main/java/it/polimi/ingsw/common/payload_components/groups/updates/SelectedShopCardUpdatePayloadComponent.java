package it.polimi.ingsw.common.payload_components.groups.updates;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.common.annotations.SerializedType;
import it.polimi.ingsw.common.payload_components.groups.SpecificUpdatePayloadComponent;

@SerializedType("selected_shop_card")
public class SelectedShopCardUpdatePayloadComponent extends SpecificUpdatePayloadComponent {

    @SerializedName("x")
    private int x;

    @SerializedName("y")
    private int y;

    /**
     * This constructor shall only be used to instance payloads through reflection
     */
    public SelectedShopCardUpdatePayloadComponent() { }

    public SelectedShopCardUpdatePayloadComponent(String player, int x, int y) {
        super(player);
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
