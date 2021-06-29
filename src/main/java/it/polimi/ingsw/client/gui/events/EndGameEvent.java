package it.polimi.ingsw.client.gui.events;

import it.polimi.ingsw.client.gui.beans.EndGameResultsBean;
import javafx.event.Event;
import javafx.event.EventType;

public class EndGameEvent extends Event {
    private final EndGameResultsBean bean;
    public static final EventType<EndGameEvent> END_GAME_EVENT = new EventType<>(ANY, "EndGameEvent");

    public EndGameEvent(EndGameResultsBean bean){
        super(END_GAME_EVENT);
        this.bean = bean;
    }

    public EndGameResultsBean getBean() {
        return bean;
    }
}
