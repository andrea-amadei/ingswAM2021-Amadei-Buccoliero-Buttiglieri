package it.polimi.ingsw.client.gui.events;

import javafx.event.Event;
import javafx.event.EventType;


public class PreliminaryPickEvent extends Event{
    public static final EventType<PreliminaryPickEvent> PRELIMINARY_PICK_EVENT_EVENT = new EventType<>(ANY, "PreliminaryPickEvent");

    public PreliminaryPickEvent(){
        super(PRELIMINARY_PICK_EVENT_EVENT);
    }
}
