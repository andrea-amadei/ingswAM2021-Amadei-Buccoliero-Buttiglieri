package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.gamematerials.ResourceGroup;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import org.junit.jupiter.api.Test;

/**
 * Unit test for simple App.
 */
public class ResourcesTests {
    @Test
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
}
