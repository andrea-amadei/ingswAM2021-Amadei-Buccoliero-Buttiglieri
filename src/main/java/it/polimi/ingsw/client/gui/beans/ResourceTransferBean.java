package it.polimi.ingsw.client.gui.beans;

import java.util.Map;

public class ResourceTransferBean {
    private String sourceId;
    private String targetDestination;
    private String choseResource;
    private int amountToTransfer;

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getTargetDestination() {
        return targetDestination;
    }

    public void setTargetDestination(String targetDestination) {
        this.targetDestination = targetDestination;
    }

    public String getChoseResource() {
        return choseResource;
    }

    public void setChoseResource(String choseResource) {
        this.choseResource = choseResource;
    }

    public int getAmountToTransfer() {
        return amountToTransfer;
    }

    public void setAmountToTransfer(int amountToTransfer) {
        this.amountToTransfer = amountToTransfer;
    }

    @Override
    public String toString() {
        return "{" + sourceId + ", " + targetDestination + ", " + choseResource + ", " + amountToTransfer + "}";
    }
}
