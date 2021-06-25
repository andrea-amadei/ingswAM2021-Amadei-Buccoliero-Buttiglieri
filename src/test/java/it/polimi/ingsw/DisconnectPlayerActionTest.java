package it.polimi.ingsw;
import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.actions.DisconnectPlayerAction;
import it.polimi.ingsw.model.actions.ReconnectPlayerAction;
import it.polimi.ingsw.model.actions.SelectPlayAction;
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
public class DisconnectPlayerActionTest {

    private GameContext gameContext;

    @BeforeEach
    public void init() throws ParserException, IOException {
        List<String> playersNames = Arrays.asList("Ernestino", "Casimiro", "Bartolo");

        String configJSON = Files.readString(ResourceLoader.getPathFromResource("cfg/config.json"));
        String craftingJSON = Files.readString(ResourceLoader.getPathFromResource("cfg/crafting.json"));
        String faithJSON = Files.readString(ResourceLoader.getPathFromResource("cfg/faith.json"));
        String leadersJSON = Files.readString(ResourceLoader.getPathFromResource("cfg/leaders.json"));

        GameModel model = ServerBuilder.buildModel(configJSON, craftingJSON, faithJSON, leadersJSON, playersNames, false,
                new Random(5));
        gameContext = new GameContext(model, false);

        gameContext.setCurrentPlayer(model.getPlayerById("Ernestino"));
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
        DisconnectPlayerAction action = new DisconnectPlayerAction("Casimiro");

        assertDoesNotThrow(()-> action.acceptHandler(handler));
    }

    //Method "acceptHandler" throws NullPointerException if parameter "handler" is null
    @Test
    public void exceptionOnHandler(){
        DisconnectPlayerAction action = new DisconnectPlayerAction("Casimiro");

        assertThrows(NullPointerException.class, ()-> action.acceptHandler(null));
    }

    //If the current player has resources in their marketBasket, it will be emptied on disconnection
    @Test
    public void disconnectionEmptiesMarketBasket(){
        assertDoesNotThrow(()-> gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getStorage()
        .getMarketBasket().addResources(ResourceTypeSingleton.getInstance().getStoneResource(), 2));

        DisconnectPlayerAction action = new DisconnectPlayerAction("Ernestino");
        assertDoesNotThrow(()->action.execute(gameContext));

        assertEquals(0, gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getStorage()
        .getMarketBasket().totalAmountOfResources());
    }

    //method "getSender" returns the correct value
    @Test
    public void correctGetSender(){
        DisconnectPlayerAction action = new DisconnectPlayerAction("Ernestino");
        assertEquals("AI", action.getSender());
    }

    //Method "checkFormat" does not throw exceptions
    @Test
    public void checkFormatTest(){
        DisconnectPlayerAction action = new DisconnectPlayerAction("Ernestino");

        assertDoesNotThrow(action::checkFormat);
    }

}
