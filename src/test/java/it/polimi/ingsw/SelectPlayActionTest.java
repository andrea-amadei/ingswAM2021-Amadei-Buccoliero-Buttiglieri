package it.polimi.ingsw;
import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.exceptions.IllegalActionException;
import it.polimi.ingsw.common.exceptions.ParserException;
import it.polimi.ingsw.server.model.global.GameModel;
import it.polimi.ingsw.server.model.actions.DisconnectPlayerAction;
import it.polimi.ingsw.server.model.actions.ReconnectPlayerAction;
import it.polimi.ingsw.server.model.actions.SelectPlayAction;
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
public class SelectPlayActionTest {

    private GameContext gameContext;

    @BeforeEach
    public void init() throws ParserException, IOException {
        List<String> playersNames = Arrays.asList("Ernestino", "Norma", "Brigitta");

        String configJSON = Files.readString(ResourceLoader.getPathFromResource("cfg/config.json"));
        String craftingJSON = Files.readString(ResourceLoader.getPathFromResource("cfg/crafting.json"));
        String faithJSON = Files.readString(ResourceLoader.getPathFromResource("cfg/faith.json"));
        String leadersJSON = Files.readString(ResourceLoader.getPathFromResource("cfg/leaders.json"));

        GameModel model = ServerBuilder.buildModel(configJSON, craftingJSON, faithJSON, leadersJSON, playersNames, false,
                new Random(5));
        gameContext = new GameContext(model, false);

        gameContext.setCurrentPlayer(model.getPlayerById("Norma"));
    }

    //Method "acceptHandler" does not throw any exception if it receives an Handler
    @Test
    public void acceptHandlerTest(){
        ActionHandler handler = new ActionHandler() {
            @Override
            public List<Message> handleAction(SelectPlayAction selectPlayAction) {
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
        SelectPlayAction action = new SelectPlayAction("Norma", SelectPlayAction.Play.MARKET);

        assertDoesNotThrow(()-> action.acceptHandler(handler));
    }

    //Method "acceptHandler" throws NullPointerException if parameter "handler" is null
    @Test
    public void exceptionOnHandler(){
        SelectPlayAction action = new SelectPlayAction("Norma", SelectPlayAction.Play.MARKET);

        assertThrows(NullPointerException.class, ()-> action.acceptHandler(null));
    }

    //method "execute" throws NullPointerException if parameter "gameContext" is null
    @Test
    public void nullGameContext(){
        SelectPlayAction action = new SelectPlayAction("Norma", SelectPlayAction.Play.MARKET);

        assertThrows(NullPointerException.class, ()-> action.execute(null));
    }

    //method "execute" throws IllegalActionException if the player of the SelectPlayAction is not the current player
    @Test
    public void wrongPlayer(){
        SelectPlayAction action = new SelectPlayAction("Brigitta", SelectPlayAction.Play.MARKET);

        assertThrows(IllegalActionException.class, ()-> action.execute(gameContext));
    }

    //constructor method of SelectPlayAction throws NullPointerException if parameter "player" is null
    @Test
    public void nullPlayer(){
        assertThrows(NullPointerException.class, ()-> new SelectPlayAction(null, SelectPlayAction.Play.CRAFTING));
    }

    //constructor method of SelectPlayAction throws NullPointerException if parameter "play" is null
    @Test
    public void nullPlay(){
        assertThrows(NullPointerException.class, ()-> new SelectPlayAction("Norma", null));
    }

}
