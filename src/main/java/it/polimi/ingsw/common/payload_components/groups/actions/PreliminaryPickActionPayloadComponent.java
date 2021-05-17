package it.polimi.ingsw.common.payload_components.groups.actions;

import it.polimi.ingsw.annotations.SerializedType;

import java.util.List;
import java.util.Map;

@SerializedType("preliminary_pick")
public class PreliminaryPickActionPayloadComponent extends ActionPayloadComponent{
    private final List<Integer> leadersToDiscard;
    private final Map<String, Integer> chosenResources;

    public PreliminaryPickActionPayloadComponent(String player, List<Integer> leadersToDiscard, Map<String, Integer> chosenResources) {
        super(player);
        this.leadersToDiscard = leadersToDiscard;
        this.chosenResources = chosenResources;
    }

    public List<Integer> getLeadersToDiscard() {
        return leadersToDiscard;
    }

    public Map<String, Integer> getChosenResources() {
        return chosenResources;
    }
}
