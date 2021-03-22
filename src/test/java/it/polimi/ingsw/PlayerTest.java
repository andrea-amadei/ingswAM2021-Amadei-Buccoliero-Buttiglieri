package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.Test;

public class PlayerTest {
    @Test
    public void constructorTest() {
        assertThrows(IllegalArgumentException.class, () -> new Player("a", 1));
        assertThrows(IllegalArgumentException.class, () -> new Player("abc", -1));
        assertThrows(NullPointerException.class, () -> new Player(null, 1));
    }

    @Test
    public void methodsTest() {
        Player player = new Player("abcd", 0);

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
}
