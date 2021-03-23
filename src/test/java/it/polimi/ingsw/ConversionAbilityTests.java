package it.polimi.ingsw;

import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.leader.ConversionAbility;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConversionAbilityTests {

    @Test
    public void conversionAbilityConstructorTest(){

        ResourceSingle shield = ResourceTypeSingleton.getInstance().getShieldResource();
        ResourceSingle stone = ResourceTypeSingleton.getInstance().getStoneResource();
        List<ResourceSingle> resources = new ArrayList<>();
        resources.add(shield);
        resources.add(stone);

        ConversionAbility conversion = new ConversionAbility(shield, stone);

        assertEquals(conversion.getResources(), resources);
        assertNotNull(conversion.getResources());

    }

    @Test
    public void exceptionOnConversionAbilityConstructor(){

        assertThrows(NullPointerException.class, ()-> new ConversionAbility(null, ResourceTypeSingleton.getInstance().getGoldResource()));
        assertThrows(NullPointerException.class, ()-> new ConversionAbility(ResourceTypeSingleton.getInstance().getGoldResource(), null));

    }


}
