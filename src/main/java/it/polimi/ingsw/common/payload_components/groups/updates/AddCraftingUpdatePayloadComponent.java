package it.polimi.ingsw.common.payload_components.groups.updates;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.common.annotations.SerializedType;
import it.polimi.ingsw.common.payload_components.groups.SpecificUpdatePayloadComponent;
import it.polimi.ingsw.server.model.production.Production;
import it.polimi.ingsw.common.parser.raw.RawCrafting;

@SerializedType("add_crafting")
public class AddCraftingUpdatePayloadComponent extends SpecificUpdatePayloadComponent {

    @SerializedName("crafting")
    private RawCrafting crafting;

    @SerializedName(value = "crafting_type", alternate = "craftingType")
    private Production.CraftingType craftingType;

    @SerializedName("index")
    private int index;

    /**
     * This constructor shall only be used to instance payloads through reflection
     */
    public AddCraftingUpdatePayloadComponent() { }

    public AddCraftingUpdatePayloadComponent(String player, RawCrafting crafting, Production.CraftingType craftingType, int index) {
        super(player);

        if(crafting == null || craftingType == null)
            throw new NullPointerException();

        this.crafting = crafting;
        this.craftingType = craftingType;
        this.index = index;
    }
}
