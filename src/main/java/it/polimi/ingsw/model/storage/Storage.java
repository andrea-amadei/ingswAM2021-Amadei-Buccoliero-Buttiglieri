package it.polimi.ingsw.model.storage;

import it.polimi.ingsw.model.GameParameters;

import java.util.ArrayList;
import java.util.List;

public class Storage {
    private final BaseStorage chest;
    private final BaseStorage hand;
    private final BaseStorage marketBasket;
    private final Cupboard cupboard;

    public Storage(){
        chest = new BaseStorage();
        hand = new BaseStorage();
        marketBasket = new BaseStorage();
        List<Shelf> baseShelves = new ArrayList<>();
        for(int i = 0; i < GameParameters.BASE_CUPBOARD_SHELF_NAMES.size(); i++){
            baseShelves.add(new Shelf(GameParameters.BASE_CUPBOARD_SHELF_NAMES.get(i),
                                      GameParameters.BASE_CUPBOARD_SHELF_TYPES.get(i),
                                      GameParameters.BASE_CUPBOARD_SHELF_SIZES.get(i)));
        }
        cupboard = new BaseCupboard(baseShelves);

    }

    public BaseStorage getChest() {
        return chest;
    }

    public BaseStorage getHand() {
        return hand;
    }

    public BaseStorage getMarketBasket() {
        return marketBasket;
    }

    public Cupboard getCupboard() {
        return cupboard;
    }





}
