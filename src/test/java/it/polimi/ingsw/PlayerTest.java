package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.server.model.basetypes.ResourceTypeSingleton;
import it.polimi.ingsw.server.model.global.Board;
import it.polimi.ingsw.server.model.global.Player;
import it.polimi.ingsw.server.model.holder.FaithHolder;
import it.polimi.ingsw.server.model.production.Production;
import it.polimi.ingsw.server.model.storage.BaseStorage;
import it.polimi.ingsw.server.model.storage.Shelf;
import it.polimi.ingsw.server.model.storage.Storage;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class PlayerTest {

    @Test
    public void methodsTest() {
        Storage storage = new Storage(new BaseStorage("Chest"), new BaseStorage("Hand"), new BaseStorage("MarketBasket"),
                Arrays.asList(new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3),
                        new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2),
                        new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1)));

        Production production = new Production(3);
        FaithHolder faithHolder = new FaithHolder(3);

        Board b = new Board(storage, production, faithHolder);
        Player player = new Player("abcd", 0, b);

        assertEquals(player.getUsername(), "abcd");
        assertTrue(player.isConnected());
        assertEquals(player.getArrivalId(), 0);
        assertEquals(player.getPoints(), 0);

        player.setConnected(false);
        assertFalse(player.isConnected());
        player.setConnected(true);
        assertTrue(player.isConnected());

        player.addPoints(10);
        assertEquals(player.getPoints(), 10);
        assertThrows(IllegalArgumentException.class, () -> player.addPoints(-1));
        player.resetPoints();
        assertEquals(player.getPoints(), 0);
    }

    @Test
    public void getBoardTest(){
        Storage storage = new Storage(new BaseStorage("Chest"), new BaseStorage("Hand"), new BaseStorage("MarketBasket"),
                Arrays.asList(new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3),
                        new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2),
                        new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1)));

        Production production = new Production(3);
        FaithHolder faithHolder = new FaithHolder(3);

        Board b = new Board(storage, production, faithHolder);
        Player player = new Player("John", 0, b);
        assertNotNull(player.getBoard());
    }
}
