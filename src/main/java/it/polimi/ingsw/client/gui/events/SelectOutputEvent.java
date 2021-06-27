package it.polimi.ingsw.client.gui.events;

import javafx.event.Event;
import javafx.event.EventType;

public class SelectOutputEvent extends Event{
    public static final EventType<SelectOutputEvent> SELECT_OUTPUT_EVENT = new EventType<>(ANY, "SelectOutputEvent");

    public SelectOutputEvent(){
        super(SELECT_OUTPUT_EVENT);
    }
}
