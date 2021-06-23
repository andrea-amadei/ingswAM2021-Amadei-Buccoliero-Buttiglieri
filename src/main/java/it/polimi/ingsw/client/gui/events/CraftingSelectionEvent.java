package it.polimi.ingsw.client.gui.events;

import it.polimi.ingsw.client.gui.beans.CraftingSelectionBean;
import javafx.event.Event;
import javafx.event.EventType;

public class CraftingSelectionEvent extends Event {
    private final CraftingSelectionBean bean;
    public static EventType<CraftingSelectionEvent> CRAFTING_SELECTION_EVENT = new EventType<>(ANY, "CraftingSelectionEvent");

    public CraftingSelectionEvent(CraftingSelectionBean bean){
        super(CRAFTING_SELECTION_EVENT);
        this.bean = bean;
    }

    public CraftingSelectionBean getBean() {
        return bean;
    }
}
