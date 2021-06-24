package it.polimi.ingsw.client.gui.events;

import it.polimi.ingsw.client.gui.beans.ShopSelectionBean;
import javafx.event.Event;
import javafx.event.EventType;

public class ShopCardSelectionEvent extends Event {
    private final ShopSelectionBean shopSelectionBean;
    public static final EventType<ShopCardSelectionEvent> SHOP_CARD_SELECTION_EVENT = new EventType<>(ANY, "ShopCardSelectionEvent");


    public ShopCardSelectionEvent(ShopSelectionBean shopSelectionBean){
        super(SHOP_CARD_SELECTION_EVENT);
        this.shopSelectionBean = shopSelectionBean;
    }

    public ShopSelectionBean getCardSelectionBean(){
        return shopSelectionBean;
    }
}
