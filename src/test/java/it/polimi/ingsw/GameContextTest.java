package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.CountdownException;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.server.ServerBuilder;
import it.polimi.ingsw.utils.ResourceLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GameContextTest {
    private GameModel model;

    @BeforeAll
    public void init() throws ParserException {

        List<String> usernames = Arrays.asList("p1", "p2");
        String config = ResourceLoader.loadFile("cfg/config.json");
        String crafting = ResourceLoader.loadFile("cfg/crafting.json");
        String faith = ResourceLoader.loadFile("cfg/faith.json");
        String leaders = ResourceLoader.loadFile("cfg/leaders.json");
        model  = ServerBuilder.buildModel(config, crafting, faith, leaders, usernames, false, new Random(3));
    }

    @Test
    public void createValidGameContext(){
        GameContext context = new GameContext(model, false);
        assertEquals(model, context.getGameModel());
        assertNull(context.getCurrentPlayer());
    }

    @Test
    public void nullModel(){
        assertThrows(NullPointerException.class, ()->new GameContext(null));
    }

    @Test
    public void validSetCurrentPlayer(){
        GameContext context = new GameContext(model, false);
        context.setCurrentPlayer(model.getPlayers().get(0));
        assertEquals(model.getPlayers().get(0), context.getCurrentPlayer());
    }

    @Test
    @DisplayName("Player moved")
    public void playerMovedTest() {
        GameContext context = new GameContext(model, false);
        context.setCurrentPlayer(model.getPlayers().get(0));

        assertFalse(context.hasPlayerMoved());
        context.setPlayerMoved(true);
        assertTrue(context.hasPlayerMoved());

        context.setCurrentPlayer(model.getPlayers().get(1));
        assertFalse(context.hasPlayerMoved());
    }

    @Test
    public void nullPlayerSetCurrentPlayer(){
        GameContext context = new GameContext(model, false);
        assertThrows(NullPointerException.class, ()->context.setCurrentPlayer(null));
    }

}
