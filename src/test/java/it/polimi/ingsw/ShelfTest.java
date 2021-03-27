package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.exceptions.IllegalResourceTransferException;
import it.polimi.ingsw.gamematerials.ResourceGroup;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceType;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.storage.Shelf;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ShelfTest {
    @Test
    public void baseShelfConstruction(){
        Shelf s1 = new Shelf("Shelf 1", ResourceTypeSingleton.getInstance().getAnyResource(), 3);
        assertEquals(s1.getId(), "Shelf 1");
        assertEquals(s1.toString(), "Shelf 1 {}");
        assertEquals(s1.getAmount(), 0);
        assertNull(s1.getCurrentType());
    }

    @Test
    public void leaderShelfConstruction(){
        Shelf leaderShelf = new Shelf("LeaderShelf 1", ResourceTypeSingleton.getInstance().getGoldResource(), 2);
        assertEquals(leaderShelf.getId(), "LeaderShelf 1");
        assertEquals(leaderShelf.toString(), "LeaderShelf 1 {}");
        assertEquals(leaderShelf.getAmount(), 0);
        assertNull(leaderShelf.getCurrentType());
    }

    @Test
    public void exceptionOnConstruction(){
        assertThrows(NullPointerException.class, () -> new Shelf(null, ResourceTypeSingleton.getInstance().getGoldResource(), 3));
        assertThrows(NullPointerException.class, () -> new Shelf("Shelf", null, 3));
        assertThrows(IllegalArgumentException.class, () -> new Shelf("Shelf", ResourceTypeSingleton.getInstance().getGoldResource(), 0));
        assertThrows(IllegalArgumentException.class, () -> new Shelf("", ResourceTypeSingleton.getInstance().getGoldResource(), -3));
    }

    @Test
    public void addValidAmount(){
        ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
        ResourceType any = ResourceTypeSingleton.getInstance().getAnyResource();
        Shelf leaderShelf = new Shelf("LeaderShelf 1", any, 2);
        assertDoesNotThrow(()->leaderShelf.addResources(gold, 1));
        assertDoesNotThrow(()->leaderShelf.addResources(gold, 1));
        assertEquals(leaderShelf.getAmount(), 2);
        assertEquals(leaderShelf.getCurrentType(), gold);
    }

    @Test
    public void addValidAmountAndCheckCurrentType(){
        ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
        ResourceType any = ResourceTypeSingleton.getInstance().getAnyResource();
        Shelf leaderShelf = new Shelf("LeaderShelf 1", any, 2);
        assertDoesNotThrow(()->leaderShelf.addResources(gold, 1));
        assertEquals(leaderShelf.getCurrentType(), gold);
    }

    @Test
    public void addInvalidAmount(){
        ResourceSingle stone = ResourceTypeSingleton.getInstance().getStoneResource();
        Shelf shelf = new Shelf("Shelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3);
        assertDoesNotThrow(() -> shelf.addResources(stone, 3));
        assertThrows(IllegalResourceTransferException.class, ()->shelf.addResources(stone, 1));
        assertThrows(IllegalArgumentException.class, ()->shelf.addResources(stone, -1));
    }

    @Test
    public void addInvalidResourceType(){
        ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
        ResourceSingle shield = ResourceTypeSingleton.getInstance().getShieldResource();
        ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();

        Shelf shelf1 = new Shelf("Shelf", servant, 3);
        assertThrows(IllegalResourceTransferException.class, ()->shelf1.addResources(shield, 1));

        Shelf shelf2 = new Shelf("Shelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3);
        assertDoesNotThrow(()->shelf2.addResources(servant, 3));
        assertThrows(NullPointerException.class, ()->shelf2.addResources(null, 1));
        assertThrows(IllegalResourceTransferException.class, ()->shelf2.addResources(gold, 1));
    }

    @Test
    public void validRemove(){
        ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
        Shelf shelf = new Shelf("Shelf", servant, 3);
        assertDoesNotThrow(()->shelf.addResources(servant, 3));
        assertDoesNotThrow(()->shelf.removeResources(2));
        assertEquals(shelf.getAmount(), 1);
    }

    @Test
    public void validRemoveAndCheckCurrentType(){
        ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
        ResourceSingle shield = ResourceTypeSingleton.getInstance().getShieldResource();
        ResourceType newGroup = new ResourceGroup("misc", new HashSet<>(Arrays.asList(servant, shield)));
        Shelf shelf = new Shelf("Shelf", newGroup, 3);
        assertDoesNotThrow(()->shelf.addResources(servant, 3));
        assertEquals(shelf.getCurrentType(), servant);
        assertDoesNotThrow(()->shelf.removeResources(3));
        assertNull(shelf.getCurrentType());
    }

    @Test
    public void removeContainerOverloadBadParameters(){
        ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
        ResourceSingle shield = ResourceTypeSingleton.getInstance().getShieldResource();

        Shelf shelf = new Shelf("Shelf", servant, 2);
        assertThrows(IllegalArgumentException.class, ()->shelf.removeResources(shield, 0));
        assertThrows(IllegalResourceTransferException.class, ()->shelf.removeResources(shield, 1));


        assertThrows(IllegalArgumentException.class, ()->shelf.removeResources(shield, 0));
        assertThrows(IllegalResourceTransferException.class, ()->shelf.removeResources(servant, 1));
        assertDoesNotThrow(()->shelf.addResources(servant, 1));
        assertThrows(IllegalResourceTransferException.class, ()->shelf.removeResources(servant, 2));
        assertThrows(IllegalResourceTransferException.class,()->shelf.removeResources(servant, 3));
        assertThrows(NullPointerException.class, ()->shelf.removeResources(null, 2));
    }

    @Test
    public void invalidRemoveContainerOverload(){
        ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
        ResourceSingle shield = ResourceTypeSingleton.getInstance().getShieldResource();

        Shelf shelf = new Shelf("Shelf", servant, 2);
        assertDoesNotThrow(()->shelf.addResources(servant, 1));
        assertThrows(IllegalResourceTransferException.class, ()->shelf.removeResources(shield, 1));
    }

    @Test
    public void validGetAllResourcesWithEmptyShelf(){
        ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
        ResourceSingle shield = ResourceTypeSingleton.getInstance().getShieldResource();

        Shelf shelf = new Shelf("Shelf", servant, 2);

        assertEquals(shelf.getAllResources(), new HashMap<ResourceSingle, Integer>());
    }


    @Test
    public void validGetAllResourcesWithNonEmptyShelf(){
        ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
        ResourceSingle shield = ResourceTypeSingleton.getInstance().getShieldResource();

        Shelf shelf = new Shelf("Shelf", servant, 2);

        assertDoesNotThrow(()->shelf.addResources(servant, 1));

        Map<ResourceSingle, Integer> result = new HashMap<>();
        result.put(servant, 1);
        assertEquals(shelf.getAllResources(), result);
    }

}
