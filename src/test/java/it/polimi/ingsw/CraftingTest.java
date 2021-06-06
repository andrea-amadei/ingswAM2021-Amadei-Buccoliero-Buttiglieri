package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.exceptions.NegativeCraftingIngredientException;
import it.polimi.ingsw.exceptions.NotReadyToCraftException;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceType;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.FaithPath;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.production.Crafting;
import it.polimi.ingsw.model.production.UpgradableCrafting;
import it.polimi.ingsw.parser.JSONParser;
import it.polimi.ingsw.utils.ResourceLoader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;

@DisplayName("Crafting and UpgradableCrafting tests")
public class CraftingTest {
    @Test
    @DisplayName("Construction")
    public void constructionTest() {
        HashMap<ResourceType, Integer> empty_input = new HashMap<>();
        HashMap<ResourceType, Integer> empty_output = new HashMap<>();

        HashMap<ResourceType, Integer> input = new HashMap<>();
        input.put(ResourceTypeSingleton.getInstance().getGoldResource(), 1);
        input.put(ResourceTypeSingleton.getInstance().getAnyResource(), 1);

        HashMap<ResourceType, Integer> output = new HashMap<>();
        output.put(ResourceTypeSingleton.getInstance().getStoneResource(), 1);
        output.put(ResourceTypeSingleton.getInstance().getAnyResource(), 1);

        HashMap<ResourceType, Integer> input_negative = new HashMap<>();
        input_negative.put(ResourceTypeSingleton.getInstance().getGoldResource(), -1);

        HashMap<ResourceType, Integer> output_negative = new HashMap<>();
        output_negative.put(ResourceTypeSingleton.getInstance().getStoneResource(), -1);

        assertThrows(NullPointerException.class, () -> new Crafting(null, output, 1));
        assertThrows(NullPointerException.class, () -> new Crafting(input, null, 1));
        assertThrows(IllegalArgumentException.class, () -> new Crafting(empty_input, output, 1));
        assertThrows(IllegalArgumentException.class, () -> new Crafting(input, empty_output, 0));
        assertThrows(NegativeCraftingIngredientException.class, () -> new Crafting(input, output_negative, 1));
        assertThrows(NegativeCraftingIngredientException.class, () -> new Crafting(input_negative, output, 1));
        assertThrows(IllegalArgumentException.class, () ->  new Crafting(input, output, -1));

        assertDoesNotThrow(() -> new Crafting(input, output, 1));
    }

    @Test
    @DisplayName("Getters")
    public void getTest() {
        HashMap<ResourceType, Integer> input = new HashMap<>();
        input.put(ResourceTypeSingleton.getInstance().getServantResource(), 1);
        input.put(ResourceTypeSingleton.getInstance().getShieldResource(), 2);

        HashMap<ResourceType, Integer> output = new HashMap<>();
        output.put(ResourceTypeSingleton.getInstance().getStoneResource(), 1);
        output.put(ResourceTypeSingleton.getInstance().getGoldResource(), 2);

        Crafting crafting = new Crafting(input, output, 1);

        assertEquals(crafting.getInput(), input);
        assertEquals(crafting.getOutput(), output);
        assertEquals(crafting.getFaithOutput(), 1);
    }

    @Test
    @DisplayName("Conversion")
    void conversionTest() {
        HashMap<ResourceType, Integer> input = new HashMap<>();
        input.put(ResourceTypeSingleton.getInstance().getGoldResource(), 1);
        input.put(ResourceTypeSingleton.getInstance().getAnyResource(), 1);

        HashMap<ResourceType, Integer> output1 = new HashMap<>();
        output1.put(ResourceTypeSingleton.getInstance().getStoneResource(), 1);
        output1.put(ResourceTypeSingleton.getInstance().getAnyResource(), 1);

        HashMap<ResourceType, Integer> output2 = new HashMap<>();
        output2.put(ResourceTypeSingleton.getInstance().getStoneResource(), 1);

        HashMap<ResourceSingle, Integer> conversion1 = new HashMap<>();
        conversion1.put(ResourceTypeSingleton.getInstance().getStoneResource(), 2);

        HashMap<ResourceSingle, Integer> conversion2 = new HashMap<>();
        conversion2.put(ResourceTypeSingleton.getInstance().getStoneResource(), 1);

        Crafting crafting1 = new Crafting(input, output2, 1);

        assertEquals(crafting1.getUndecidedOutputs().size(), 0);
        assertThrows(NullPointerException.class, () -> crafting1.getGroupConversion(null));
        assertThrows(IllegalArgumentException.class, () ->
                crafting1.getGroupConversion(ResourceTypeSingleton.getInstance().getAnyResource()));

        assertThrows(NullPointerException.class, () -> crafting1.setGroupConversion(null, conversion2));
        assertThrows(NullPointerException.class, () ->
                crafting1.setGroupConversion(ResourceTypeSingleton.getInstance().getAnyResource(), null));
        assertThrows(IllegalArgumentException.class, () ->
                crafting1.setGroupConversion(ResourceTypeSingleton.getInstance().getAnyResource(), conversion2));

        Crafting crafting2 = new Crafting(input, output1, 1);

        assertThrows(IllegalArgumentException.class, () ->
                crafting2.setGroupConversion(ResourceTypeSingleton.getInstance().getAnyResource(), conversion1));

        assertEquals(crafting2.getUndecidedOutputs().size(), 1);
        assertNull(crafting2.getGroupConversion(ResourceTypeSingleton.getInstance().getAnyResource()));

        crafting2.setGroupConversion(ResourceTypeSingleton.getInstance().getAnyResource(), conversion2);

        assertEquals(crafting2.getUndecidedOutputs().size(), 0);
        assertEquals(crafting2.getGroupConversion(ResourceTypeSingleton.getInstance().getAnyResource()), conversion2);
    }

    @Test
    @DisplayName("Activate crafting")
    public void activateCraftingTest() throws ParserException, IOException {
        Player player = new Player("Name", 0);


        FaithPath fp = JSONParser.parseFaithPath(ResourceLoader.getPathFromResource("cfg/faith.json"));

        HashMap<ResourceType, Integer> input = new HashMap<>();
        input.put(ResourceTypeSingleton.getInstance().getGoldResource(), 1);
        input.put(ResourceTypeSingleton.getInstance().getAnyResource(), 1);

        HashMap<ResourceType, Integer> output = new HashMap<>();
        output.put(ResourceTypeSingleton.getInstance().getStoneResource(), 1);
        output.put(ResourceTypeSingleton.getInstance().getAnyResource(), 1);

        HashMap<ResourceSingle, Integer> conversion = new HashMap<>();
        conversion.put(ResourceTypeSingleton.getInstance().getStoneResource(), 1);

        Crafting crafting = new Crafting(input, output, 1);

        assertThrows(NullPointerException.class, () -> crafting.activateCrafting(null, fp));

        assertThrows(NotReadyToCraftException.class, () -> crafting.activateCrafting(player, fp));

        crafting.setGroupConversion(ResourceTypeSingleton.getInstance().getAnyResource(), conversion);

        assertEquals(player.getBoard().getStorage().getChest().getResources(ResourceTypeSingleton.getInstance().getStoneResource()), 0);
        assertEquals(player.getBoard().getFaithHolder().getFaithPoints(), 0);

        assertFalse(crafting.readyToCraft());
        assertFalse(crafting.hasAllResourcesTransferred());
        assertThrows(NotReadyToCraftException.class, () -> crafting.activateCrafting(player, fp));

        crafting.setAllResourcesTransferred(true);

        crafting.activateCrafting(player, fp);

        assertEquals(player.getBoard().getStorage().getChest().getResources(ResourceTypeSingleton.getInstance().getStoneResource()), 2);
        assertEquals(player.getBoard().getFaithHolder().getFaithPoints(), 1);
    }

    @Test
    @DisplayName("Upgradable Crafting")
    public void upgradableCraftingTest() {
        HashMap<ResourceType, Integer> input = new HashMap<>();
        input.put(ResourceTypeSingleton.getInstance().getServantResource(), 1);
        input.put(ResourceTypeSingleton.getInstance().getShieldResource(), 2);

        HashMap<ResourceType, Integer> output = new HashMap<>();
        output.put(ResourceTypeSingleton.getInstance().getStoneResource(), 1);
        output.put(ResourceTypeSingleton.getInstance().getGoldResource(), 2);

        assertThrows(IllegalArgumentException.class, () -> new UpgradableCrafting(input, output, 1, 4));

        UpgradableCrafting crafting = new UpgradableCrafting(input, output, 1, 2);

        assertEquals(crafting.getLevel(), 2);
    }

    @Test
    @DisplayName("To string")
    public void toStringTest() {
        HashMap<ResourceType, Integer> input = new HashMap<>();
        input.put(ResourceTypeSingleton.getInstance().getServantResource(), 1);
        input.put(ResourceTypeSingleton.getInstance().getShieldResource(), 2);

        HashMap<ResourceType, Integer> output = new HashMap<>();
        output.put(ResourceTypeSingleton.getInstance().getStoneResource(), 1);
        output.put(ResourceTypeSingleton.getInstance().getGoldResource(), 2);

        Crafting crafting = new Crafting(input, output, 1);

        assertEquals("Crafting{shield: 2 servant: 1 -> stone: 1 gold: 2 + Faith: 1}", crafting.toString());
    }
}