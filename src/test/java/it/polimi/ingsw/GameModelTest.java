package it.polimi.ingsw;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.GameModel;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameModelTest {

    private final List<Player> players = Arrays.asList(new Player("John", 0), new Player("Alice", 2));

    @Test
    public void createGame(){
        assertDoesNotThrow(()-> new GameModel(players));
    }

    @Test
    public void checkPlayers(){
        GameModel game = new GameModel(players);
        assertEquals(players, game.getPlayers());
    }

    @Test
    public void checkMarket(){
        GameModel game = new GameModel(players);
        assertDoesNotThrow(game::getMarket);
    }

    @Test
    public void checkShop(){
        GameModel game = new GameModel(players);
        assertDoesNotThrow(game::getShop);
    }

    @Test
    public void checkFaithPath(){
        GameModel game = new GameModel(players);
        assertDoesNotThrow(game::getFaithPath);
    }

    @Test
    public void checkLeaderCards(){
        GameModel game = new GameModel(players);
        assertDoesNotThrow(game::getLeaderCards);
    }
}
