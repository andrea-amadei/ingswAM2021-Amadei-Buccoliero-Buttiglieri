package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.exceptions.UnsupportedShelfInsertionException;
import it.polimi.ingsw.gamematerials.ResourceGroup;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceType;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.storage.Shelf;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

public class ShelfTest {
    @Test
    public void baseShelfConstruction(){
        Shelf s1 = new Shelf("Shelf 1", ResourceTypeSingleton.getInstance().getAnyResource(), 3);
        assertEquals(s1.getId(), "Shelf 1");
        assertEquals(s1.toString(), "Shelf 1 {"+ ResourceTypeSingleton.getInstance().getAnyResource() + ": " + 0 + "}");
        assertEquals(s1.getAmount(), 0);
        assertEquals(s1.getCurrentType(), ResourceTypeSingleton.getInstance().getAnyResource());
    }

    @Test
    public void leaderShelfConstruction(){
        Shelf leaderShelf = new Shelf("LeaderShelf 1", ResourceTypeSingleton.getInstance().getGoldResource(), 2);
        assertEquals(leaderShelf.getId(), "LeaderShelf 1");
        assertEquals(leaderShelf.toString(), "LeaderShelf 1 {Gold: 0}");
        assertEquals(leaderShelf.getAmount(), 0);
        assertEquals(leaderShelf.getCurrentType(), ResourceTypeSingleton.getInstance().getGoldResource());
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
        leaderShelf.addResources(gold, 1);
        leaderShelf.addResources(gold, 1);
        assertEquals(leaderShelf.getAmount(), 2);
        assertEquals(leaderShelf.getCurrentType(), gold);
    }

    @Test
    public void addValidAmountAndCheckCurrentType(){
        ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
        ResourceType any = ResourceTypeSingleton.getInstance().getAnyResource();
        Shelf leaderShelf = new Shelf("LeaderShelf 1", any, 2);
        leaderShelf.addResources(gold, 1);
        assertEquals(leaderShelf.getCurrentType(), gold);
    }

    @Test
    public void addInvalidAmount(){
        ResourceSingle stone = ResourceTypeSingleton.getInstance().getStoneResource();
        Shelf shelf = new Shelf("Shelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3);
        assertDoesNotThrow(() -> shelf.addResources(stone, 3));
        assertThrows(UnsupportedShelfInsertionException.class, ()->shelf.addResources(stone, 1));
    }

    @Test
    public void addInvalidResourceType(){
        ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
        ResourceSingle shield = ResourceTypeSingleton.getInstance().getShieldResource();
        Shelf shelf = new Shelf("Shelf", servant, 3);
        assertThrows(UnsupportedShelfInsertionException.class, ()->shelf.addResources(shield, 1));
    }

    @Test
    public void validRemove(){
        ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
        Shelf shelf = new Shelf("Shelf", servant, 3);
        shelf.addResources(servant, 3);
        shelf.removeResources(2);
        assertEquals(shelf.getAmount(), 1);
    }

    @Test
    public void validRemoveAndCheckCurrentType(){
        ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
        ResourceSingle shield = ResourceTypeSingleton.getInstance().getShieldResource();
        ResourceType newGroup = new ResourceGroup("misc", new HashSet<>(Arrays.asList(servant, shield)));
        Shelf shelf = new Shelf("Shelf", newGroup, 3);
        shelf.addResources(servant, 3);
        assertEquals(shelf.getCurrentType(), servant);
        shelf.removeResources(3);
        assertEquals(shelf.getCurrentType(), newGroup);
    }

}
