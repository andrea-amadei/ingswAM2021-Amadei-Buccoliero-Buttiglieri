package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.gamematerials.ResourceType;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.production.Crafting;
import it.polimi.ingsw.model.production.Production;
import it.polimi.ingsw.model.production.UpgradableCrafting;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ProductionTest {
    @Test
    public void test() {
        assertThrows(NullPointerException.class, () -> new Production(null));

        HashMap<ResourceType, Integer> input = new HashMap<>();
        input.put(ResourceTypeSingleton.getInstance().getServantResource(), 1);
        input.put(ResourceTypeSingleton.getInstance().getShieldResource(), 2);

        HashMap<ResourceType, Integer> output = new HashMap<>();
        output.put(ResourceTypeSingleton.getInstance().getStoneResource(), 1);
        output.put(ResourceTypeSingleton.getInstance().getGoldResource(), 2);

        Crafting crafting = new Crafting(input, output, 1);
        UpgradableCrafting upgradableCrafting = new UpgradableCrafting(input, output, 1, 1);

        Production production1 = new Production();
        Production production2 = new Production(new ArrayList<Crafting>() {{add(crafting);}});

        assertEquals(production1.getBaseCrafting().size(), 0);
        assertEquals(production1.getLeaderCrafting().size(), 0);
        assertEquals(production1.getUpgradableCrafting().size(), 3);

        assertEquals(production2.getBaseCrafting().size(), 1);
        assertEquals(production2.getLeaderCrafting().size(), 0);
        assertEquals(production2.getUpgradableCrafting().size(), 3);

        assertThrows(NullPointerException.class, () -> production2.addLeaderCrafting(null));
        assertThrows(NullPointerException.class, () -> production2.setUpgradableCrafting(1, null));
        assertThrows(IndexOutOfBoundsException.class, () -> production2.setUpgradableCrafting(-1, upgradableCrafting));

        production2.addLeaderCrafting(crafting);
        production2.setUpgradableCrafting(1, upgradableCrafting);

        assertEquals(production2.getLeaderCrafting().get(0), crafting);
        assertEquals(production2.getUpgradableCrafting().get(1), upgradableCrafting);
    }
}
