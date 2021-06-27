package it.polimi.ingsw.client.gui.events;

import javafx.event.Event;
import javafx.event.EventType;

public class CollectFromBasketEvent extends Event{
    public static final EventType<CollectFromBasketEvent> COLLECT_FROM_BASKET_EVENT_EVENT = new EventType<>(ANY, "CollectFromBasketEvent");

    public CollectFromBasketEvent(){
        super(COLLECT_FROM_BASKET_EVENT_EVENT);
    }
}
