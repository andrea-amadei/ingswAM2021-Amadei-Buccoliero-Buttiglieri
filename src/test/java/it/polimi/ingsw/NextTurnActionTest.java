package it.polimi.ingsw;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.actions.NextTurnAction;
import it.polimi.ingsw.model.actions.SelectPlayAction;
import it.polimi.ingsw.model.actions.StartGameAction;
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
public class NextTurnActionTest {

    private GameContext gameContext;

    @BeforeEach
    public void init() throws ParserException, IOException {
        List<String> playersNames = Arrays.asList("Ernestino", "Apollonio", "Gesualdo");

        String configJSON = Files.readString(ResourceLoader.getPathFromResource("cfg/config.json"));
        String craftingJSON = Files.readString(ResourceLoader.getPathFromResource("cfg/crafting.json"));
        String faithJSON = Files.readString(ResourceLoader.getPathFromResource("cfg/faith.json"));
        String leadersJSON = Files.readString(ResourceLoader.getPathFromResource("cfg/leaders.json"));

        GameModel model = ServerBuilder.buildModel(configJSON, craftingJSON, faithJSON, leadersJSON, playersNames, false,
                new Random(5));
        gameContext = new GameContext(model, false);

        gameContext.setCurrentPlayer(model.getPlayerById("Ernestino"));
    }

    //Method "acceptHandler" throws NullPointerException if parameter "handler" is null
    @Test
    public void exceptionOnHandler(){
        NextTurnAction action = new NextTurnAction();

        assertThrows(NullPointerException.class, ()-> action.acceptHandler(null));
    }

    //method "execute" throws NullPointerException if parameter "gameContext" is null
    @Test
    public void nullGameContext(){
        NextTurnAction action = new NextTurnAction();

        assertThrows(NullPointerException.class, ()-> action.execute(null));
    }

    //method "getSender" returns the correct value
    @Test
    public void correctGetSender(){
        NextTurnAction action = new NextTurnAction();

        assertEquals("AI", action.getSender());
    }

    //Method "checkFormat" does not throw exceptions
    @Test
    public void checkFormatTest(){
        NextTurnAction action = new NextTurnAction();

        assertDoesNotThrow(action::checkFormat);
    }

}
