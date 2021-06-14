package it.polimi.ingsw;

import it.polimi.ingsw.gamematerials.MarbleColor;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.holder.FaithHolder;
import it.polimi.ingsw.model.leader.ConversionAbility;
import it.polimi.ingsw.model.market.ConversionActuator;
import it.polimi.ingsw.model.production.Production;
import it.polimi.ingsw.model.storage.BaseStorage;
import it.polimi.ingsw.model.storage.Shelf;
import it.polimi.ingsw.model.storage.Storage;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
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
        Storage storage = new Storage(new BaseStorage("Chest"), new BaseStorage("Hand"), new BaseStorage("MarketBasket"),
                Arrays.asList(new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3),
                        new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2),
                        new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1)));

        Production production = new Production(3);
        FaithHolder faithHolder = new FaithHolder(3);

        Board b = new Board(storage, production, faithHolder);
        Player player = new Player("Username", 0, b);


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
