package it.polimi.ingsw.client.updates;

import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.parser.raw.RawMarket;

public class ChangeMarketUpdate implements Update{

    private final RawMarket market;

    public ChangeMarketUpdate(RawMarket market){
        this.market = market;
        checkFormat();
    }

    @Override
    public void apply(ClientModel client) {
        client.getMarket().changeMarket(market);
    }

    @Override
    public void checkFormat() {
        if(market == null)
            throw new NullPointerException("pointer to market is null");
    }

    public RawMarket getMarket() {
        return market;
    }
}
