package it.polimi.ingsw.client.gui.updaters;

import it.polimi.ingsw.client.gui.nodes.MarketBox;
import it.polimi.ingsw.client.model.ClientMarket;
import it.polimi.ingsw.client.observables.Listener;
import javafx.application.Platform;

public class MarketGuiUpdater implements Listener<ClientMarket> {

    private final MarketBox marketBox;

    public MarketGuiUpdater(MarketBox marketBox, ClientMarket clientMarket){
        this.marketBox = marketBox;
        this.marketBox.setup(clientMarket.getRowSize(), clientMarket.getColSize());
        if(clientMarket.getMarket() != null){
            update(clientMarket);
        }
        clientMarket.addListener(this);
    }

    @Override
    public void update(ClientMarket clientMarket) {
        Platform.runLater(()->{
            marketBox.setRawMarket(clientMarket.getMarket());
            marketBox.updateConversions(clientMarket.getPossibleConversions(), clientMarket.getSelectedMarbles());
        });
    }
}
