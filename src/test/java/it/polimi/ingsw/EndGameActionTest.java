package it.polimi.ingsw;
import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.exceptions.IllegalActionException;
import it.polimi.ingsw.common.exceptions.ParserException;
import it.polimi.ingsw.server.model.global.GameModel;
import it.polimi.ingsw.server.model.actions.DisconnectPlayerAction;
import it.polimi.ingsw.server.model.actions.EndGameAction;
import it.polimi.ingsw.server.model.actions.ReconnectPlayerAction;
import it.polimi.ingsw.server.model.actions.StartGameAction;
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
public class EndGameActionTest {

    private GameContext gameContext;

    @BeforeEach
    public void init() throws ParserException, IOException {
        List<String> playersNames = Arrays.asList("Ernestino", "Ingrid", "Filomena", "Lucertola");

        String configJSON = Files.readString(ResourceLoader.getPathFromResource("cfg/config.json"));
        String craftingJSON = Files.readString(ResourceLoader.getPathFromResource("cfg/crafting.json"));
        String faithJSON = Files.readString(ResourceLoader.getPathFromResource("cfg/faith.json"));
        String leadersJSON = Files.readString(ResourceLoader.getPathFromResource("cfg/leaders.json"));

        GameModel model = ServerBuilder.buildModel(configJSON, craftingJSON, faithJSON, leadersJSON, playersNames, false,
                new Random(5));
        gameContext = new GameContext(model, false);

        gameContext.setCurrentPlayer(model.getPlayerById("Filomena"));
    }

    //Method "execute", when called correctly, sets "isGameEnded" to true and returns messages
    @Test
    public void correctExecution(){
        List<Message> messages;
        EndGameAction action = new EndGameAction();

        try {
            messages = action.execute(gameContext);
        } catch (IllegalActionException e) {
            throw new RuntimeException();
        }

        assertTrue(gameContext.isGameEnded());
        assertTrue(messages.size() > 0);
    }

    //Method "execute" throws NullPointerException if parameter "gameContext" is null
    @Test
    public void nullGameContext(){
        EndGameAction action = new EndGameAction();

        assertThrows(NullPointerException.class, ()-> action.execute(null));
    }

    //Getters work
    @Test
    public void correctGetSender(){
        EndGameAction action = new EndGameAction();

        assertEquals("AI", action.getSender());
    }

    //Method "acceptHandler" does not throw any exception if it receives an Handler
    @Test
    public void acceptHandlerTest(){
        ActionHandler handler = new ActionHandler() {
            @Override
            public List<Message> handleAction(EndGameAction disconnectPlayerAction){
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
        EndGameAction action = new EndGameAction();

        assertDoesNotThrow(()-> action.acceptHandler(handler));
    }

    //Method "acceptHandler" throws NullPointerException if parameter "handler" is null
    @Test
    public void exceptionOnHandler(){
        EndGameAction action = new EndGameAction();

        assertThrows(NullPointerException.class, ()-> action.acceptHandler(null));
    }

    //Method "checkFormat" does not throw exceptions
    @Test
    public void checkFormatTest(){
        StartGameAction action = new StartGameAction();

        assertDoesNotThrow(action::checkFormat);
    }

}
