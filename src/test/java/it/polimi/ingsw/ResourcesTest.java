package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.gamematerials.ResourceGroup;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceType;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

@DisplayName("Resources tests")
public class ResourcesTest {
    @Test
    @DisplayName("Type match")
    public void typeMatchTest() {

        ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
        ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
        ResourceSingle shield = ResourceTypeSingleton.getInstance().getShieldResource();
        ResourceSingle stone = ResourceTypeSingleton.getInstance().getStoneResource();
        ResourceGroup any = ResourceTypeSingleton.getInstance().getAnyResource();

        assertTrue(gold.isA(gold));
        assertFalse(gold.isA(shield));
        assertFalse(any.isA(servant));
        assertTrue(servant.isA(any));
        assertFalse(any.isA(stone));
    }

    @Test
    @DisplayName("Groups")
    public void groupTest() {
        assertFalse(ResourceTypeSingleton.getInstance().getGoldResource().isGroup());
        assertTrue(ResourceTypeSingleton.getInstance().getAnyResource().isGroup());
    }

    @Test
    @DisplayName("Equals match")
    public void equalsMatch(){
        ResourceSingle gold1 = ResourceTypeSingleton.getInstance().getGoldResource();
        ResourceSingle gold2 = ResourceTypeSingleton.getInstance().getGoldResource();

        ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
        ResourceSingle shield = ResourceTypeSingleton.getInstance().getShieldResource();
        ResourceSingle stone1 = ResourceTypeSingleton.getInstance().getStoneResource();
        ResourceSingle stone2 = ResourceTypeSingleton.getInstance().getStoneResource();
        ResourceGroup any1 = ResourceTypeSingleton.getInstance().getAnyResource();
        ResourceGroup any2 = ResourceTypeSingleton.getInstance().getAnyResource();

        assertEquals(gold2, gold1);
        assertEquals(stone2, stone1);
        assertEquals(any2, any1);
        assertNotEquals(servant, shield);
    }

    @Test
    @DisplayName("Resource Single representation")
    public void resourceSingleRepresentation(){
        ResourceType gold = ResourceTypeSingleton.getInstance().getGoldResource();
        assertEquals(gold.toString(), "Gold");
    }

    @Test
    @DisplayName("Resource Group representation")
    public void resourceGroupRepresentation(){
        ResourceType any = ResourceTypeSingleton.getInstance().getAnyResource();
        assertEquals(any.toString(), "Any {Gold, Servant, Shield, Stone}");
    }

    @Test
    @DisplayName("Getters by name")
    public void gettersByNameTest() {
        assertEquals(ResourceTypeSingleton.getInstance().getGoldResource(), ResourceTypeSingleton.getInstance().getResourceSingleByName("gold"));
        assertEquals(ResourceTypeSingleton.getInstance().getServantResource(), ResourceTypeSingleton.getInstance().getResourceSingleByName("servant"));
        assertEquals(ResourceTypeSingleton.getInstance().getShieldResource(), ResourceTypeSingleton.getInstance().getResourceSingleByName("shield"));
        assertEquals(ResourceTypeSingleton.getInstance().getStoneResource(), ResourceTypeSingleton.getInstance().getResourceSingleByName("stone"));

        assertEquals(ResourceTypeSingleton.getInstance().getAnyResource(), ResourceTypeSingleton.getInstance().getResourceGroupByName("any"));
        assertEquals(ResourceTypeSingleton.getInstance().getAnyResource(), ResourceTypeSingleton.getInstance().getResourceTypeByName("any"));
    }

    @Test
    @DisplayName("Exceptions")
    public void exceptionTest() {
        ResourceType any = ResourceTypeSingleton.getInstance().getAnyResource();
        ResourceType gold = ResourceTypeSingleton.getInstance().getGoldResource();

        assertThrows(NullPointerException.class, () -> any.isA(null));
        assertThrows(NullPointerException.class, () -> gold.isA(null));

        assertThrows(NullPointerException.class, () -> ResourceTypeSingleton.getInstance().getResourceSingleByName(null));
        assertThrows(NullPointerException.class, () -> ResourceTypeSingleton.getInstance().getResourceGroupByName(null));
        assertThrows(NullPointerException.class, () -> ResourceTypeSingleton.getInstance().getResourceTypeByName(null));

        assertThrows(NoSuchElementException.class, () -> ResourceTypeSingleton.getInstance().getResourceSingleByName("a"));
        assertThrows(NoSuchElementException.class, () -> ResourceTypeSingleton.getInstance().getResourceGroupByName("b"));
        assertThrows(NoSuchElementException.class, () -> ResourceTypeSingleton.getInstance().getResourceTypeByName("c"));
    }
}
