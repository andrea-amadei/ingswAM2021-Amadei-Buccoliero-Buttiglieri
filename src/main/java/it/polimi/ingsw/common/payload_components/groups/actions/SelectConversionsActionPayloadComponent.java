package it.polimi.ingsw.common.payload_components.groups.actions;

import it.polimi.ingsw.common.annotations.SerializedType;

import java.util.List;

@SerializedType("select_conversions")
public class SelectConversionsActionPayloadComponent extends ActionPayloadComponent{
    private final List<Integer> actuatorsChoice;

    public SelectConversionsActionPayloadComponent(String player, List<Integer> actuatorsChoice) {
        super(player);
        this.actuatorsChoice = actuatorsChoice;
    }

    public List<Integer> getActuatorsChoice() {
        return actuatorsChoice;
    }
}
