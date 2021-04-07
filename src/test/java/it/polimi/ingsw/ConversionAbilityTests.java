package it.polimi.ingsw;

import it.polimi.ingsw.gamematerials.MarbleColor;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.leader.ConversionAbility;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConversionAbilityTests {

    @Test
    public void conversionAbilityConstructorTest(){
        ResourceSingle stone = ResourceTypeSingleton.getInstance().getStoneResource();

        ConversionAbility conversion = new ConversionAbility(MarbleColor.BLUE, stone);

        assertEquals(conversion.getFrom(), MarbleColor.BLUE);
        assertEquals(conversion.getTo(), stone);
    }

    @Test
    public void exceptionOnConversionAbilityConstructor(){

        assertThrows(NullPointerException.class, ()-> new ConversionAbility(null, ResourceTypeSingleton.getInstance().getGoldResource()));
        assertThrows(NullPointerException.class, ()-> new ConversionAbility(MarbleColor.BLUE, null));

    }


}
