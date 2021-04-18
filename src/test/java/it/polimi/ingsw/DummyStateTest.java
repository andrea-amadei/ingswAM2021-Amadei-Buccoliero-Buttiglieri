package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.actions.Action;
import it.polimi.ingsw.model.actions.ActivateLeaderAction;
import it.polimi.ingsw.model.actions.BuyFromMarketAction;
import it.polimi.ingsw.model.fsm.DummyState;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.fsm.InterruptListener;
import it.polimi.ingsw.model.fsm.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DummyStateTest {

    private static class DummyListener implements InterruptListener {

        public Action toBeLaunched;
        @Override
        public void launchInterrupt(Action interrupt) {
            toBeLaunched = interrupt;
        }
    }
    private GameContext gameContext;

    @BeforeEach
    public void init(){
        Player player1 = new Player("Bob", 0);

        GameModel model = new GameModel(Collections.singletonList(player1));

        gameContext = new GameContext(model);
    }

    @Test
    public void nullContextCreation(){
        assertThrows(NullPointerException.class, () -> new DummyState(null));
    }

    @Test
    public void addListener(){
        State dummyState = new DummyState(gameContext);
        DummyListener listener = new DummyListener();
        dummyState.setListener(listener);
        Action interrupt = new BuyFromMarketAction("bob", true, 2);
        dummyState.launchInterrupt(interrupt);

        assertEquals(interrupt, listener.toBeLaunched);
    }

    @Test
    public void validCreation(){
        State dummyState = new DummyState(gameContext);
        assertNull(dummyState.getNextState());
    }

    @Test
    public void handleValidAction(){
        State dummyState = new DummyState(gameContext);
        ActivateLeaderAction action = new ActivateLeaderAction("Bob", 1);
        assertDoesNotThrow(()->dummyState.handleAction(action));
        assertNotNull(dummyState.getNextState());
    }

    @Test
    public void handleInvalidAction(){
        State dummyState = new DummyState(gameContext);
        BuyFromMarketAction action = new BuyFromMarketAction("bob", true, 2);
        assertThrows(FSMTransitionFailedException.class, ()->dummyState.handleAction(action));
        assertNull(dummyState.getNextState());
    }
}
