package it.polimi.ingsw;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.actions.DisconnectPlayerAction;
import it.polimi.ingsw.model.actions.ReconnectPlayerAction;
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
public class ReconnectPlayerActionTest {

    private GameContext gameContext;

    //4 total players, 1 player disconnected
    @BeforeEach
    public void init() throws ParserException, IOException {
        List<String> playersNames = Arrays.asList("Nonno", "Nonna", "Cugino", "Cugina");

        String configJSON = Files.readString(ResourceLoader.getPathFromResource("cfg/config.json"));
        String craftingJSON = Files.readString(ResourceLoader.getPathFromResource("cfg/crafting.json"));
        String faithJSON = Files.readString(ResourceLoader.getPathFromResource("cfg/faith.json"));
        String leadersJSON = Files.readString(ResourceLoader.getPathFromResource("cfg/leaders.json"));

        GameModel model = ServerBuilder.buildModel(configJSON, craftingJSON, faithJSON, leadersJSON, playersNames, false,
                new Random(5));
        gameContext = new GameContext(model, false);

        gameContext.setCurrentPlayer(model.getPlayerById("Nonno"));

        //Cugino is disconnected
        gameContext.getGameModel().getPlayerById("Cugino").setConnected(false);
    }

    //Formerly disconnected player has their status changed to "Connected" after ReconnectPlayerAction
    @Test
    public void correctExecution(){
        List<Message> messages;
        ReconnectPlayerAction action = new ReconnectPlayerAction("Cugino");
        try {
            messages = action.execute(gameContext);
        } catch (IllegalActionException e) {
            throw new RuntimeException();
        }

        assertTrue(gameContext.getGameModel().getPlayerById("Cugino").isConnected());
        assertTrue(messages.size() > 0);
    }

    //Constructor method of ReconnectPlayerAction functions correctly. All getters work.
    @Test
    public void correctCreation(){
        ReconnectPlayerAction action = new ReconnectPlayerAction("Cugino");

        assertEquals("AI", action.getSender());
        assertEquals("Cugino", action.getTarget());
    }

    //Constructor method throws NullPointerException if the parameter "target" is null
    @Test
    public void nullTarget(){
        assertThrows(NullPointerException.class, ()-> new ReconnectPlayerAction(null));
    }

    //Method "acceptHandler" does not throw any exception if it receives an Handler
    @Test
    public void acceptHandlerTest(){
        ActionHandler handler = new ActionHandler() {
            @Override
            public List<Message> handleAction(DisconnectPlayerAction disconnectPlayerAction) {
                return null;
            }

            @Override
            public List<Message> handleAction(ReconnectPlayerAction reconnectPlayerAction){
                return null;
            }
        };
        ReconnectPlayerAction action = new ReconnectPlayerAction("Cugino");

        assertDoesNotThrow(()-> action.acceptHandler(handler));
    }

    //Method "acceptHandler" throws NullPointerException if parameter "handler" is null
    @Test
    public void exceptionOnHandler(){
        ReconnectPlayerAction action = new ReconnectPlayerAction("Cugino");

        assertThrows(NullPointerException.class, ()-> action.acceptHandler(null));
    }


}
