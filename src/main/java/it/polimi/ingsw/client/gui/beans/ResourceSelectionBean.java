package it.polimi.ingsw.client.gui.beans;

import it.polimi.ingsw.client.gui.nodes.PlayerNode;

public class ResourceSelectionBean extends ResourceContainerBean{

    private String resource;
    private int amount;

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

}
