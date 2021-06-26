package it.polimi.ingsw.client.gui.events;

import it.polimi.ingsw.client.gui.beans.ResourceSelectionBean;
import javafx.event.Event;
import javafx.event.EventType;

public class ResourceSelectionEvent extends Event {
    private final ResourceSelectionBean bean;
    public static final EventType<ResourceSelectionEvent> RESOURCE_SELECTION_EVENT = new EventType<>(ANY, "ResourceSelectionEvent");

    public ResourceSelectionEvent(ResourceSelectionBean bean){
        super(RESOURCE_SELECTION_EVENT);
        this.bean = bean;
    }

    public ResourceSelectionBean getBean() {
        return bean;
    }
}
