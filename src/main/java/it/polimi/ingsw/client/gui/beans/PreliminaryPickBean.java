package it.polimi.ingsw.client.gui.beans;

import java.util.List;
import java.util.Map;

public class PreliminaryPickBean {
    private List<Integer> leaderIndexes;
    private Map<String, Integer> selectedResources;

    public List<Integer> getLeaderIndexes() {
        return leaderIndexes;
    }

    public void setLeaderIndexes(List<Integer> leaderIndexes) {
        this.leaderIndexes = leaderIndexes;
    }

    public Map<String, Integer> getSelectedResources() {
        return selectedResources;
    }

    public void setSelectedResources(Map<String, Integer> selectedResources) {
        this.selectedResources = selectedResources;
    }
}
