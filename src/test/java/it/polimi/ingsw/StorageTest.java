package it.polimi.ingsw;

import it.polimi.ingsw.model.GameParameters;
import it.polimi.ingsw.model.storage.BaseStorage;
import it.polimi.ingsw.model.storage.Cupboard;
import it.polimi.ingsw.model.storage.Shelf;
import it.polimi.ingsw.model.storage.Storage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StorageTest {

    @Test
    public void createStorage(){
        assertDoesNotThrow(Storage::new);
    }

    @Test
    public void containsBaseCupboardWithEmptyShelves(){
        Storage s = new Storage();
        Cupboard c = s.getCupboard();
        for(int i = 0; i < GameParameters.BASE_CUPBOARD_SHELF_NAMES.size(); i++){
            Shelf shelf = c.getShelfById(GameParameters.BASE_CUPBOARD_SHELF_NAMES.get(i));
            assertEquals(shelf.getAmount(), 0);
        }
    }

    @Test
    public void containsEmptyChest(){
        Storage s = new Storage();
        BaseStorage chest = s.getChest();
        assertEquals(chest.getAllResources().size(), 0);
    }

    @Test
    public void containsEmptyHand(){
        Storage s = new Storage();
        BaseStorage hand = s.getHand();
        assertEquals(hand.getAllResources().size(), 0);
    }

    @Test
    public void conaitnsEmptyMarketBasket(){
        Storage s = new Storage();
        BaseStorage basket = s.getMarketBasket();
        assertEquals(basket.getAllResources().size(), 0);
    }

}
