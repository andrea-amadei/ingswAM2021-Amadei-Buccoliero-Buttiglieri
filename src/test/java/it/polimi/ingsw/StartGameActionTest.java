package it.polimi.ingsw;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.actions.DisconnectPlayerAction;
import it.polimi.ingsw.model.actions.EndGameAction;
import it.polimi.ingsw.model.actions.ReconnectPlayerAction;
import it.polimi.ingsw.model.actions.StartGameAction;
import it.polimi.ingsw.model.fsm.ActionHandler;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.server.ServerBuilder;
import it.polimi.ingsw.utils.ResourceLoader;
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
public class StartGameActionTest {

    private GameContext gameContext;

    @BeforeEach
    public void init() throws ParserException, IOException {
        List<String> playersNames = Arrays.asList("Pollo", "Petunia");

        String configJSON = Files.readString(ResourceLoader.getPathFromResource("cfg/config.json"));
        String craftingJSON = Files.readString(ResourceLoader.getPathFromResource("cfg/crafting.json"));
        String faithJSON = Files.readString(ResourceLoader.getPathFromResource("cfg/faith.json"));
        String leadersJSON = Files.readString(ResourceLoader.getPathFromResource("cfg/leaders.json"));

        GameModel model = ServerBuilder.buildModel(configJSON, craftingJSON, faithJSON, leadersJSON, playersNames, false,
                new Random(5));
        gameContext = new GameContext(model, false);

    }

    //method "execute" does not throw exceptions
    @Test
    public void correctExecution(){
        StartGameAction action = new StartGameAction();

        assertDoesNotThrow(()-> action.execute(gameContext));
    }

    //method "getSender" returns correct value
    @Test
    public void correctGetSender(){
        StartGameAction action = new StartGameAction();

        assertEquals("AI", action.getSender());
    }

    //method "acceptHandler" throws NullPointerException if parameter "actionHandler" is null
    @Test
    public void nullActionHandler(){
        StartGameAction action = new StartGameAction();

        assertThrows(NullPointerException.class, ()-> action.acceptHandler(null));
    }

    //Method "acceptHandler" does not throw any exception if it receives an Handler
    @Test
    public void acceptHandlerTest(){
        ActionHandler handler = new ActionHandler() {
            @Override
            public List<Message> handleAction(StartGameAction startGameAction) {
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
        StartGameAction action = new StartGameAction();

        assertDoesNotThrow(()-> action.acceptHandler(handler));
    }

    //Method "checkFormat" does not throw exceptions
    @Test
    public void checkFormatTest(){
        EndGameAction action = new EndGameAction();

        assertDoesNotThrow(action::checkFormat);
    }

}
