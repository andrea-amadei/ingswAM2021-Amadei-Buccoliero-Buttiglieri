package it.polimi.ingsw.common.payload_components.groups.updates;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.common.annotations.SerializedGroup;
import it.polimi.ingsw.common.annotations.SerializedType;
import it.polimi.ingsw.common.payload_components.groups.UpdatePayloadComponent;

@SerializedType("change_shop")
@SerializedGroup("update")
public class ChangeShopUpdatePayloadComponent implements UpdatePayloadComponent {

    @SerializedName("x")
    private int x;

    @SerializedName("y")
    private int y;

    @SerializedName("id")
    private int id;

    /**
     * This constructor shall only be used to instance payloads through reflection
     */
    public ChangeShopUpdatePayloadComponent() { }

    public ChangeShopUpdatePayloadComponent(int x, int y, int id) {
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getId() {
        return id;
    }
}
