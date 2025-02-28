package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.server.model.basetypes.ResourceSingle;
import it.polimi.ingsw.server.model.basetypes.ResourceType;
import it.polimi.ingsw.server.model.basetypes.ResourceTypeSingleton;
import it.polimi.ingsw.server.model.production.Crafting;
import it.polimi.ingsw.server.model.production.Production;
import it.polimi.ingsw.server.model.production.UpgradableCrafting;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

@DisplayName("Production tests")
public class ProductionTest {
    @Test
    public void test() {
        assertThrows(NullPointerException.class, () -> new Production(3, null));

        HashMap<ResourceType, Integer> input = new HashMap<>();
        input.put(ResourceTypeSingleton.getInstance().getServantResource(), 1);
        input.put(ResourceTypeSingleton.getInstance().getShieldResource(), 2);

        HashMap<ResourceType, Integer> output = new HashMap<>();
        output.put(ResourceTypeSingleton.getInstance().getStoneResource(), 1);
        output.put(ResourceTypeSingleton.getInstance().getGoldResource(), 2);

        Crafting crafting = new Crafting(input, output, 1);
        UpgradableCrafting upgradableCrafting = new UpgradableCrafting(input, output, 1, 1);

        Production production1 = new Production(3);
        Production production2 = new Production(3, new ArrayList<>() {{add(crafting);}});

        assertEquals(production1.getAllBaseCrafting().size(), 0);
        assertEquals(production1.getAllLeaderCrafting().size(), 0);
        assertEquals(production1.getAllUpgradableCrafting().size(), 3);

        assertEquals(production2.getAllBaseCrafting().size(), 1);
        assertEquals(production2.getAllLeaderCrafting().size(), 0);
        assertEquals(production2.getAllUpgradableCrafting().size(), 3);

        assertThrows(NullPointerException.class, () -> production2.addLeaderCrafting(null));
        assertThrows(NullPointerException.class, () -> production2.setUpgradableCrafting(1, null));
        assertThrows(IndexOutOfBoundsException.class, () -> production2.setUpgradableCrafting(-1, upgradableCrafting));

        production2.addLeaderCrafting(crafting);
        production2.setUpgradableCrafting(1, upgradableCrafting);

        assertEquals(production2.getAllLeaderCrafting().get(0), crafting);
        assertEquals(production2.getAllUpgradableCrafting().get(1), upgradableCrafting);
        assertEquals(production2.getAllBaseCrafting().get(0), crafting);

        assertEquals(production2.getLeaderCrafting(0), crafting);
        assertEquals(production2.getUpgradableCrafting(1), upgradableCrafting);
        assertEquals(production2.getBaseCrafting(0), crafting);
    }

    @Test
    @DisplayName("Is selected")
    public void isSelectedTest() {
        HashMap<ResourceType, Integer> input = new HashMap<>();
        input.put(ResourceTypeSingleton.getInstance().getServantResource(), 1);
        input.put(ResourceTypeSingleton.getInstance().getShieldResource(), 2);

        HashMap<ResourceType, Integer> output = new HashMap<>();
        output.put(ResourceTypeSingleton.getInstance().getStoneResource(), 1);
        output.put(ResourceTypeSingleton.getInstance().getGoldResource(), 2);

        Crafting crafting = new Crafting(input, output, 1);

        Production production = new Production(3, new ArrayList<>() {{add(crafting);}});

        assertDoesNotThrow(() -> production.selectCrafting(Production.CraftingType.BASE, 1));
        assertTrue(production.isCraftingSelected());
    }

    @Test
    public void selectNonNullUpgradableCrafting(){

        ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
        ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();

        Production production = new Production(3);
        production.setUpgradableCrafting(2,
                new UpgradableCrafting(new HashMap<>(){{put(gold, 2);}}, new HashMap<>(){{put(servant, 2);}}, 0, 1
        ));

        production.selectCrafting(Production.CraftingType.UPGRADABLE ,2);
        assertEquals(2, production.getSelectedCraftingIndex());
    }
}
