package it.polimi.ingsw;
import it.polimi.ingsw.common.ActionQueue;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.model.FaithPath;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.fsm.StateMachine;
import it.polimi.ingsw.model.fsm.states.MenuState;
import it.polimi.ingsw.model.holder.FaithHolder;
import it.polimi.ingsw.server.ServerBuilder;
import it.polimi.ingsw.utils.ResourceLoader;
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
    public void init() throws ParserException {
        //set the gameContext with the DummyBuilder and Menu State as the initial state.
        //player1 is the current player
        String config = ResourceLoader.loadFile("cfg/config.json");
        String crafting = ResourceLoader.loadFile("cfg/crafting.json");
        String faith = ResourceLoader.loadFile("cfg/faith.json");
        String leaders = ResourceLoader.loadFile("cfg/leaders.json");

        List<String> usernames = Arrays.asList("Alice", "Bob", "Carlo");

        GameModel model  = ServerBuilder.buildModel(config, crafting, faith, leaders, usernames, false, new Random(3));

        player1 = model.getPlayerById("Alice");
        player2 = model.getPlayerById("Bob");
        player3 = model.getPlayerById("Carlo");

        actionQueue = new ActionQueue();
        gameContext = new GameContext(model, false);

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

        assertEquals(29, player1.getPoints());

    }
}
