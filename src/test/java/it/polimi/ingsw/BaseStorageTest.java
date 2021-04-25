package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.exceptions.IllegalResourceTransferException;
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

        bs = new BaseStorage("id");
        assertEquals(bs.getAllResources().size(), 0);

        bs = new BaseStorage(map, "id");
        assertEquals(bs.getAllResources().size(), 2);
    }

    @Test
    public void addRemoveSetTest() {
        BaseStorage bs = new BaseStorage("id");

        bs.addResources(ResourceTypeSingleton.getInstance().getServantResource(), 1);
        bs.addResources(ResourceTypeSingleton.getInstance().getShieldResource(), 2);
        bs.addResources(ResourceTypeSingleton.getInstance().getServantResource(), 3);
        bs.addResources(ResourceTypeSingleton.getInstance().getStoneResource(), 4);

        assertDoesNotThrow(() -> bs.removeResources(ResourceTypeSingleton.getInstance().getServantResource(), 1));
        assertDoesNotThrow(() -> bs.removeResources(ResourceTypeSingleton.getInstance().getStoneResource(), 4));

        bs.addResources(ResourceTypeSingleton.getInstance().getGoldResource(), 5);
        assertDoesNotThrow(() -> bs.removeResources(ResourceTypeSingleton.getInstance().getShieldResource(), 1));

        assertEquals(bs.getResources(ResourceTypeSingleton.getInstance().getServantResource()), 3);
        assertEquals(bs.getResources(ResourceTypeSingleton.getInstance().getShieldResource()), 1);
        assertEquals(bs.getResources(ResourceTypeSingleton.getInstance().getGoldResource()), 5);
        assertEquals(bs.getResources(ResourceTypeSingleton.getInstance().getStoneResource()), 0);

        bs.reset();

        assertEquals(bs.getResources(ResourceTypeSingleton.getInstance().getServantResource()), 0);
        assertEquals(bs.getResources(ResourceTypeSingleton.getInstance().getShieldResource()), 0);
        assertEquals(bs.getResources(ResourceTypeSingleton.getInstance().getGoldResource()), 0);
        assertEquals(bs.getResources(ResourceTypeSingleton.getInstance().getStoneResource()), 0);

        assertEquals(bs.getAllResources().size(), 0);
    }

    @Test
    public void exceptionsTest() {
        BaseStorage bs;

        Map<ResourceSingle, Integer> map = new HashMap<>();
        map.put(ResourceTypeSingleton.getInstance().getGoldResource(), -1);

        assertThrows(NullPointerException.class, () -> new BaseStorage(null));
        assertThrows(IllegalArgumentException.class, () -> new BaseStorage(map, "id"));

        bs = new BaseStorage("id");

        assertThrows(NullPointerException.class, () -> bs.addResources(null, 1));
        assertThrows(NullPointerException.class, () -> bs.removeResources(null, 1));
        assertThrows(NullPointerException.class, () -> bs.getResources(null));

        assertThrows(IllegalArgumentException.class, () ->
                bs.addResources(ResourceTypeSingleton.getInstance().getGoldResource(), -1));
        assertThrows(IllegalArgumentException.class, () ->
                bs.removeResources(ResourceTypeSingleton.getInstance().getGoldResource(), -1));

        assertThrows(IllegalResourceTransferException.class, () ->
                bs.removeResources(ResourceTypeSingleton.getInstance().getGoldResource(), 2));

        bs.addResources(ResourceTypeSingleton.getInstance().getGoldResource(), 1);

        assertThrows(IllegalResourceTransferException.class, () ->
                bs.removeResources(ResourceTypeSingleton.getInstance().getGoldResource(), 2));
    }

    @Test
    public void movementTest() {
        BaseStorage bs1 = new BaseStorage("id");
        bs1.addResources(ResourceTypeSingleton.getInstance().getGoldResource(), 1);
        bs1.addResources(ResourceTypeSingleton.getInstance().getStoneResource(), 2);

        BaseStorage bs2 = new BaseStorage("id");

        assertThrows(NullPointerException.class, () ->
                bs1.moveTo(null, ResourceTypeSingleton.getInstance().getGoldResource(), 1));
        assertThrows(NullPointerException.class, () ->
                bs1.moveTo(bs2, null, 1));
        assertThrows(IllegalArgumentException.class, () ->
                bs1.moveTo(bs2, ResourceTypeSingleton.getInstance().getGoldResource(), -1));

        assertThrows(IllegalResourceTransferException.class, () ->
                bs1.moveTo(bs2, ResourceTypeSingleton.getInstance().getShieldResource(), 1));
        assertThrows(IllegalResourceTransferException.class, () ->
                bs1.moveTo(bs2, ResourceTypeSingleton.getInstance().getGoldResource(), 2));

        assertDoesNotThrow(() ->
                bs1.moveTo(bs2, ResourceTypeSingleton.getInstance().getGoldResource(), 1));

        assertEquals(bs1.getResources(ResourceTypeSingleton.getInstance().getGoldResource()), 0);
        assertEquals(bs1.getResources(ResourceTypeSingleton.getInstance().getStoneResource()), 2);

        assertEquals(bs2.getResources(ResourceTypeSingleton.getInstance().getGoldResource()), 1);
    }

    /*
    @Test
    public void equalsTest() {
        BaseStorage bs1 = new BaseStorage();

        bs1.addResources(ResourceTypeSingleton.getInstance().getGoldResource(), 1);
        bs1.addResources(ResourceTypeSingleton.getInstance().getStoneResource(), 2);

        Map<ResourceSingle, Integer> map = new HashMap<>();
        map.put(ResourceTypeSingleton.getInstance().getGoldResource(), 1);
        map.put(ResourceTypeSingleton.getInstance().getStoneResource(), 2);

        BaseStorage bs2 = new BaseStorage(map);

        assertEquals(bs1, bs2);
        assertEquals(bs2, bs1);
        assertEquals(bs1, bs1);

        assertNotEquals(bs1, null);

        bs2.reset();
        bs2.addResources(ResourceTypeSingleton.getInstance().getGoldResource(), 1);
        assertNotEquals(bs1, bs2);

        bs2.addResources(ResourceTypeSingleton.getInstance().getStoneResource(), 3);
        assertNotEquals(bs1, bs2);

        assertDoesNotThrow(() -> bs2.removeResources(ResourceTypeSingleton.getInstance().getStoneResource(), 1));
        bs2.addResources(ResourceTypeSingleton.getInstance().getShieldResource(), 3);
        assertNotEquals(bs1, bs2);

        bs2.reset();
        bs2.addResources(ResourceTypeSingleton.getInstance().getStoneResource(), 2);
        bs2.addResources(ResourceTypeSingleton.getInstance().getShieldResource(), 3);
        assertNotEquals(bs1, bs2);
    }


    @Test
    public void hashCodeTest() {
        BaseStorage bs1 = new BaseStorage();

        bs1.addResources(ResourceTypeSingleton.getInstance().getGoldResource(), 1);
        bs1.addResources(ResourceTypeSingleton.getInstance().getStoneResource(), 2);

        Map<ResourceSingle, Integer> map = new HashMap<>();
        map.put(ResourceTypeSingleton.getInstance().getGoldResource(), 1);
        map.put(ResourceTypeSingleton.getInstance().getStoneResource(), 2);

        BaseStorage bs2 = new BaseStorage(map);

        assertEquals(bs1.hashCode(), bs2.hashCode());
    }
     */
}