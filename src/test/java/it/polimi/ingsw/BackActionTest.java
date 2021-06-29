package it.polimi.ingsw;
import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.exceptions.IllegalActionException;
import it.polimi.ingsw.common.exceptions.ParserException;
import it.polimi.ingsw.server.model.global.GameModel;
import it.polimi.ingsw.server.model.actions.BackAction;
import it.polimi.ingsw.server.model.actions.DisconnectPlayerAction;
import it.polimi.ingsw.server.model.actions.ReconnectPlayerAction;
import it.polimi.ingsw.server.model.fsm.ActionHandler;
import it.polimi.ingsw.server.model.fsm.GameContext;
import it.polimi.ingsw.server.ServerBuilder;
import it.polimi.ingsw.common.utils.ResourceLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BackActionTest {

    private GameContext gameContext;

    @BeforeEach
    public void init() throws ParserException, IOException {
        List<String> playersNames = Arrays.asList("Ernestino", "Olmo");

        String configJSON = Files.readString(ResourceLoader.getPathFromResource("cfg/config.json"));
        String craftingJSON = Files.readString(ResourceLoader.getPathFromResource("cfg/crafting.json"));
        String faithJSON = Files.readString(ResourceLoader.getPathFromResource("cfg/faith.json"));
        String leadersJSON = Files.readString(ResourceLoader.getPathFromResource("cfg/leaders.json"));

        GameModel model = ServerBuilder.buildModel(configJSON, craftingJSON, faithJSON, leadersJSON, playersNames, false,
                new Random(5));
        gameContext = new GameContext(model, false);

        gameContext.setCurrentPlayer(model.getPlayerById("Olmo"));
    }


    //calling method "execute" with the a null pointer as a parameter for "gameContext" results in NullPointerException
    @Test
    public void nullGameContext(){
        BackAction action = new BackAction("Olmo");

        assertThrows(NullPointerException.class, ()-> action.execute(null));
    }

    //calling method "execute" with the wrong player as a parameter of the backAction results in IllegalActionException
    @Test
    public void wrongPlayer(){
        BackAction action = new BackAction("Ernestino");

        assertThrows(IllegalActionException.class, ()-> action.execute(gameContext));
    }

    //execute method returns an empty ArrayList, if called correctly
    @Test
    public void correctExecute(){
        List<Message> messages;
        BackAction action = new BackAction("Olmo");

        try {
            messages = action.execute(gameContext);
        } catch (IllegalActionException e) {
            throw new RuntimeException();
        }

        assertEquals(messages.size(), 0);
    }

    //method "getSender" returns the correct value
    @Test
    public void getSenderTest(){
        BackAction action = new BackAction("Olmo");

        assertEquals("Olmo", action.getSender());
    }

    //declaring a BackAction with a null pointer for parameter "player" results in a NullPointerException
    @Test
    public void nullPlayer(){
        assertThrows(NullPointerException.class, ()-> new BackAction(null));
    }

    //Method "acceptHandler" does not throw any exception if it receives an Handler
    @Test
    public void acceptHandlerTest(){
        ActionHandler handler = new ActionHandler() {
            @Override
            public List<Message> handleAction(BackAction backAction) {
                return null;
            }
            @Override
            public List<Message> handleAction(DisconnectPlayerAction disconnectPlayerAction) {
                return null;
            }

            @Override
            public List<Message> handleAction(ReconnectPlayerAction reconnectPlayerAction){
                return null;
            }
        };
        BackAction action = new BackAction("Olmo");
        assertDoesNotThrow(()-> action.acceptHandler(handler));
    }

    //Method "acceptHandler" throws NullPointerException if parameter "handler" is null
    @Test
    public void exceptionOnHandler(){
        BackAction action = new BackAction("Olmo");

        assertThrows(NullPointerException.class, ()-> action.acceptHandler(null));
    }
}
