package it.polimi.ingsw.client.gui.events;

import it.polimi.ingsw.client.gui.beans.MarketPickBean;
import javafx.event.Event;
import javafx.event.EventType;

public class MarketPickEvent extends Event {
    private final MarketPickBean bean;
    public static final EventType<MarketPickEvent> MARKET_PICK_EVENT = new EventType<>(ANY, "MarketPickEvent");

    public MarketPickEvent(MarketPickBean bean){
        super(MARKET_PICK_EVENT);
        this.bean = bean;
    }

    public MarketPickBean getBean() {
        return bean;
    }
}
