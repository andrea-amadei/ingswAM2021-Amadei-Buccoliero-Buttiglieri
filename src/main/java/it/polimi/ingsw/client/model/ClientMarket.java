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

    public void changeMarket(RawMarket newMarket){
        this.market = newMarket;
        update();
    }

    public void changePossibleConversions(List<MarbleColor> selectedMarbles, List<List<ConversionOption>> possibleConversions){
        this.selectedMarbles = selectedMarbles;
        this.possibleConversions = possibleConversions;
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
