package it.polimi.ingsw.client.gui.beans;

import it.polimi.ingsw.client.gui.nodes.PlayerNode;

public class ResourceContainerBean {
    private PlayerNode.ContainerType sourceType;
    private Object source;
    private int index;

    public PlayerNode.ContainerType getSourceType() {
        return sourceType;
    }

    public void setSourceType(PlayerNode.ContainerType sourceType) {
        this.sourceType = sourceType;
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
