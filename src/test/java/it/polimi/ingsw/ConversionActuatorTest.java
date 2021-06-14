package it.polimi.ingsw;

import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.FaithPath;
import it.polimi.ingsw.model.FaithPathTile;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.holder.FaithHolder;
import it.polimi.ingsw.model.market.ConversionActuator;
import it.polimi.ingsw.model.production.Production;
import it.polimi.ingsw.model.storage.BaseStorage;
import it.polimi.ingsw.model.storage.Shelf;
import it.polimi.ingsw.model.storage.Storage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ConversionActuatorTest {

    private final ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
    private final ResourceSingle stone = ResourceTypeSingleton.getInstance().getStoneResource();
    private final ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
    private final ResourceSingle shield = ResourceTypeSingleton.getInstance().getShieldResource();

    private FaithPath standardFaithPath;

    @BeforeAll
    public void init(){
        List<FaithPathTile> tiles = new ArrayList<>();
        for(int i = 0; i < 10; i++)
            tiles.add(new FaithPathTile(i+1,1, i, 2, 0, false));

        standardFaithPath = new FaithPath(new ArrayList<>(), tiles);
    }

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
        Storage storage = new Storage(new BaseStorage("Chest"), new BaseStorage("Hand"), new BaseStorage("MarketBasket"),
                Arrays.asList(new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3),
                        new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2),
                        new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1)));

        Production production = new Production(3);
        FaithHolder faithHolder = new FaithHolder(3);

        Board b = new Board(storage, production, faithHolder);
        Player player = new Player("John", 2, b);

        ConversionActuator actuator = new ConversionActuator(Collections.singletonList(stone), 0);
        assertDoesNotThrow(()->actuator.actuateConversion(player, standardFaithPath));

        Map<ResourceSingle, Integer> expectedResources = new HashMap<>();
        expectedResources.put(stone, 1);

        assertEquals(expectedResources, player.getBoard().getStorage().getMarketBasket().getAllResources());
        assertEquals(0, player.getBoard().getFaithHolder().getFaithPoints());
    }

    @Test
    public void addValidResourcesAndFaith(){
        Storage storage = new Storage(new BaseStorage("Chest"), new BaseStorage("Hand"), new BaseStorage("MarketBasket"),
                Arrays.asList(new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3),
                        new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2),
                        new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1)));

        Production production = new Production(3);
        FaithHolder faithHolder = new FaithHolder(3);

        Board b = new Board(storage, production, faithHolder);
        Player player = new Player("John", 2, b);

        ConversionActuator actuator = new ConversionActuator(Collections.singletonList(servant), 3);
        assertDoesNotThrow(()->actuator.actuateConversion(player, standardFaithPath));

        Map<ResourceSingle, Integer> expectedResources = new HashMap<>();
        expectedResources.put(servant, 1);

        assertEquals(expectedResources, player.getBoard().getStorage().getMarketBasket().getAllResources());
        assertEquals(3, player.getBoard().getFaithHolder().getFaithPoints());
    }

    @Test
    public void addNoResourcesAndFaith(){
        Storage storage = new Storage(new BaseStorage("Chest"), new BaseStorage("Hand"), new BaseStorage("MarketBasket"),
                Arrays.asList(new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3),
                        new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2),
                        new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1)));

        Production production = new Production(3);
        FaithHolder faithHolder = new FaithHolder(3);

        Board b = new Board(storage, production, faithHolder);
        Player player = new Player("John", 2, b);

        ConversionActuator actuator = new ConversionActuator(new ArrayList<>(), 3);
        assertDoesNotThrow(()->actuator.actuateConversion(player, standardFaithPath));

        Map<ResourceSingle, Integer> expectedResources = new HashMap<>();

        assertEquals(expectedResources, player.getBoard().getStorage().getMarketBasket().getAllResources());
        assertEquals(3, player.getBoard().getFaithHolder().getFaithPoints());
    }

    @Test
    public void addMultipleResources(){
        Storage storage = new Storage(new BaseStorage("Chest"), new BaseStorage("Hand"), new BaseStorage("MarketBasket"),
                Arrays.asList(new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3),
                        new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2),
                        new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1)));

        Production production = new Production(3);
        FaithHolder faithHolder = new FaithHolder(3);

        Board b = new Board(storage, production, faithHolder);
        Player player = new Player("John", 2, b);

        ConversionActuator actuator = new ConversionActuator(Arrays.asList(gold, stone, shield), 3);
        assertDoesNotThrow(()->actuator.actuateConversion(player, standardFaithPath));

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
        assertThrows(NullPointerException.class, ()->actuator.actuateConversion(null, null));
    }

}
