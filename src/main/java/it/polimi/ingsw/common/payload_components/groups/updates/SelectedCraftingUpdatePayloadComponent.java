package it.polimi.ingsw.common.payload_components.groups.updates;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.common.annotations.SerializedType;
import it.polimi.ingsw.common.payload_components.groups.SpecificUpdatePayloadComponent;
import it.polimi.ingsw.server.model.production.Production;

@SerializedType("selected_crafting")
public class SelectedCraftingUpdatePayloadComponent extends SpecificUpdatePayloadComponent {

    @SerializedName(value = "crafting_type", alternate = "craftingType")
    private Production.CraftingType craftingType;

    @SerializedName("index")
    private int index;

    /**
     * This constructor shall only be used to instance payloads through reflection
     */
    public SelectedCraftingUpdatePayloadComponent() { }

    public SelectedCraftingUpdatePayloadComponent(String player, Production.CraftingType craftingType, int index) {
        super(player);

        if(craftingType == null)
            throw new NullPointerException();

        this.craftingType = craftingType;
        this.index = index;
    }

    public Production.CraftingType getCraftingType() {
        return craftingType;
    }

    public int getIndex() {
        return index;
    }
}
