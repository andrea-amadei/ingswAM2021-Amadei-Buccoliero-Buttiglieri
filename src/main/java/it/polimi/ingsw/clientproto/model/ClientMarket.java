package it.polimi.ingsw.clientproto.model;

import it.polimi.ingsw.clientproto.observables.Listener;
import it.polimi.ingsw.clientproto.observables.Observable;
import it.polimi.ingsw.parser.raw.RawMarket;

import java.util.ArrayList;
import java.util.List;

public class ClientMarket implements Observable<ClientMarket> {

    private final List<Listener<ClientMarket>> listeners;
    private final int rowSize;
    private final int colSize;

    private RawMarket market;

    /**
     * Creates an empty market (market reference is initially set to null)
     * @param rowSize the rowSize of the market
     * @param colSize the colSize of the market
     */
    public ClientMarket(int rowSize, int colSize){
        this.rowSize = rowSize;
        this.colSize = colSize;
        listeners = new ArrayList<>();
    }

    public void changeMarket(RawMarket newMarket){
        this.market = newMarket;
        update();
    }

    public int getRowSize() {
        return rowSize;
    }

    public int getColSize() {
        return colSize;
    }

    public RawMarket getMarket() {
        return market;
    }

    @Override
    public void addListener(Listener<ClientMarket> listener) {
        listeners.add(listener);
    }

    @Override
    public void update() {
        for(Listener<ClientMarket> l : listeners)
            l.update(this);
    }
}
