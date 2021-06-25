package it.polimi.ingsw.client.gui.events;

import javafx.event.Event;
import javafx.event.EventType;

public class ConfirmEvent extends Event{
    public static final EventType<ConfirmEvent> CONFIRM_EVENT = new EventType<>(ANY, "ConfirmEvent");

    public ConfirmEvent(){
        super(CONFIRM_EVENT);
    }
}
