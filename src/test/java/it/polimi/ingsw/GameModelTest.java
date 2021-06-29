package it.polimi.ingsw;

import it.polimi.ingsw.common.exceptions.ParserException;
import it.polimi.ingsw.server.model.global.GameModel;
import it.polimi.ingsw.server.ServerBuilder;
import it.polimi.ingsw.common.utils.ResourceLoader;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class GameModelTest {

    private final List<String> players = Arrays.asList("John", "Alice");
    private final String config = ResourceLoader.loadFile("cfg/config.json");
    private final String crafting = ResourceLoader.loadFile("cfg/crafting.json");
    private final String faith = ResourceLoader.loadFile("cfg/faith.json");
    private final String leaders = ResourceLoader.loadFile("cfg/leaders.json");

    @Test
    public void createGame(){
        assertDoesNotThrow(()-> ServerBuilder.buildModel(config, crafting, faith, leaders, players, false, new Random(3)));
    }

    @Test
    public void checkPlayers() throws ParserException {
        GameModel game = ServerBuilder.buildModel(config, crafting, faith, leaders, players, false, new Random(3));
        assertEquals(players, game.getPlayerNames());
    }

    @Test
    public void checkMarket() throws ParserException {
        GameModel game = ServerBuilder.buildModel(config, crafting, faith, leaders, players, false, new Random(3));
        assertDoesNotThrow(game::getMarket);
    }

    @Test
    public void checkShop() throws ParserException {
        GameModel game = ServerBuilder.buildModel(config, crafting, faith, leaders, players, false, new Random(3));
        assertDoesNotThrow(game::getShop);
    }

    @Test
    public void checkFaithPath() throws ParserException {
        GameModel game = ServerBuilder.buildModel(config, crafting, faith, leaders, players, false, new Random(3));
        assertDoesNotThrow(game::getFaithPath);
    }

    @Test
    public void checkLeaderCards() throws ParserException {
        GameModel game = ServerBuilder.buildModel(config, crafting, faith, leaders, players, false, new Random(3));
        assertDoesNotThrow(game::getLeaderCards);
    }

    @Test
    public void validGetById() throws ParserException {
        GameModel game = ServerBuilder.buildModel(config, crafting, faith, leaders, players, false, new Random(3));
        assertEquals(players.get(0), game.getPlayerById("John").getUsername());
    }

    @Test
    public void noSuchPlayerGetById() throws ParserException {
        GameModel game = ServerBuilder.buildModel(config, crafting, faith, leaders, players, false, new Random(3));
        assertThrows(NoSuchElementException.class, ()->game.getPlayerById("asfd"));
    }

    @Test
    public void nullPlayerGetById() throws ParserException {
        GameModel game = ServerBuilder.buildModel(config, crafting, faith, leaders, players, false, new Random(3));
        assertThrows(NullPointerException.class, ()->game.getPlayerById(null));
    }
}
