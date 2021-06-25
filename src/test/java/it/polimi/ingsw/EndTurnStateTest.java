package it.polimi.ingsw;
import it.polimi.ingsw.common.ActionQueue;
import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.actions.*;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.fsm.State;
import it.polimi.ingsw.model.fsm.StateMachine;
import it.polimi.ingsw.model.fsm.states.EndTurnState;
import it.polimi.ingsw.server.ServerBuilder;
import it.polimi.ingsw.utils.ResourceLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EndTurnStateTest {

    private GameContext gameContext;
    private State currentState;
    private ActionQueue actionQueue;


    @BeforeEach
    public void init() throws ParserException {

        List<String> usernames = Arrays.asList("Ernestino", "Geonna", "Alrigo");
        String config = ResourceLoader.loadFile("cfg/config.json");
        String crafting = ResourceLoader.loadFile("cfg/crafting.json");
        String faith = ResourceLoader.loadFile("cfg/faith.json");
        String leaders = ResourceLoader.loadFile("cfg/leaders.json");

        GameModel model = ServerBuilder.buildModel(config, crafting, faith, leaders, usernames, false, new Random(3));
        actionQueue = new ActionQueue();

        gameContext = new GameContext(model, false);
        gameContext.setCurrentPlayer(model.getPlayerById("Ernestino"));
        currentState = new EndTurnState(gameContext);

        StateMachine stateMachine = new StateMachine(actionQueue, gameContext, new EndTurnState(gameContext));
        currentState.setListener(stateMachine);
    }

    //method "handleAction" throws NullPointerException" if parameter "endGameAction" is null
    @Test
   public void nullEndGameAction(){
        assertThrows(NullPointerException.class, ()->currentState.handleAction((EndGameAction) null));
   }

    //method "handleAction" throws NullPointerException" if parameter "lorenzoMoveAction" is null
    @Test
    public void nullLorenzoMoveAction(){
        assertThrows(NullPointerException.class, ()->currentState.handleAction((LorenzoMoveAction) null));
    }

    //method "handleAction" throws NullPointerException" if parameter "nextTurnAction" is null
    @Test
    public void nullNextTurnAction(){
        assertThrows(NullPointerException.class, ()->currentState.handleAction((NextTurnAction) null));
    }

    //method "handleAction" correctly sends messages when managing a "ReconnectPlayerAction"
    @Test
    public void reconnectPlayerActionSendsMessages(){
        gameContext.getGameModel().getPlayerById("Geonna").setConnected(false);
        List<Message> messages;
        try {
            messages = currentState.handleAction(new ReconnectPlayerAction("Geonna"));
        } catch (FSMTransitionFailedException e) {
            throw new RuntimeException();
        }

        assertTrue(messages.size() > 0);
    }

    //when a player reconnects, if the game was previously stalling, said player becomes the current player
    @Test
    public void gameStalls(){
        gameContext.getGameModel().getPlayerById("Geonna").setConnected(false);
        gameContext.getGameModel().getPlayerById("Ernestino").setConnected(false);
        gameContext.getGameModel().getPlayerById("Alrigo").setConnected(false);

        try {
            currentState.handleAction(new ReconnectPlayerAction("Geonna"));
        } catch (FSMTransitionFailedException e) {
            throw new RuntimeException();
        }

        assertEquals("Geonna", gameContext.getCurrentPlayer().getUsername());
    }

    //in normal conditions (no end triggered and at least one player connected) method "onEntry" correctly sends messages
    @Test
    public void onEntrySendsMessages(){
        List<Message> messages;
        messages = currentState.onEntry();

        assertTrue(messages.size() > 0);
    }

    //when end conditions are satisfied, method "onEntry" adds a "EndGameAction" to the Actionque
    @Test
    public void onEntryWhenEndTriggered(){
        gameContext.getGameModel().getPlayerById("Geonna").setConnected(false);
        gameContext.getGameModel().getPlayerById("Alrigo").setConnected(false);
        gameContext.startCountdown();

        currentState.onEntry();
        Action action;

        try {
            action = actionQueue.pop();
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }

        assertTrue(action instanceof EndGameAction);
    }

    //method "onEntry" returns empty ArrayList when all players era disconnected
    @Test
    public void everyoneDisconnected(){
        gameContext.getGameModel().getPlayerById("Geonna").setConnected(false);
        gameContext.getGameModel().getPlayerById("Alrigo").setConnected(false);
        gameContext.getGameModel().getPlayerById("Ernestino").setConnected(false);

        List<Message> messages;
        messages = currentState.onEntry();

        assertEquals(messages.size(), 0);
    }

}
