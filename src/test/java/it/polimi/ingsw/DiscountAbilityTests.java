package it.polimi.ingsw;

import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.leader.DiscountAbility;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DiscountAbilityTests {

    @Test
    public void discountAbilityConstructorTest(){

        DiscountAbility discount = new DiscountAbility(-5, ResourceTypeSingleton.getInstance().getGoldResource());
        ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();

        assertEquals(-5, discount.getAmount());
        assertNotEquals(discount.getAmount(), 5);
        assertEquals(gold, discount.getResource());
    }

    @Test
    public void exceptionOnDiscountAbilityConstructor(){

        assertThrows(IllegalArgumentException.class, ()-> new DiscountAbility(0, ResourceTypeSingleton.getInstance().getServantResource()));
        assertThrows(NullPointerException.class, ()-> new DiscountAbility(4, null));

    }

    @Test
    public void activateMethodTest(){

        ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
        Player player = new Player("Username", 0);
        DiscountAbility discount = new DiscountAbility(3, gold);
        discount.activate(player);

        assertNotNull(player.getBoard().getDiscountHolder().getDiscounts());
        assertEquals(player.getBoard().getDiscountHolder().getDiscounts().size(), 1);

    }

    @Test
    public void exceptionOnActivateMethod(){

        assertThrows(NullPointerException.class, ()-> new DiscountAbility(2, ResourceTypeSingleton.getInstance().getGoldResource()).activate(null));

    }


}
