package it.polimi.ingsw;

import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.holder.FaithHolder;
import it.polimi.ingsw.model.leader.DiscountAbility;
import it.polimi.ingsw.model.production.Production;
import it.polimi.ingsw.model.storage.BaseStorage;
import it.polimi.ingsw.model.storage.Shelf;
import it.polimi.ingsw.model.storage.Storage;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

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
        Storage storage = new Storage(new BaseStorage("Chest"), new BaseStorage("Hand"), new BaseStorage("MarketBasket"),
                Arrays.asList(new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3),
                        new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2),
                        new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1)));

        Production production = new Production(3);
        FaithHolder faithHolder = new FaithHolder(3);

        Board b = new Board(storage, production, faithHolder);
        Player player = new Player("Username", 0, b);
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
