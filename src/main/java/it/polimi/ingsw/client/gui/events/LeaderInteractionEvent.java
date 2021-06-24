package it.polimi.ingsw.client.gui.events;

import it.polimi.ingsw.client.gui.beans.LeaderInteractionBean;
import javafx.event.Event;
import javafx.event.EventType;

public class LeaderInteractionEvent extends Event {
    private final LeaderInteractionBean bean;
    public static EventType<LeaderInteractionEvent> LEADER_INTERACTION_EVENT = new EventType<>(ANY, "LeaderInteractionEvent");

    public LeaderInteractionEvent(LeaderInteractionBean bean){
        super(LEADER_INTERACTION_EVENT);
        this.bean = bean;
    }

    public LeaderInteractionBean getBean() {
        return bean;
    }
}
