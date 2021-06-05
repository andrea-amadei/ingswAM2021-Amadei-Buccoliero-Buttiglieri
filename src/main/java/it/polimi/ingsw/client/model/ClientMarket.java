package it.polimi.ingsw.client.model;

import it.polimi.ingsw.client.observables.Listener;
import it.polimi.ingsw.client.observables.Observable;
import it.polimi.ingsw.gamematerials.MarbleColor;
import it.polimi.ingsw.parser.raw.RawMarket;

import java.util.ArrayList;
import java.util.List;

public class ClientMarket implements Observable<ClientMarket> {

    private final List<Listener<ClientMarket>> listeners;
    private final int rowSize;
    private final int colSize;

    //The outer list contains a list of option for each marble
    //(the size of the outer list is equal to the amount of selected marbles)
    private List<List<ConversionOption>> possibleConversions;
    private List<MarbleColor> selectedMarbles;

    private RawMarket market;

    /**
     * Creates an empty market (market reference is initially set to null)
     * @param rowSize the rowSize of the market
     * @param colSize the colSize of the market
     */
    public ClientMarket(int rowSize, int colSize){
        this.rowSize = rowSize;
        this.colSize = colSize;
        possibleConversions = new ArrayList<>();
        selectedMarbles = new ArrayList<>();
        listeners = new ArrayList<>();
    }

    public synchronized void changeMarket(RawMarket newMarket){
        this.market = newMarket;
        update();
    }

    public synchronized void changePossibleConversions(List<MarbleColor> selectedMarbles, List<List<ConversionOption>> possibleConversions){
        this.selectedMarbles = selectedMarbles;
        this.possibleConversions = possibleConversions;
        update();
    }

    public synchronized int getRowSize() {
        return rowSize;
    }

    public synchronized int getColSize() {
        return colSize;
    }

    public synchronized RawMarket getMarket() {
        return market;
    }

    public synchronized List<List<ConversionOption>> getPossibleConversions() {
        return new ArrayList<>(possibleConversions);
    }

    public synchronized List<MarbleColor> getSelectedMarbles() {
        return new ArrayList<>(selectedMarbles);
    }

    @Override
    public synchronized void addListener(Listener<ClientMarket> listener) {
        listeners.add(listener);
    }

    @Override
    public synchronized void update() {
        for(Listener<ClientMarket> l : listeners)
            l.update(this);
    }
}
