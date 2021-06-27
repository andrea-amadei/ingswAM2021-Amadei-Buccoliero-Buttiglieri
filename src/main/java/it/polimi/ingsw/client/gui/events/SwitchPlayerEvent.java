package it.polimi.ingsw.client.gui.events;

import javafx.event.Event;
import javafx.event.EventType;

public class SwitchPlayerEvent extends Event {
    private final String username;
    public static EventType<SwitchPlayerEvent> SWITCH_PLAYER_EVENT = new EventType<>(ANY, "SwitchPlayerEvent");

    public SwitchPlayerEvent(String username){
        super(SWITCH_PLAYER_EVENT);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
