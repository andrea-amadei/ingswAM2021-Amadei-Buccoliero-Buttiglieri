package it.polimi.ingsw;
import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.common.exceptions.ParserException;
import it.polimi.ingsw.server.model.global.GameModel;
import it.polimi.ingsw.server.model.actions.StartGameAction;
import it.polimi.ingsw.server.model.fsm.GameContext;
import it.polimi.ingsw.server.model.fsm.State;
import it.polimi.ingsw.server.model.fsm.states.PreliminaryPickState;
import it.polimi.ingsw.server.model.fsm.states.SetupState;
import it.polimi.ingsw.server.ServerBuilder;
import it.polimi.ingsw.common.utils.ResourceLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SetupStateTest {

    private State currentState;

    @BeforeEach
    public void init() throws ParserException {

        List<String> usernames = Arrays.asList("Ernestino", "Geonna", "Alrigo");
        String config = ResourceLoader.loadFile("cfg/config.json");
        String crafting = ResourceLoader.loadFile("cfg/crafting.json");
        String faith = ResourceLoader.loadFile("cfg/faith.json");
        String leaders = ResourceLoader.loadFile("cfg/leaders.json");

        GameModel model = ServerBuilder.buildModel(config, crafting, faith, leaders, usernames, false, new Random(3));

        GameContext gameContext = new GameContext(model, false);
        gameContext.setCurrentPlayer(model.getPlayerById("Ernestino"));
        currentState = new SetupState(gameContext);

    }

    //method "handleAction" correctly sets next state as "PreliminaryPickState" and returns an empty ArrayList
    @Test
    public void handleActionTest(){
        List<Message> messages;

        try {
            messages = currentState.handleAction(new StartGameAction());
        } catch (FSMTransitionFailedException e) {
            throw new RuntimeException();
        }

        assertEquals(messages.size(), 0);
        assertTrue(currentState.getNextState() instanceof PreliminaryPickState);
    }

    //method "onEntry" sends messages and does not throw any exception
    @Test
    public void onEntryTest(){
        List<Message> messages;
        messages = currentState.onEntry();

        assertTrue(messages.size() > 0);
    }

}
