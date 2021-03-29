package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.IllegalResourceTransferException;
import it.polimi.ingsw.gamematerials.ResourceGroup;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.storage.BaseStorage;
import it.polimi.ingsw.model.storage.LimitedStorage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("LimitedStorage test")
public class LimitedStorageTest {
    @Test
    @DisplayName("Construction test")
    public void constructionTest() {
        Map<ResourceSingle, Integer> single = new HashMap<>();
        single.put(ResourceTypeSingleton.getInstance().getGoldResource(), 2);

        Map<ResourceSingle, Integer> invalid_single = new HashMap<>();
        invalid_single.put(ResourceTypeSingleton.getInstance().getGoldResource(), -2);

        Map<ResourceGroup, Integer> group = new HashMap<>();
        group.put(ResourceTypeSingleton.getInstance().getAnyResource(), 1);

        Map<ResourceGroup, Integer> invalid_group = new HashMap<>();
        invalid_group.put(ResourceTypeSingleton.getInstance().getAnyResource(), -1);

        Map<ResourceSingle, Integer> map1 = new HashMap<>();
        map1.put(ResourceTypeSingleton.getInstance().getGoldResource(), 2);

        Map<ResourceSingle, Integer> map2 = new HashMap<>();
        map2.put(ResourceTypeSingleton.getInstance().getGoldResource(), 2);
        map2.put(ResourceTypeSingleton.getInstance().getShieldResource(), 1);

        assertThrows(NullPointerException.class, () -> new LimitedStorage(null, group));
        assertThrows(NullPointerException.class, () -> new LimitedStorage(null, single, group));
        assertThrows(NullPointerException.class, () -> new LimitedStorage(map1, null, group));

        assertThrows(IllegalArgumentException.class, () -> new LimitedStorage(invalid_single, group));
        assertThrows(IllegalArgumentException.class, () -> new LimitedStorage(map1, invalid_single, group));
        assertThrows(IllegalArgumentException.class, () -> new LimitedStorage(single, invalid_group));
        assertThrows(IllegalArgumentException.class, () -> new LimitedStorage(map1, single, invalid_group));

        assertDoesNotThrow(() -> new LimitedStorage(single, null));
        assertDoesNotThrow(() -> new LimitedStorage(single, group));
        assertDoesNotThrow(() -> new LimitedStorage(map1, single, null));
        assertDoesNotThrow(() -> new LimitedStorage(map1, single, group));

        assertThrows(IllegalArgumentException.class, () -> new LimitedStorage(map2, single, null));
    }

    @Test
    @DisplayName("Add, Remove and Get test")
    void addRemoveGetResetTest() {
        ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
        ResourceGroup any = ResourceTypeSingleton.getInstance().getAnyResource();

        Map<ResourceSingle, Integer> single = new HashMap<>();
        single.put(gold, 2);

        Map<ResourceGroup, Integer> group = new HashMap<>();
        group.put(any, 1);

        LimitedStorage ls = new LimitedStorage(single, group);

        assertDoesNotThrow(() -> ls.addResources(gold, 3));
        assertThrows(IllegalResourceTransferException.class, () -> ls.addResources(gold, 1));
        assertEquals(ls.getResources(gold), 3);
        assertEquals(ls.totalAmountOfResources(), 3);

        assertThrows(IllegalResourceTransferException.class, () -> ls.removeResources(gold, 4));
        assertDoesNotThrow(() -> ls.removeResources(gold, 1));
        assertDoesNotThrow(() -> ls.removeResources(gold, 2));
        assertEquals(ls.getResources(gold), 0);
        assertEquals(ls.totalAmountOfResources(), 0);

        assertDoesNotThrow(() -> ls.addResources(gold, 3));
        assertEquals(ls.getResources(gold), 3);
        assertDoesNotThrow(() -> ls.removeResources(gold, 3));
        assertEquals(ls.getResources(gold), 0);
    }

    @Test
    @DisplayName("Getters and Reset test")
    public void getterResetTest() {
        ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
        ResourceSingle shield = ResourceTypeSingleton.getInstance().getShieldResource();
        ResourceGroup any = ResourceTypeSingleton.getInstance().getAnyResource();

        Map<ResourceSingle, Integer> single = new HashMap<>();
        single.put(gold, 2);

        Map<ResourceGroup, Integer> group = new HashMap<>();
        group.put(any, 1);

        Map<ResourceSingle, Integer> map1 = new HashMap<>();
        map1.put(gold, 2);
        map1.put(shield, 1);

        Map<ResourceSingle, Integer> map2 = new HashMap<>();
        map2.put(gold, 3);

        LimitedStorage ls = new LimitedStorage(single, group);

        assertEquals(ls.getSingleResourceLimit(), single);
        assertEquals(ls.getGroupResourceLimit(), group);

        assertDoesNotThrow(() -> ls.addResources(gold, 2));
        assertDoesNotThrow(() -> ls.addResources(shield, 1));

        assertEquals(ls.getResources(gold), 2);
        assertEquals(ls.getResources(shield), 1);
        assertEquals(ls.getAllResources(), map1);

        ls.reset();

        assertEquals(ls.getSingleResourceLimit(), single);
        assertEquals(ls.getGroupResourceLimit(), group);
        assertEquals(ls.totalAmountOfResources(), 0);

        assertDoesNotThrow(() -> ls.addResources(gold, 3));
        assertEquals(ls.getAllResources(), map2);
    }

    @Test
    @DisplayName("Storage Full test")
    public void isFullTest() {
        ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
        ResourceGroup any = ResourceTypeSingleton.getInstance().getAnyResource();

        Map<ResourceSingle, Integer> single = new HashMap<>();
        single.put(gold, 2);

        Map<ResourceGroup, Integer> group = new HashMap<>();
        group.put(any, 1);

        LimitedStorage ls = new LimitedStorage(single, group);

        assertDoesNotThrow(() -> ls.addResources(gold, 1));
        assertFalse(ls.isFull());
        assertDoesNotThrow(() -> ls.addResources(gold, 1));
        assertFalse(ls.isFull());
        assertDoesNotThrow(() -> ls.addResources(gold, 1));
        assertTrue(ls.isFull());
    }

    @Test
    @DisplayName("Exception test")
    public void exceptionTest() {
        ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
        ResourceGroup any = ResourceTypeSingleton.getInstance().getAnyResource();

        Map<ResourceSingle, Integer> single = new HashMap<>();
        single.put(gold, 2);

        Map<ResourceGroup, Integer> group = new HashMap<>();
        group.put(any, 1);

        LimitedStorage ls = new LimitedStorage(single, group);

        assertThrows(NullPointerException.class, () -> ls.addResources(null ,1));
        assertThrows(IllegalArgumentException.class, () -> ls.addResources(gold ,-1));
        assertThrows(NullPointerException.class, () -> ls.removeResources(null, 1));
        assertThrows(IllegalArgumentException.class, () -> ls.removeResources(gold, -1));
        assertThrows(NullPointerException.class, () -> ls.getResources(null));
    }

    @Test
    @DisplayName("Equals test")
    public void equalsTest() {
        ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
        ResourceSingle shield = ResourceTypeSingleton.getInstance().getShieldResource();
        ResourceGroup any = ResourceTypeSingleton.getInstance().getAnyResource();

        Map<ResourceSingle, Integer> single = new HashMap<>();
        single.put(gold, 2);
        single.put(shield, 1);

        Map<ResourceGroup, Integer> group = new HashMap<>();
        group.put(any, 1);

        LimitedStorage ls1 = new LimitedStorage(single, group);

        // preliminary
        assertNotEquals(ls1, null);
        assertEquals(ls1, ls1);

        // single
        Map<ResourceSingle, Integer> single1 = new HashMap<>();
        single1.put(gold, 2);

        Map<ResourceSingle, Integer> single2 = new HashMap<>();
        single2.put(gold, 3);

        Map<ResourceSingle, Integer> single3 = new HashMap<>();
        single3.put(shield, 3);

        assertNotEquals(ls1, new LimitedStorage(single1, group));
        assertNotEquals(new LimitedStorage(single1, group), new LimitedStorage(single2, group));
        assertNotEquals(new LimitedStorage(single2, group), new LimitedStorage(single3, group));

        // group
        Map<ResourceGroup, Integer> group1 = new HashMap<>();
        Map<ResourceGroup, Integer> group2 = new HashMap<>();
        group2.put(any, 2);

        assertNotEquals(ls1, new LimitedStorage(single, group1));
        assertNotEquals(ls1, new LimitedStorage(single, group2));

        // resources
        LimitedStorage ls2 = new LimitedStorage(single, group);

        assertDoesNotThrow(() -> ls1.addResources(gold, 1));
        assertDoesNotThrow(() -> ls2.addResources(gold, 1));
        assertDoesNotThrow(() -> ls2.addResources(shield, 1));
        assertNotEquals(ls1, ls2);

        ls2.reset();
        assertDoesNotThrow(() -> ls2.addResources(gold, 2));
        assertNotEquals(ls1, ls2);

        ls2.reset();
        assertDoesNotThrow(() -> ls2.addResources(shield, 1));
        assertNotEquals(ls1, ls2);

        // equals
        ls2.reset();
        assertDoesNotThrow(() -> ls2.addResources(gold, 1));
        assertEquals(ls1, ls2);
    }

    @Test
    @DisplayName("Hash Code test")
    public void hashCodeTest() {
        ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
        ResourceSingle shield = ResourceTypeSingleton.getInstance().getShieldResource();
        ResourceGroup any = ResourceTypeSingleton.getInstance().getAnyResource();

        Map<ResourceSingle, Integer> single = new HashMap<>();
        single.put(gold, 2);
        single.put(shield, 1);

        Map<ResourceGroup, Integer> group = new HashMap<>();
        group.put(any, 1);

        Map<ResourceSingle, Integer> resources = new HashMap<>();
        resources.put(gold, 1);
        resources.put(shield, 1);

        LimitedStorage ls1 = new LimitedStorage(resources, single, group);
        LimitedStorage ls2 = new LimitedStorage(single, group);

        assertDoesNotThrow(() -> ls2.addResources(gold, 1));
        assertDoesNotThrow(() -> ls2.addResources(shield, 1));

        assertEquals(ls1.hashCode(), ls2.hashCode());
    }
}