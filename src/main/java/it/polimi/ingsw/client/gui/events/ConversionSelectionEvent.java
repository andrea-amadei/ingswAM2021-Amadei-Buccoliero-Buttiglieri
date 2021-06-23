package it.polimi.ingsw.client.gui.events;

import it.polimi.ingsw.client.gui.beans.ConversionSelectionBean;
import javafx.event.Event;
import javafx.event.EventType;

public class ConversionSelectionEvent extends Event {
    private final ConversionSelectionBean bean;
    public static final EventType<ConversionSelectionEvent> CONVERSION_SELECTION_EVENT = new EventType<>(ANY, "ConversionSelectionEvent");

    public ConversionSelectionEvent(ConversionSelectionBean bean){
        super(CONVERSION_SELECTION_EVENT);
        this.bean = bean;
    }

    public ConversionSelectionBean getBean() {
        return bean;
    }
}
