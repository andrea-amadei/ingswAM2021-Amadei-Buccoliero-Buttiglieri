package it.polimi.ingsw.client;

import it.polimi.ingsw.clientproto.model.ClientProduction;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.production.Crafting;
import it.polimi.ingsw.model.production.Production;
import it.polimi.ingsw.parser.raw.RawCrafting;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class ClientProductionTest {

    private final static RawCrafting normalCrafting = new RawCrafting(new Crafting(
            new HashMap<>(){{
                put(ResourceTypeSingleton.getInstance().getGoldResource(), 2);
                put(ResourceTypeSingleton.getInstance().getAnyResource(), 3);
            }},
            new HashMap<>(){{
                put(ResourceTypeSingleton.getInstance().getStoneResource(), 1);
                put(ResourceTypeSingleton.getInstance().getGoldResource(), 3);
            }},
            0
    ));

    private final static RawCrafting faithCrafting = new RawCrafting(new Crafting(
            new HashMap<>(){{
                put(ResourceTypeSingleton.getInstance().getGoldResource(), 2);
                put(ResourceTypeSingleton.getInstance().getAnyResource(), 3);
            }},
            new HashMap<>(){{
                put(ResourceTypeSingleton.getInstance().getStoneResource(), 1);
                put(ResourceTypeSingleton.getInstance().getGoldResource(), 3);
            }},
            2
    ));

    @Test
    public void creation(){
        ClientProduction production = new ClientProduction(3);
        assertEquals(3, production.getUpgradableCraftings().size());
    }

    @Test
    public void addUpgradableCrafting(){
        ClientProduction production = new ClientProduction(3);
        production.addUpgradableCrafting(normalCrafting, 1, 2);
        assertEquals(normalCrafting, production.getUpgradableCraftings().get(2));
        assertEquals(1, production.getUpgradableLevels().get(2));
    }

    @Test
    public void addBaseCrafting(){
        ClientProduction production = new ClientProduction(3);
        production.addBaseCrafting(normalCrafting);
        assertEquals(normalCrafting, production.getBaseCraftings().get(0));
    }

    @Test
    public void addLeaderCrafting(){
        ClientProduction production = new ClientProduction(3);
        production.addLeaderCrafting(normalCrafting);
        assertEquals(normalCrafting, production.getLeaderCraftings().get(0));
    }

    @Test
    public void selectCrafting(){
        ClientProduction production = new ClientProduction(3);
        production.addBaseCrafting(faithCrafting);
        production.selectCrafting(Production.CraftingType.BASE, 0);
        assertEquals(0, production.getSelectedCraftingIndex());
        assertEquals(Production.CraftingType.BASE, production.getSelectedType());
    }

    @Test
    public void unselect(){
        ClientProduction production = new ClientProduction(3);
        production.addBaseCrafting(faithCrafting);
        production.selectCrafting(Production.CraftingType.BASE, 0);
        production.unselect();
        assertNull(production.getSelectedCraftingIndex());
        assertNull(production.getSelectedType());
    }



}
