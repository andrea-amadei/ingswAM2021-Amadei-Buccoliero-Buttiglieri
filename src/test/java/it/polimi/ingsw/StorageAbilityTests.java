package it.polimi.ingsw;

import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.leader.StorageAbility;
import it.polimi.ingsw.model.storage.Shelf;
import org.junit.jupiter.api.Test;

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
        Player player = new Player("Ernestino", 2);
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
