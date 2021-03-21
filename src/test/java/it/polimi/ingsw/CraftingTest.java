package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.exceptions.NegativeCraftingIngredientException;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceType;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.production.Crafting;
import it.polimi.ingsw.model.production.UpgradableCrafting;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class CraftingTest {
    @Test
    public void constructorTest() {
        HashMap<ResourceType, Integer> empty_input = new HashMap<>();
        HashMap<ResourceSingle, Integer> empty_output = new HashMap<>();

        HashMap<ResourceType, Integer> input = new HashMap<>();
        input.put(ResourceTypeSingleton.getInstance().getGoldResource(), 1);

        HashMap<ResourceSingle, Integer> output = new HashMap<>();
        output.put(ResourceTypeSingleton.getInstance().getStoneResource(), 1);

        HashMap<ResourceType, Integer> input_negative = new HashMap<>();
        input_negative.put(ResourceTypeSingleton.getInstance().getGoldResource(), -1);

        HashMap<ResourceSingle, Integer> output_negative = new HashMap<>();
        output_negative.put(ResourceTypeSingleton.getInstance().getStoneResource(), -1);

        assertThrows(NullPointerException.class, () -> new Crafting(null, output, 0));
        assertThrows(NullPointerException.class, () -> new Crafting(input, null, 0));
        assertThrows(IllegalArgumentException.class, () -> new Crafting(empty_input, output, 0));
        assertThrows(IllegalArgumentException.class, () -> new Crafting(input, empty_output, 0));
        assertThrows(NegativeCraftingIngredientException.class, () -> new Crafting(input, output_negative, 0));
        assertThrows(NegativeCraftingIngredientException.class, () -> new Crafting(input_negative, output, 0));
        assertThrows(IllegalArgumentException.class, () ->  new Crafting(input, output, -1));
    }

    @Test
    public void methodsTest() {
        HashMap<ResourceType, Integer> input = new HashMap<>();
        input.put(ResourceTypeSingleton.getInstance().getServantResource(), 1);
        input.put(ResourceTypeSingleton.getInstance().getShieldResource(), 2);

        HashMap<ResourceSingle, Integer> output = new HashMap<>();
        output.put(ResourceTypeSingleton.getInstance().getStoneResource(), 1);
        output.put(ResourceTypeSingleton.getInstance().getGoldResource(), 2);

        Crafting crafting = new Crafting(input, output, 1);

        assertEquals(crafting.getInput(), input);
        assertEquals(crafting.getOutput(), output);
        assertEquals(crafting.getFaithOutput(), 1);
    }

    @Test
    public void upgradableCraftingTest() {
        HashMap<ResourceType, Integer> input = new HashMap<>();
        input.put(ResourceTypeSingleton.getInstance().getServantResource(), 1);
        input.put(ResourceTypeSingleton.getInstance().getShieldResource(), 2);

        HashMap<ResourceSingle, Integer> output = new HashMap<>();
        output.put(ResourceTypeSingleton.getInstance().getStoneResource(), 1);
        output.put(ResourceTypeSingleton.getInstance().getGoldResource(), 2);

        assertThrows(IllegalArgumentException.class, () -> new UpgradableCrafting(input, output, 1, 4));

        UpgradableCrafting crafting = new UpgradableCrafting(input, output, 1, 2);

        assertEquals(crafting.getLevel(), 2);
    }
}