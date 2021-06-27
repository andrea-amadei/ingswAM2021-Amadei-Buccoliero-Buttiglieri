package it.polimi.ingsw.client.gui.events;

import javafx.event.Event;
import javafx.event.EventType;

public class ChangedCurrentPlayerEvent extends Event {
    private final String username;
    public static final EventType<ChangedCurrentPlayerEvent> CHANGED_CURRENT_PLAYER_EVENT = new EventType<>(ANY, "ChangedCurrentPlayerEvent");

    public ChangedCurrentPlayerEvent(String username){
        super(CHANGED_CURRENT_PLAYER_EVENT);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
