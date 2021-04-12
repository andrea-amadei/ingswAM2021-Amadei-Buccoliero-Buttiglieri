package it.polimi.ingsw;

import it.polimi.ingsw.gamematerials.MarbleColor;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.leader.ConversionAbility;
import it.polimi.ingsw.model.market.ConversionActuator;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConversionAbilityTests {

    @Test
    public void conversionAbilityConstructorTest(){
        ResourceSingle stone = ResourceTypeSingleton.getInstance().getStoneResource();

        ConversionAbility conversion = new ConversionAbility(MarbleColor.BLUE,
                new ConversionActuator(Collections.singletonList(stone), 0));

        assertEquals(conversion.getFrom(), MarbleColor.BLUE);
        assertEquals(stone, conversion.getTo().getResources().get(0));
    }

    @Test
    public void exceptionOnConversionAbilityConstructor(){
        ResourceSingle stone = ResourceTypeSingleton.getInstance().getStoneResource();

        assertThrows(NullPointerException.class, ()-> new ConversionAbility(null,
                new ConversionActuator(Collections.singletonList(stone), 0)));
        assertThrows(NullPointerException.class, ()-> new ConversionAbility(MarbleColor.BLUE, null));

    }

    @Test
    public void activateMethodTest(){
        ResourceSingle stone = ResourceTypeSingleton.getInstance().getStoneResource();
        ConversionActuator conversionActuator = new ConversionActuator(Collections.singletonList(stone), 2);
        ConversionAbility conversionAbility = new ConversionAbility(MarbleColor.BLUE, conversionActuator);
        Player player = new Player("Username", 0);

        conversionAbility.activate(player);

        assertEquals(player.getBoard().getConversionHolder().getActuatorsFromColor(MarbleColor.BLUE), Collections.singletonList(conversionActuator));
    }

    @Test
    public void exceptionOnActivateMethod(){
        ResourceSingle stone = ResourceTypeSingleton.getInstance().getStoneResource();
        ConversionActuator conversionActuator = new ConversionActuator(Collections.singletonList(stone), 2);
        ConversionAbility conversionAbility = new ConversionAbility(MarbleColor.BLUE, conversionActuator);

        assertThrows(NullPointerException.class, ()-> conversionAbility.activate(null));
    }


}
