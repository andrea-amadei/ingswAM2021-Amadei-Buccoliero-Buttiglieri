package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceType;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.leader.CraftingAbility;
import it.polimi.ingsw.model.production.Crafting;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.HashMap;

public class CraftingAbilityTests {

    @Test
    public void craftingAbilityConstructorTest(){

        ResourceSingle shield = ResourceTypeSingleton.getInstance().getShieldResource();
        ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
        HashMap<ResourceType, Integer> input = new HashMap<>();
        HashMap<ResourceType, Integer> output = new HashMap<>();
        input.put(shield, 2);
        output.put(gold, 3);
        Crafting crafting = new Crafting(input, output, 1);

        CraftingAbility craftingAbility = new CraftingAbility(crafting);

        assertSame(craftingAbility.getCrafting(), crafting);

    }

    @Test
    public void exceptionOnCraftingAbilityConstructor(){

        assertThrows(NullPointerException.class, ()-> new CraftingAbility(null));

    }

    @Test
    public void activateMethodTest(){
        ResourceSingle shield = ResourceTypeSingleton.getInstance().getShieldResource();
        ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
        Player player = new Player("Username", 0);
        HashMap<ResourceType, Integer> input = new HashMap<>();
        HashMap<ResourceType, Integer> output = new HashMap<>();
        input.put(shield, 2);
        output.put(gold, 3);
        Crafting crafting = new Crafting(input, output, 1);
        CraftingAbility ability = new CraftingAbility(crafting);

        ability.activate(player);

        assertEquals(player.getBoard().getProduction().getAllLeaderCrafting(), Collections.singletonList(crafting));
    }

    @Test
    public void exceptionOnActivateMethod(){

        ResourceSingle shield = ResourceTypeSingleton.getInstance().getShieldResource();
        ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
        HashMap<ResourceType, Integer> input = new HashMap<>();
        HashMap<ResourceType, Integer> output = new HashMap<>();
        input.put(shield, 2);
        output.put(gold, 3);
        assertThrows(NullPointerException.class, ()-> new CraftingAbility(new Crafting(input, output, 1)).activate(null));
    }

}
