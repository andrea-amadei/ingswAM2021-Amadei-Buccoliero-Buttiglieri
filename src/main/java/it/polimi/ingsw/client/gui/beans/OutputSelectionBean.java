package it.polimi.ingsw.client.gui.beans;

import java.util.Map;

public class OutputSelectionBean {
    private int amount;
    private Map<String, Integer> selection;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Map<String, Integer> getSelection() {
        return selection;
    }

    public void setSelection(Map<String, Integer> selection) {
        this.selection = selection;
    }
}
