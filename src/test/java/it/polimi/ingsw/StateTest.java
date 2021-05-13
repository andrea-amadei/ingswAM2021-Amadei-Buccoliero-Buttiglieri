package it.polimi.ingsw;
import it.polimi.ingsw.common.ActionQueue;
import it.polimi.ingsw.model.FaithPath;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.fsm.StateMachine;
import it.polimi.ingsw.model.fsm.states.MenuState;
import it.polimi.ingsw.model.holder.FaithHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StateTest {

    private GameContext gameContext;
    private StateMachine stateMachine;
    private Player player1;
    private Player player2;
    private Player player3;
    private ActionQueue actionQueue;

    @BeforeEach
    public void init(){
        //set the gameContext with the DummyBuilder and Menu State as the initial state.
        //player1 is the current player
        player1 = new Player("Alice", 0);
        player2 = new Player("Bob", 1);
        player3 = new Player("Carlo", 2);

        GameModel model = new GameModel(Arrays.asList(player1, player2, player3));
        actionQueue = new ActionQueue();
        gameContext = new GameContext(model);

        stateMachine = new StateMachine(actionQueue, gameContext, new MenuState(gameContext));
        model.getFaithPath().setListener(stateMachine);

        gameContext.setCurrentPlayer(player1);
    }

    @Test
    public void currentPlayerFinishesTheFaithTrack(){
        FaithPath fp = gameContext.getGameModel().getFaithPath();
        fp.executeMovement(100, player1);
        assertFalse(actionQueue.isEmpty());
        assertDoesNotThrow(()->stateMachine.executeAction(actionQueue.pop()));

        assertEquals(FaithHolder.CheckpointStatus.ACTIVE, player1.getBoard().getFaithHolder().getPopeCardStatus(0));
        assertEquals(FaithHolder.CheckpointStatus.ACTIVE, player1.getBoard().getFaithHolder().getPopeCardStatus(1));
        assertEquals(FaithHolder.CheckpointStatus.INACTIVE, player2.getBoard().getFaithHolder().getPopeCardStatus(0));
        assertEquals(FaithHolder.CheckpointStatus.INACTIVE, player2.getBoard().getFaithHolder().getPopeCardStatus(1));
        assertEquals(FaithHolder.CheckpointStatus.INACTIVE, player3.getBoard().getFaithHolder().getPopeCardStatus(0));
        assertEquals(FaithHolder.CheckpointStatus.INACTIVE, player3.getBoard().getFaithHolder().getPopeCardStatus(1));

        assertEquals(57, player1.getPoints());

    }
}
