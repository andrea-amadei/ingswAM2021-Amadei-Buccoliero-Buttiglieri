package it.polimi.ingsw;
import it.polimi.ingsw.common.ActionQueue;
import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.common.exceptions.ParserException;
import it.polimi.ingsw.server.model.faithpath.FaithPath;
import it.polimi.ingsw.server.model.global.GameModel;
import it.polimi.ingsw.server.model.global.Player;
import it.polimi.ingsw.server.model.actions.DisconnectPlayerAction;
import it.polimi.ingsw.server.model.actions.EndGameAction;
import it.polimi.ingsw.server.model.actions.PopeCheckAction;
import it.polimi.ingsw.server.model.actions.ReconnectPlayerAction;
import it.polimi.ingsw.server.model.fsm.GameContext;
import it.polimi.ingsw.server.model.fsm.State;
import it.polimi.ingsw.server.model.fsm.StateMachine;
import it.polimi.ingsw.server.model.fsm.states.BasketCollectState;
import it.polimi.ingsw.server.model.fsm.states.EndTurnState;
import it.polimi.ingsw.server.model.fsm.states.MenuState;
import it.polimi.ingsw.server.model.holder.FaithHolder;
import it.polimi.ingsw.server.ServerBuilder;
import it.polimi.ingsw.common.utils.ResourceLoader;
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
    private State currentState;

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

    //testing disconnection on state "BasketCollectState", since it does not override.
    //current player disconnects
    @Test
    public void disconnectOnBasketCollectState(){
        currentState = new BasketCollectState(gameContext);
        try {
            currentState.handleAction(new DisconnectPlayerAction("Alice"));
        } catch (FSMTransitionFailedException e) {
            throw new RuntimeException();
        }

        assertFalse(gameContext.getGameModel().getPlayerById("Alice").isConnected());
        assertTrue(currentState.getNextState() instanceof EndTurnState);
    }

    //player who is not the current player disconnects
    @Test
    public void disconnectOnBasketCollectState1(){
        currentState = new BasketCollectState(gameContext);
        try {
            currentState.handleAction(new DisconnectPlayerAction("Bob"));
        } catch (FSMTransitionFailedException e) {
            throw new RuntimeException();
        }

        assertFalse(gameContext.getGameModel().getPlayerById("Bob").isConnected());
    }

    //testing reconnection on state "BasketCollectState", since it does not override.
    @Test
    public void reconnectionOnBasketCollectState(){
        currentState = new BasketCollectState(gameContext);
        gameContext.getGameModel().getPlayerById("Bob").setConnected(false);

        try {
            currentState.handleAction(new ReconnectPlayerAction("Bob"));
        } catch (FSMTransitionFailedException e) {
            throw new RuntimeException();
        }

        assertTrue(gameContext.getGameModel().getPlayerById("Bob").isConnected());
    }

    //cannot create a State with null parameter "gameContext", demonstrated with BasketCollectState
    @Test
    public void nullGameContext(){
        assertThrows(NullPointerException.class, ()-> new BasketCollectState(null));
    }

    @Test
    public void nullSetListener(){
        currentState = new BasketCollectState(gameContext);

        assertThrows(NullPointerException.class, ()-> currentState.setListener(null));
    }

    @Test
    public void nullLaunchInterrupt(){
        currentState = new BasketCollectState(gameContext);

        assertThrows(NullPointerException.class, ()-> currentState.launchInterrupt(null, 1));
    }

    @Test
    public void nullEndGame(){
        currentState = new BasketCollectState(gameContext);

        assertThrows(NullPointerException.class, ()-> currentState.handleAction((EndGameAction) null));
    }

    @Test
    public void nullPopeCheck(){
        currentState = new BasketCollectState(gameContext);

        assertThrows(NullPointerException.class, ()-> currentState.handleAction((PopeCheckAction) null));
    }

    @Test
    public void endGameOnBasketCollectState(){
        List<Message> messages;
        currentState = new BasketCollectState(gameContext);

        try {
            messages = currentState.handleAction(new EndGameAction());
        } catch (FSMTransitionFailedException e) {
            throw new RuntimeException();
        }

        assertTrue(messages.size() > 0);
    }

}
