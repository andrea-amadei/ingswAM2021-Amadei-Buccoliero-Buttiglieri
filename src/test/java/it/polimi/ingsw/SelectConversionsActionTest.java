package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.gamematerials.MarbleColor;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.actions.BuyFromMarketAction;
import it.polimi.ingsw.model.actions.SelectConversionsAction;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.market.ConversionActuator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SelectConversionsActionTest {

    private GameContext gameContext;

    private final ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
    private final ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
    private final ResourceSingle shield = ResourceTypeSingleton.getInstance().getShieldResource();
    private final ResourceSingle stone = ResourceTypeSingleton.getInstance().getStoneResource();


    @BeforeEach
    public void init(){
        Player player1 = new Player("Paolo", 0);
        Player player2 = new Player("Genoveffa", 1);
        Player player3 = new Player("Gertrude", 2);

        GameModel model = new GameModel(Arrays.asList(player1, player2, player3), new Random(3));

        player2.getBoard().getConversionHolder().addConversionActuator(MarbleColor.WHITE,
                new ConversionActuator(Collections.singletonList(servant), 0));

        player2.getBoard().getConversionHolder().addConversionActuator(MarbleColor.WHITE,
                new ConversionActuator(Collections.singletonList(gold), 0));
        gameContext = new GameContext(model);
    }

    @Test
    public void pickWithMultipleConversions(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Genoveffa"));
        assertDoesNotThrow(()->new BuyFromMarketAction("Genoveffa", false, 2).execute(gameContext));
        assertDoesNotThrow(()->new SelectConversionsAction("Genoveffa", Arrays.asList(0,0,1)).execute(gameContext));
        Map<ResourceSingle, Integer> expectedBasket = new HashMap<>(){{
            put(servant, 1);
            put(gold, 1);
        }};
        assertEquals(expectedBasket,
                gameContext.getGameModel().getPlayerById("Genoveffa").getBoard().getStorage()
                        .getMarketBasket().getAllResources());
    }

    @Test
    public void pickBaseConversions(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Paolo"));
        assertDoesNotThrow(()->new BuyFromMarketAction("Paolo", false, 3).execute(gameContext));
        assertDoesNotThrow(()->new SelectConversionsAction("Paolo", Arrays.asList(0,0,0)).execute(gameContext));
        Map<ResourceSingle, Integer> expectedBasket = new HashMap<>(){{
            put(gold, 1);
            put(stone, 1);
        }};

        assertEquals(expectedBasket,
                gameContext.getGameModel().getPlayerById("Paolo").getBoard().getStorage()
                        .getMarketBasket().getAllResources());
    }

    @Test
    public void pickNonValidConversions(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Paolo"));
        assertDoesNotThrow(()->new BuyFromMarketAction("Paolo", false, 3).execute(gameContext));
        assertThrows(IllegalActionException.class, ()->new SelectConversionsAction("Paolo", Arrays.asList(0,1,0)).execute(gameContext));
    }

    @Test
    public void noSuchPlayerPickConversion(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Paolo"));
        assertThrows(IllegalActionException.class, ()->new SelectConversionsAction("Boh", Arrays.asList(0,1,0)).execute(gameContext));
    }

    @Test
    public void negativePickConversion(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Paolo"));
        assertDoesNotThrow(()->new BuyFromMarketAction("Paolo", false, 3).execute(gameContext));
        assertThrows(IllegalArgumentException.class, ()->new SelectConversionsAction("Paolo", Arrays.asList(0,-1,0)).execute(gameContext));
    }

    @Test
    public void wrongSizeOfConversionChoices(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Genoveffa"));
        assertDoesNotThrow(()->new BuyFromMarketAction("Genoveffa", false, 3).execute(gameContext));
        assertThrows(IllegalActionException.class, ()->new SelectConversionsAction("Genoveffa", Arrays.asList(0,0)).execute(gameContext));
    }

    @Test
    public void tooHighConversionChoice(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Genoveffa"));
        assertDoesNotThrow(()->new BuyFromMarketAction("Genoveffa", false, 3).execute(gameContext));
        assertThrows(IllegalActionException.class, ()->new SelectConversionsAction("Genoveffa", Arrays.asList(0,2,0)).execute(gameContext));
    }

    @Test
    public void nullPlayerOrChoicesCreation(){
        assertThrows(NullPointerException.class, ()->new SelectConversionsAction(null, Arrays.asList(0,2,0)));
        assertThrows(NullPointerException.class, ()->new SelectConversionsAction("Paolo", null));
    }

    @Test
    public void nullGameContextCreation(){
        assertThrows(NullPointerException.class, ()->new SelectConversionsAction("Genoveffa", Arrays.asList(0,2,0)).execute(null));
    }

}
