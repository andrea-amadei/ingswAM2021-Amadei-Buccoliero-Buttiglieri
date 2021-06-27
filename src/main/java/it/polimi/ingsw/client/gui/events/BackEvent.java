package it.polimi.ingsw.client.gui.events;

import javafx.event.Event;
import javafx.event.EventType;

public class BackEvent extends Event {
    public final static EventType<BackEvent> BACK_EVENT = new EventType<>(ANY, "BackEvent");

    public BackEvent(){
        super(BACK_EVENT);
    }
}
