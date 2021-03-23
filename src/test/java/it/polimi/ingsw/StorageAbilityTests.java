package it.polimi.ingsw;

import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.leader.StorageAbility;
import it.polimi.ingsw.model.storage.Shelf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

}
