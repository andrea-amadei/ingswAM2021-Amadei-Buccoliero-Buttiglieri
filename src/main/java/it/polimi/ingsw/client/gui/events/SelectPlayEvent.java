package it.polimi.ingsw.client.gui.events;

import it.polimi.ingsw.server.model.actions.SelectPlayAction;
import javafx.event.Event;
import javafx.event.EventType;

public class SelectPlayEvent extends Event{
    private final SelectPlayAction.Play play;
    public static final EventType<SelectPlayEvent> SELECT_PLAY_EVENT_EVENT = new EventType<>(ANY, "SelectPlayEvent");

    public SelectPlayEvent(SelectPlayAction.Play play){
        super(SELECT_PLAY_EVENT_EVENT);
        this.play = play;
    }

    public SelectPlayAction.Play getPlay() {
        return play;
    }
}
