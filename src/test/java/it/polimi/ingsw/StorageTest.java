package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.IllegalSelectionException;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.GameParameters;
import it.polimi.ingsw.model.storage.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class StorageTest {

    @Test
    public void createStorage(){
        assertDoesNotThrow((ThrowingSupplier<Storage>) Storage::new);
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
    public void containsEmptyMarketBasket(){
        Storage s = new Storage();
        BaseStorage basket = s.getMarketBasket();
        assertEquals(basket.getAllResources().size(), 0);
    }

    @Test
    public void ownedResources(){

        ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
        ResourceSingle stone = ResourceTypeSingleton.getInstance().getStoneResource();
        ResourceSingle shield = ResourceTypeSingleton.getInstance().getShieldResource();
        ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();

        Storage s = new Storage();
        assertDoesNotThrow(()->s.getCupboard().addResource(s.getCupboard().getShelfById("BottomShelf"),gold, 3));
        assertDoesNotThrow(()->s.getCupboard().addResource(s.getCupboard().getShelfById("TopShelf"),shield, 1));
        assertDoesNotThrow(()->s.getChest().addResources(gold, 2));
        assertDoesNotThrow(()->s.getChest().addResources(shield, 4));
        assertDoesNotThrow(()->s.getChest().addResources(servant, 5));
        assertDoesNotThrow(()->s.getChest().addResources(stone, 1));

        Map<ResourceSingle, Integer> expectedResources = new HashMap<>(){{
            put(gold, 5);
            put(stone, 1);
            put(shield, 5);
            put(servant, 5);
        }};

        assertEquals(expectedResources, s.getStoredResources());
    }

    @Test
    public void invalidDecorateTest(){
        Storage s = new Storage();
        assertThrows(NullPointerException.class, ()->s.decorate(null));
    }

    @Test
    public void validDecorateTest(){
        Storage s = new Storage();
        LeaderDecorator decorator = new LeaderDecorator(new Shelf("leader1", ResourceTypeSingleton.getInstance().getAnyResource(), 2), s.getCupboard());
        assertDoesNotThrow(()->s.decorate(decorator));
        assertDoesNotThrow(()->s.getCupboard().getShelfById("leader1"));
    }

    @Test
    public void addToSelectionFromHandAndShelf(){
        ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
        ResourceSingle stone = ResourceTypeSingleton.getInstance().getStoneResource();
        ResourceSingle shield = ResourceTypeSingleton.getInstance().getShieldResource();
        ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();

        Storage s = new Storage();
        Shelf bottomShelf = s.getCupboard().getShelfById("BottomShelf");
        Shelf middleShelf = s.getCupboard().getShelfById("MiddleShelf");

        assertDoesNotThrow(()->s.getCupboard().addResource(bottomShelf, gold, 3));
        assertDoesNotThrow(()->s.getCupboard().addResource(middleShelf, shield, 2));
        assertDoesNotThrow(()->s.getChest().addResources(servant, 2));
        assertDoesNotThrow(()->s.getChest().addResources(gold, 3));

        assertDoesNotThrow(()->s.addToSelection(bottomShelf, gold, 2));
        assertDoesNotThrow(()->s.addToSelection(middleShelf, shield, 2));
        assertDoesNotThrow(()->s.addToSelection(s.getChest(), gold, 1));
        assertDoesNotThrow(()->s.addToSelection(s.getChest(), servant, 1));

        Map<ResourceContainer, Map<ResourceSingle, Integer>> expectedSelected = new HashMap<>(){
            {
                put(bottomShelf, new HashMap<>(){{put(gold, 2);}});
                put(middleShelf, new HashMap<>(){{put(shield, 2);}});
                put(s.getChest(), new HashMap<>(){{put(gold, 1); put(servant, 1);}});
            }
        };

        assertEquals(expectedSelected, s.getSelection());
    }

    @Test
    public void selectTooManyResources(){
        ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
        ResourceSingle stone = ResourceTypeSingleton.getInstance().getStoneResource();
        ResourceSingle shield = ResourceTypeSingleton.getInstance().getShieldResource();
        ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();

        Storage s = new Storage();
        Shelf bottomShelf = s.getCupboard().getShelfById("BottomShelf");
        Shelf middleShelf = s.getCupboard().getShelfById("MiddleShelf");

        assertDoesNotThrow(()->s.getCupboard().addResource(bottomShelf, gold, 3));
        assertDoesNotThrow(()->s.getCupboard().addResource(middleShelf, shield, 2));
        assertDoesNotThrow(()->s.getChest().addResources(servant, 2));
        assertDoesNotThrow(()->s.getChest().addResources(gold, 3));

        assertThrows(IllegalSelectionException.class, ()->s.addToSelection(bottomShelf, shield, 2));
        assertDoesNotThrow(()->s.addToSelection(middleShelf, shield, 1));
        assertThrows(IllegalSelectionException.class, ()->s.addToSelection(middleShelf, shield, 2));
    }

    @Test
    public void clearSelection(){
        ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
        ResourceSingle stone = ResourceTypeSingleton.getInstance().getStoneResource();
        ResourceSingle shield = ResourceTypeSingleton.getInstance().getShieldResource();
        ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();

        Storage s = new Storage();
        Shelf bottomShelf = s.getCupboard().getShelfById("BottomShelf");
        Shelf middleShelf = s.getCupboard().getShelfById("MiddleShelf");

        assertDoesNotThrow(()->s.getCupboard().addResource(bottomShelf, gold, 3));
        assertDoesNotThrow(()->s.getCupboard().addResource(middleShelf, shield, 2));
        assertDoesNotThrow(()->s.getChest().addResources(servant, 2));
        assertDoesNotThrow(()->s.getChest().addResources(gold, 3));

        assertDoesNotThrow(()->s.addToSelection(bottomShelf, gold, 2));
        assertDoesNotThrow(()->s.addToSelection(middleShelf, shield, 2));
        assertDoesNotThrow(()->s.addToSelection(s.getChest(), gold, 1));
        assertDoesNotThrow(()->s.addToSelection(s.getChest(), servant, 1));

        s.resetSelection();
        assertNull(s.getSelection());
    }

    @Test
    public void getChestById(){
        Storage s = new Storage();
        assertEquals(s.getChest(), s.getSpendableResourceContainerById("Chest"));
    }

}
