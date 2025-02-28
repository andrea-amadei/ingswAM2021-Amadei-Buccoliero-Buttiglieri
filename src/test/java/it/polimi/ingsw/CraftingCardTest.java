package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.common.exceptions.NegativeCostException;
import it.polimi.ingsw.server.model.basetypes.*;
import it.polimi.ingsw.server.model.production.CraftingCard;
import it.polimi.ingsw.server.model.production.UpgradableCrafting;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class CraftingCardTest {
    @Test
    public void constructorTest() {
        LevelFlag flag = new LevelFlag(FlagColor.PURPLE, 2);
        HashMap<ResourceSingle, Integer> cost = new HashMap<>();
        cost.put(ResourceTypeSingleton.getInstance().getShieldResource(), 3);
        HashMap<ResourceType, Integer> input = new HashMap<>();
        input.put(ResourceTypeSingleton.getInstance().getShieldResource(), 2);
        HashMap<ResourceType, Integer> output = new HashMap<>();
        output.put(ResourceTypeSingleton.getInstance().getStoneResource(), 1);
        output.put(ResourceTypeSingleton.getInstance().getGoldResource(), 1);
        UpgradableCrafting crafting = new UpgradableCrafting(input, output, 2, 2);

        assertThrows(IllegalArgumentException.class, () -> new CraftingCard(-1, flag, cost, crafting, 5));
        assertThrows(NullPointerException.class, () -> new CraftingCard(1, null, cost, crafting, 5));
        assertThrows(NullPointerException.class, () -> new CraftingCard(1, flag, null, crafting, 5));
        assertThrows(NullPointerException.class, () -> new CraftingCard(1, flag, cost, null, 5));
        assertThrows(IllegalArgumentException.class, () -> new CraftingCard(1, flag, cost, crafting, -1));

        HashMap<ResourceSingle, Integer> cost_negative = new HashMap<>();
        cost_negative.put(ResourceTypeSingleton.getInstance().getGoldResource(), -1);
        HashMap<ResourceSingle, Integer> cost_empty = new HashMap<>();
        LevelFlag flag3 = new LevelFlag(FlagColor.PURPLE, 3);

        assertThrows(NegativeCostException.class, () -> new CraftingCard(1, flag, cost_negative, crafting, 5));
        assertThrows(IllegalArgumentException.class, () -> new CraftingCard(1, flag, cost_empty, crafting, 5));
        assertThrows(IllegalArgumentException.class, () -> new CraftingCard(1, flag3, cost, crafting, 5));
    }

    @Test
    public void methodsTest() {
        LevelFlag flag = new LevelFlag(FlagColor.PURPLE, 2);
        HashMap<ResourceSingle, Integer> cost = new HashMap<>();
        cost.put(ResourceTypeSingleton.getInstance().getShieldResource(), 3);
        HashMap<ResourceType, Integer> input = new HashMap<>();
        input.put(ResourceTypeSingleton.getInstance().getShieldResource(), 2);
        HashMap<ResourceType, Integer> output = new HashMap<>();
        output.put(ResourceTypeSingleton.getInstance().getStoneResource(), 1);
        output.put(ResourceTypeSingleton.getInstance().getGoldResource(), 1);
        UpgradableCrafting crafting = new UpgradableCrafting(input, output, 2, 2);

        CraftingCard card = new CraftingCard(1, flag, cost, crafting, 5);

        assertEquals(card.getId(), 1);
        assertEquals(card.getFlag(), flag);
        assertEquals(card.getCost(), cost);
        assertEquals(card.getCrafting(), crafting);
        assertEquals(card.getPoints(), 5);
    }
}
