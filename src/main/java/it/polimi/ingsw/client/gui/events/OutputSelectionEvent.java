package it.polimi.ingsw.client.gui.events;

import it.polimi.ingsw.client.gui.beans.OutputSelectionBean;
import javafx.event.Event;
import javafx.event.EventType;

public class OutputSelectionEvent extends Event {
    private final OutputSelectionBean bean;
    public static final EventType<OutputSelectionEvent> OUTPUT_SELECTION_EVENT = new EventType<>(ANY, "OutputSelectionEvent");

    public OutputSelectionEvent(OutputSelectionBean bean){
        super(OUTPUT_SELECTION_EVENT);
        this.bean = bean;
    }

    public OutputSelectionBean getBean() {
        return bean;
    }
}
