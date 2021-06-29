package it.polimi.ingsw.common.payload_components.groups.updates;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.common.annotations.SerializedType;
import it.polimi.ingsw.common.payload_components.groups.SpecificUpdatePayloadComponent;

@SerializedType("add_upgradable_crafting")
public class AddUpgradableCraftingUpdatePayloadComponent extends SpecificUpdatePayloadComponent {

    @SerializedName("id")
    private int id;

    @SerializedName("index")
    private int index;

    /**
     * This constructor shall only be used to instance payloads through reflection
     */
    public AddUpgradableCraftingUpdatePayloadComponent() { }

    public AddUpgradableCraftingUpdatePayloadComponent(String player, int id, int index) {
        super(player);
        this.id = id;
        this.index = index;
    }

    public int getId() {
        return id;
    }

    public int getIndex() {
        return index;
    }
}
