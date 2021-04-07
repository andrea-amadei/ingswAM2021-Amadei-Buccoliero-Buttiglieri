package it.polimi.ingsw;

import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.leader.ResourceRequirement;
import it.polimi.ingsw.model.storage.Shelf;
import org.junit.jupiter.api.Test;

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

        Player player = new Player("Player", 2);
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
