package it.polimi.ingsw;

import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.holder.DiscountHolder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiscountHolderTest {

    @Test
    public void testMethods() {
        DiscountHolder dh = new DiscountHolder();

        assertEquals(dh.getDiscounts().size(), 0);

        assertFalse(dh.addDiscount(ResourceTypeSingleton.getInstance().getGoldResource(), 1));
        assertFalse(dh.addDiscount(ResourceTypeSingleton.getInstance().getShieldResource(), 1));
        assertTrue(dh.addDiscount(ResourceTypeSingleton.getInstance().getGoldResource(), 2));

        assertEquals(dh.getDiscounts().size(), 2);
        assertEquals(dh.totalDiscountByResource(ResourceTypeSingleton.getInstance().getGoldResource()), 3);
        assertEquals(dh.totalDiscountByResource(ResourceTypeSingleton.getInstance().getShieldResource()), 1);
        assertEquals(dh.totalDiscountByResource(ResourceTypeSingleton.getInstance().getStoneResource()), 0);

        dh.reset();

        assertEquals(dh.getDiscounts().size(), 0);
    }

    @Test
    public void testExceptions() {
        DiscountHolder dh = new DiscountHolder();

        assertThrows(NullPointerException.class, () -> dh.addDiscount(null, 1));
        assertThrows(IllegalArgumentException.class,
                () -> dh.addDiscount(ResourceTypeSingleton.getInstance().getGoldResource(), 0));
        assertThrows(NullPointerException.class, () -> dh.totalDiscountByResource(null));
    }
}