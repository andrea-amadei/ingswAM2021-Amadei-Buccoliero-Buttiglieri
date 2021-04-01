package it.polimi.ingsw;

import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.market.ConversionActuator;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ConversionActuatorTest {

    private final ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
    private final ResourceSingle stone = ResourceTypeSingleton.getInstance().getStoneResource();
    private final ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
    private final ResourceSingle shield = ResourceTypeSingleton.getInstance().getShieldResource();

    @Test
    public void validCreation(){
        ConversionActuator actuator = new ConversionActuator(Collections.singletonList(gold), 0);
        assertEquals(0, actuator.getFaith());
        assertEquals(gold, actuator.getResources().get(0));
    }

    @Test
    public void nullPointerCreation(){
        assertThrows(NullPointerException.class, ()->new ConversionActuator(null, 0));
    }

    @Test
    public void negativeFaithCreation(){
        assertThrows(IllegalArgumentException.class, ()->new ConversionActuator(Collections.singletonList(servant), -1));
    }

    @Test
    public void addValidResourcesAndNoFaith(){
        Player player = new Player("John", 2);
        ConversionActuator actuator = new ConversionActuator(Collections.singletonList(stone), 0);
        assertDoesNotThrow(()->actuator.actuateConversion(player));

        Map<ResourceSingle, Integer> expectedResources = new HashMap<>();
        expectedResources.put(stone, 1);

        assertEquals(expectedResources, player.getBoard().getStorage().getMarketBasket().getAllResources());
        assertEquals(0, player.getBoard().getFaithHolder().getFaithPoints());
    }

    @Test
    public void addValidResourcesAndFaith(){
        Player player = new Player("John", 2);
        ConversionActuator actuator = new ConversionActuator(Collections.singletonList(servant), 3);
        assertDoesNotThrow(()->actuator.actuateConversion(player));

        Map<ResourceSingle, Integer> expectedResources = new HashMap<>();
        expectedResources.put(servant, 1);

        assertEquals(expectedResources, player.getBoard().getStorage().getMarketBasket().getAllResources());
        assertEquals(3, player.getBoard().getFaithHolder().getFaithPoints());
    }

    @Test
    public void addNoResourcesAndFaith(){
        Player player = new Player("John", 2);
        ConversionActuator actuator = new ConversionActuator(new ArrayList<>(), 3);
        assertDoesNotThrow(()->actuator.actuateConversion(player));

        Map<ResourceSingle, Integer> expectedResources = new HashMap<>();

        assertEquals(expectedResources, player.getBoard().getStorage().getMarketBasket().getAllResources());
        assertEquals(3, player.getBoard().getFaithHolder().getFaithPoints());
    }

    @Test
    public void addMultipleResources(){
        Player player = new Player("John", 2);
        ConversionActuator actuator = new ConversionActuator(Arrays.asList(gold, stone, shield), 3);
        assertDoesNotThrow(()->actuator.actuateConversion(player));

        Map<ResourceSingle, Integer> expectedResources = new HashMap<>();
        expectedResources.put(gold, 1);
        expectedResources.put(stone, 1);
        expectedResources.put(shield, 1);

        assertEquals(expectedResources, player.getBoard().getStorage().getMarketBasket().getAllResources());
        assertEquals(3, player.getBoard().getFaithHolder().getFaithPoints());
    }

    @Test
    public void nullPlayer(){
        ConversionActuator actuator = new ConversionActuator(Arrays.asList(gold, stone, shield), 3);
        assertThrows(NullPointerException.class, ()->actuator.actuateConversion(null));
    }

}
