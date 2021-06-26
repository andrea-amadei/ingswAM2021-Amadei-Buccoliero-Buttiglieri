package it.polimi.ingsw.client.gui.events;

import it.polimi.ingsw.client.gui.beans.ResourceContainerBean;
import javafx.event.Event;
import javafx.event.EventType;

public class ResourceTransferEvent extends Event {
    private final ResourceContainerBean bean;
    public static EventType<ResourceTransferEvent> RESOURCE_TRANSFER_EVENT = new EventType<>(ANY, "ResourceTransferEvent");

    public ResourceTransferEvent(ResourceContainerBean bean){
        super(RESOURCE_TRANSFER_EVENT);
        this.bean = bean;
    }

    public ResourceContainerBean getBean() {
        return bean;
    }
}
