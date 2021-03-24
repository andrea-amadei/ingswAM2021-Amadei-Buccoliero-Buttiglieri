package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.exceptions.RemovedInvalidAmountException;
import it.polimi.ingsw.exceptions.RemovedInvalidResourceException;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.storage.BaseStorage;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class BaseStorageTest {
    @Test
    public void constructionTest() {
        Map<ResourceSingle, Integer> map = new HashMap<>();
        map.put(ResourceTypeSingleton.getInstance().getGoldResource(), 1);
        map.put(ResourceTypeSingleton.getInstance().getStoneResource(), 1);

        BaseStorage bs;

        bs = new BaseStorage();
        assertEquals(bs.getAllResources().size(), 0);

        bs = new BaseStorage(map);
        assertEquals(bs.getAllResources().size(), 2);

        bs = new BaseStorage(bs);
        assertEquals(bs.getAllResources().size(), 2);
    }

    @Test
    public void methodsTest() {
        BaseStorage bs = new BaseStorage();

        bs.addResource(ResourceTypeSingleton.getInstance().getServantResource(), 1);
        bs.addResource(ResourceTypeSingleton.getInstance().getShieldResource(), 2);
        bs.addResource(ResourceTypeSingleton.getInstance().getServantResource(), 3);

        bs.removeResource(ResourceTypeSingleton.getInstance().getServantResource(), 1);

        bs.setResource(ResourceTypeSingleton.getInstance().getGoldResource(), 5);
        bs.setResource(ResourceTypeSingleton.getInstance().getShieldResource(), 1);

        assertEquals(bs.getResource(ResourceTypeSingleton.getInstance().getServantResource()), 3);
        assertEquals(bs.getResource(ResourceTypeSingleton.getInstance().getShieldResource()), 1);
        assertEquals(bs.getResource(ResourceTypeSingleton.getInstance().getGoldResource()), 5);
        assertEquals(bs.getResource(ResourceTypeSingleton.getInstance().getStoneResource()), 0);

        bs.reset();

        assertEquals(bs.getResource(ResourceTypeSingleton.getInstance().getServantResource()), 0);
        assertEquals(bs.getResource(ResourceTypeSingleton.getInstance().getShieldResource()), 0);
        assertEquals(bs.getResource(ResourceTypeSingleton.getInstance().getGoldResource()), 0);
        assertEquals(bs.getResource(ResourceTypeSingleton.getInstance().getStoneResource()), 0);

        assertEquals(bs.getAllResources().size(), 0);
    }

    @Test
    public void exceptionsTest() {
        BaseStorage bs;

        Map<ResourceSingle, Integer> map = new HashMap<>();
        map.put(ResourceTypeSingleton.getInstance().getGoldResource(), -1);

        assertThrows(NullPointerException.class, () -> new BaseStorage((BaseStorage) null));
        assertThrows(NullPointerException.class, () -> new BaseStorage((Map<ResourceSingle, Integer>) null));
        assertThrows(IllegalArgumentException.class, () -> new BaseStorage(map));

        bs = new BaseStorage();

        assertThrows(NullPointerException.class, () -> bs.addResource(null, 1));
        assertThrows(NullPointerException.class, () -> bs.removeResource(null, 1));
        assertThrows(NullPointerException.class, () -> bs.setResource(null, 1));
        assertThrows(NullPointerException.class, () -> bs.getResource(null));

        assertThrows(IllegalArgumentException.class, () ->
                bs.addResource(ResourceTypeSingleton.getInstance().getGoldResource(), -1));
        assertThrows(IllegalArgumentException.class, () ->
                bs.removeResource(ResourceTypeSingleton.getInstance().getGoldResource(), -1));
        assertThrows(IllegalArgumentException.class, () ->
                bs.setResource(ResourceTypeSingleton.getInstance().getGoldResource(), -1));

        assertThrows(RemovedInvalidResourceException.class, () ->
                bs.removeResource(ResourceTypeSingleton.getInstance().getGoldResource(), 2));

        bs.addResource(ResourceTypeSingleton.getInstance().getGoldResource(), 1);

        assertThrows(RemovedInvalidAmountException.class, () ->
                bs.removeResource(ResourceTypeSingleton.getInstance().getGoldResource(), 2));
    }

    @Test
    public void equalsTest() {
        BaseStorage bs1 = new BaseStorage();

        bs1.addResource(ResourceTypeSingleton.getInstance().getGoldResource(), 1);
        bs1.setResource(ResourceTypeSingleton.getInstance().getStoneResource(), 2);

        Map<ResourceSingle, Integer> map = new HashMap<>();
        map.put(ResourceTypeSingleton.getInstance().getGoldResource(), 1);
        map.put(ResourceTypeSingleton.getInstance().getStoneResource(), 2);

        BaseStorage bs2 = new BaseStorage(map);

        assertEquals(bs1, bs2);
        assertEquals(bs2, bs1);
        assertEquals(bs1, bs1);

        assertNotEquals(bs1, null);

        bs2.reset();
        bs2.addResource(ResourceTypeSingleton.getInstance().getGoldResource(), 1);
        assertNotEquals(bs1, bs2);

        bs2.addResource(ResourceTypeSingleton.getInstance().getStoneResource(), 3);
        assertNotEquals(bs1, bs2);
    }

    @Test
    public void hashCodeTest() {
        BaseStorage bs1 = new BaseStorage();

        bs1.addResource(ResourceTypeSingleton.getInstance().getGoldResource(), 1);
        bs1.setResource(ResourceTypeSingleton.getInstance().getStoneResource(), 2);

        Map<ResourceSingle, Integer> map = new HashMap<>();
        map.put(ResourceTypeSingleton.getInstance().getGoldResource(), 1);
        map.put(ResourceTypeSingleton.getInstance().getStoneResource(), 2);

        BaseStorage bs2 = new BaseStorage(map);

        assertEquals(bs1.hashCode(), bs1.hashCode());
    }
}