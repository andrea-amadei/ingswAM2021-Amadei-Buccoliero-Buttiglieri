package it.polimi.ingsw;

import it.polimi.ingsw.server.model.basetypes.ResourceTypeSingleton;
import it.polimi.ingsw.server.model.global.Board;
import it.polimi.ingsw.server.model.global.Player;
import it.polimi.ingsw.server.model.holder.FaithHolder;
import it.polimi.ingsw.server.model.leader.StorageAbility;
import it.polimi.ingsw.server.model.production.Production;
import it.polimi.ingsw.server.model.storage.BaseStorage;
import it.polimi.ingsw.server.model.storage.Shelf;
import it.polimi.ingsw.server.model.storage.Storage;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class StorageAbilityTests {

    @Test
    public void storageAbilityConstructorTest(){

        Shelf shelf = new Shelf("shelf1", ResourceTypeSingleton.getInstance().getServantResource(), 2);
        StorageAbility storage = new StorageAbility(shelf);

        assertEquals(storage.getShelf(), shelf);

    }

    @Test
    public void exceptionOnStorageAbilityConstructor(){

        assertThrows(NullPointerException.class, ()-> new StorageAbility(null));

    }

    @Test
    public void activateMethodTest(){

        Shelf shelf = new Shelf("buh", ResourceTypeSingleton.getInstance().getShieldResource(), 2);
        StorageAbility storageAbility = new StorageAbility(shelf);

        Storage storage = new Storage(new BaseStorage("Chest"), new BaseStorage("Hand"), new BaseStorage("MarketBasket"),
                Arrays.asList(new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3),
                        new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2),
                        new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1)));

        Production production = new Production(3);
        FaithHolder faithHolder = new FaithHolder(3);

        Board b = new Board(storage, production, faithHolder);
        Player player = new Player("Ernestino", 0, b);

        storageAbility.activate(player);

        assertTrue(player.getBoard().getStorage().getCupboard().contains(shelf));

    }

    @Test
    public void exceptionOnActivateMethod(){

        assertThrows(NullPointerException.class, ()-> new StorageAbility(new Shelf
                ("shelf1", ResourceTypeSingleton.getInstance().getGoldResource(), 1))
                .activate(null));

    }

}
