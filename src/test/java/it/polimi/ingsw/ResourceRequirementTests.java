package it.polimi.ingsw;

import it.polimi.ingsw.server.model.basetypes.ResourceSingle;
import it.polimi.ingsw.server.model.basetypes.ResourceTypeSingleton;
import it.polimi.ingsw.server.model.global.Board;
import it.polimi.ingsw.server.model.global.Player;
import it.polimi.ingsw.server.model.holder.FaithHolder;
import it.polimi.ingsw.server.model.leader.ResourceRequirement;
import it.polimi.ingsw.server.model.production.Production;
import it.polimi.ingsw.server.model.storage.BaseStorage;
import it.polimi.ingsw.server.model.storage.Shelf;
import it.polimi.ingsw.server.model.storage.Storage;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ResourceRequirementTests {

    @Test
    public void resourceRequirementConstructorTest(){

        ResourceSingle shield = ResourceTypeSingleton.getInstance().getShieldResource();
        ResourceRequirement resourceRequirement = new ResourceRequirement(shield, 6);

        assertEquals(resourceRequirement.getResource(), shield);
        assertEquals(resourceRequirement.getAmount(), 6);
        assertNotNull(resourceRequirement.getResource());

    }

    @Test
    public void exceptionOnResourceRequirementConstructor(){

        assertThrows(IllegalArgumentException.class, ()-> new ResourceRequirement(ResourceTypeSingleton.getInstance().getShieldResource(),-6));
        assertThrows(IllegalArgumentException.class, ()-> new ResourceRequirement(ResourceTypeSingleton.getInstance().getShieldResource(),0));
        assertThrows(NullPointerException.class, ()-> new ResourceRequirement(null, 4));

    }

    @Test
    public void isSatisfiedMethodTest(){

        Storage storage = new Storage(new BaseStorage("Chest"), new BaseStorage("Hand"), new BaseStorage("MarketBasket"),
                Arrays.asList(new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3),
                        new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2),
                        new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1)));

        Production production = new Production(3);
        FaithHolder faithHolder = new FaithHolder(3);

        Board b = new Board(storage, production, faithHolder);
        Player player = new Player("Player", 2, b);
        ResourceSingle shield = ResourceTypeSingleton.getInstance().getShieldResource();
        ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
        ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
        ResourceRequirement resourceRequirement = new ResourceRequirement(shield, 6);
        ResourceRequirement resourceRequirement1 = new ResourceRequirement(gold, 4);
        ResourceRequirement resourceRequirement2 = new ResourceRequirement(servant, 2);
        Shelf shelf = player.getBoard().getStorage().getCupboard().getShelfById("BottomShelf");

        assertDoesNotThrow(()->player.getBoard().getStorage().getChest().addResources(shield, 7));
        assertDoesNotThrow(()->player.getBoard().getStorage().getCupboard().addResource(shelf, gold, 2));

        assertTrue(resourceRequirement.isSatisfied(player));
        assertFalse(resourceRequirement1.isSatisfied(player));
        assertFalse(resourceRequirement2.isSatisfied(player));

    }

    @Test
    public void exceptionOnIsSatisfiedMethod(){

        ResourceSingle shield = ResourceTypeSingleton.getInstance().getShieldResource();
        ResourceRequirement resourceRequirement = new ResourceRequirement(shield, 6);

        assertThrows(NullPointerException.class, ()-> resourceRequirement.isSatisfied(null));

    }
}
