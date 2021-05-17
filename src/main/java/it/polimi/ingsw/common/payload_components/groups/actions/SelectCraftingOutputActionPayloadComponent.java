package it.polimi.ingsw.common.payload_components.groups.actions;

import it.polimi.ingsw.annotations.SerializedType;

import java.util.Map;

@SerializedType("select_crafting_output")
public class SelectCraftingOutputActionPayloadComponent extends ActionPayloadComponent{

    private final Map<String, Integer> conversion;

    public SelectCraftingOutputActionPayloadComponent(String player, Map<String, Integer> conversion) {
        super(player);
        this.conversion = conversion;
    }

    public Map<String, Integer> getConversion() {
        return conversion;
    }
}
