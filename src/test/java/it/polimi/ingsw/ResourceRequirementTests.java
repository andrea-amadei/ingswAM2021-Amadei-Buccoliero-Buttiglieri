package it.polimi.ingsw;

import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.leader.ResourceRequirement;
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
    public void isSatisfiedTest(){

        ResourceRequirement resourceRequirement = new ResourceRequirement(ResourceTypeSingleton.getInstance().getShieldResource(), 4);

        assertFalse(resourceRequirement.isSatisfied(new Player("Player", 3)));

    }

    @Test
    public void exceptionOnResourceRequirementConstructor(){

        assertThrows(IllegalArgumentException.class, ()-> new ResourceRequirement(ResourceTypeSingleton.getInstance().getShieldResource(),-6));
        assertThrows(IllegalArgumentException.class, ()-> new ResourceRequirement(ResourceTypeSingleton.getInstance().getShieldResource(),0));
        assertThrows(NullPointerException.class, ()-> new ResourceRequirement(null, 4));

    }
}
