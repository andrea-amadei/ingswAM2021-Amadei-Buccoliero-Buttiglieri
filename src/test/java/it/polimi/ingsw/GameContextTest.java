package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.CountdownException;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.fsm.GameContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GameContextTest {
    private GameModel model;

    @BeforeAll
    public void init(){
        Player p1 = new Player("p1", 0);
        Player p2 = new Player("p2", 1);

        model = new GameModel(Arrays.asList(p1, p2));
    }

    @Test
    public void createValidGameContext(){
        GameContext context = new GameContext(model);
        assertEquals(model, context.getGameModel());
        assertNull(context.getCurrentPlayer());
    }

    @Test
    public void nullModel(){
        assertThrows(NullPointerException.class, ()->new GameContext(null));
    }

    @Test
    public void validSetCurrentPlayer(){
        GameContext context = new GameContext(model);
        context.setCurrentPlayer(model.getPlayers().get(0));
        assertEquals(model.getPlayers().get(0), context.getCurrentPlayer());
    }

    @Test
    @DisplayName("Player moved")
    public void playerMovedTest() {
        GameContext context = new GameContext(model);
        context.setCurrentPlayer(model.getPlayers().get(0));

        assertFalse(context.hasPlayerMoved());
        context.setPlayerMoved(true);
        assertTrue(context.hasPlayerMoved());

        context.setCurrentPlayer(model.getPlayers().get(1));
        assertFalse(context.hasPlayerMoved());
    }

    @Test
    @DisplayName("Turn countdown")
    public void countdownTest() {
        GameContext context = new GameContext(model);
        context.setCurrentPlayer(model.getPlayers().get(0));

        assertFalse(context.hasCountdownStarted());

        context.startCountdown(3);
        assertTrue(context.hasCountdownStarted());

        assertFalse(context.advanceCountdown());
        assertFalse(context.advanceCountdown());

        assertEquals(1, context.getTurnsLeft());

        assertFalse(context.hasCountdownEnded());

        assertTrue(context.advanceCountdown());
        assertTrue(context.hasCountdownEnded());
    }

    @Test
    @DisplayName("Invalid countdown start")
    public void invalidCountdownStartTest() {
        GameContext context = new GameContext(model);
        context.setCurrentPlayer(model.getPlayers().get(0));

        assertThrows(IllegalArgumentException.class, () -> context.startCountdown(-1));
    }

    @Test
    @DisplayName("Countdown not started yet")
    public void countdownNotStartedYet() {
        GameContext context = new GameContext(model);
        context.setCurrentPlayer(model.getPlayers().get(0));

        assertThrows(CountdownException.class, context::advanceCountdown);
        assertThrows(CountdownException.class, context::getTurnsLeft);
    }

    @Test
    @DisplayName("Countdown already started")
    public void countdownAlreadyStartedTest() {
        GameContext context = new GameContext(model);
        context.setCurrentPlayer(model.getPlayers().get(0));

        context.startCountdown(4);
        assertThrows(CountdownException.class, () -> context.startCountdown(2));
    }

    @Test
    @DisplayName("Countdown already ended")
    public void countdownAlreadyEndedTest() {
        GameContext context = new GameContext(model);
        context.setCurrentPlayer(model.getPlayers().get(0));

        context.startCountdown(1);
        context.advanceCountdown();

        assertThrows(CountdownException.class, context::advanceCountdown);
        assertThrows(CountdownException.class, context::getTurnsLeft);
    }

    @Test
    public void nullPlayerSetCurrentPlayer(){
        GameContext context = new GameContext(model);
        assertThrows(NullPointerException.class, ()->context.setCurrentPlayer(null));
    }

    @Test
    public void nonPresentInGameSetCurrentPlayer(){
        GameContext context = new GameContext(model);
        assertThrows(IllegalArgumentException.class, ()->context.setCurrentPlayer(new Player("p1", 0)));
    }
}
