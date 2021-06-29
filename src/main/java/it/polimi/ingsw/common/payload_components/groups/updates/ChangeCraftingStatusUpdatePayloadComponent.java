package it.polimi.ingsw.common.payload_components.groups.updates;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.common.annotations.SerializedType;
import it.polimi.ingsw.common.payload_components.groups.SpecificUpdatePayloadComponent;
import it.polimi.ingsw.server.model.production.Production;

@SerializedType("change_crafting_status")
public class ChangeCraftingStatusUpdatePayloadComponent extends SpecificUpdatePayloadComponent {

    @SerializedName(value = "is_ready", alternate = "isReady")
    private final Boolean isReady;

    private final Integer index;

    @SerializedName(value = "crafting_type", alternate = "craftingType")
    private final Production.CraftingType craftingType;

    public ChangeCraftingStatusUpdatePayloadComponent(String player, Boolean isReady, Integer index, Production.CraftingType craftingType) {
        super(player);
        this.isReady = isReady;
        this.index = index;
        this.craftingType = craftingType;
    }

    public Boolean getReady() {
        return isReady;
    }

    public Integer getIndex() {
        return index;
    }

    public Production.CraftingType getCraftingType() {
        return craftingType;
    }
}
