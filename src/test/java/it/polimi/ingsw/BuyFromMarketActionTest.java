package it.polimi.ingsw;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.gamematerials.MarbleColor;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.GameParameters;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.actions.BuyFromMarketAction;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.market.ConversionActuator;
import it.polimi.ingsw.model.market.Marble;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BuyFromMarketActionTest {

    private GameContext gameContext;

    private final ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
    private final ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
    private final ResourceSingle shield = ResourceTypeSingleton.getInstance().getShieldResource();


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
    public void validCreation(){
        assertDoesNotThrow(()->new BuyFromMarketAction("test", true, 1));
    }

    @Test
    public void nullPlayerParam(){
        assertThrows(NullPointerException.class, ()->new BuyFromMarketAction(null, true, 1));
    }

    @Test
    public void outOfBoundIndexException(){
        assertThrows(IndexOutOfBoundsException.class, ()->new BuyFromMarketAction("test", true, 14));
        assertThrows(IndexOutOfBoundsException.class, ()->new BuyFromMarketAction("test", true, -1));
    }

    @Test
    public void pickRowWithoutConversionPower(){
        BuyFromMarketAction action = new BuyFromMarketAction("Paolo", false, 2);
        List<Marble> expectedMarbles = new ArrayList<>();

        for(int i = 0; i < GameParameters.MARKET_ROWS; i++)
            expectedMarbles.add(gameContext.getGameModel().getMarket().getMarble(i, 2));
        Collections.reverse(expectedMarbles);
        List<Message> messages;
        try{
            messages = action.execute(gameContext);
        }catch(IllegalActionException e){
            throw new RuntimeException();
        }
        assertEquals(expectedMarbles, gameContext.getGameModel().getMarket().getSelectedMarbles());
        assertEquals(2, messages.size());
    }

    @Test
    public void pickRowWithWhiteConversionPower(){

        BuyFromMarketAction action = new BuyFromMarketAction("Genoveffa", false, 2);
        List<Marble> expectedMarbles = new ArrayList<>();

        for(int i = 0; i < GameParameters.MARKET_ROWS; i++)
            expectedMarbles.add(gameContext.getGameModel().getMarket().getMarble(i, 2));
        Collections.reverse(expectedMarbles);
        List<Message> messages;
        try{
            messages = action.execute(gameContext);
        }catch(IllegalActionException e){
            throw new RuntimeException();
        }
        assertEquals(expectedMarbles, gameContext.getGameModel().getMarket().getSelectedMarbles());
        assertEquals(2, messages.size());
    }

    @Test
    public void pickColWithWhiteConversionPower(){
        BuyFromMarketAction action = new BuyFromMarketAction("Genoveffa", true, 2);
        List<Marble> expectedMarbles = new ArrayList<>();

        for(int i = 0; i < GameParameters.MARKET_COLUMNS; i++)
            expectedMarbles.add(gameContext.getGameModel().getMarket().getMarble(2, i));
        Collections.reverse(expectedMarbles);
        List<Message> messages;
        try{
            messages = action.execute(gameContext);
        }catch(IllegalActionException e){
            throw new RuntimeException();
        }
        assertEquals(expectedMarbles, gameContext.getGameModel().getMarket().getSelectedMarbles());
        assertEquals(2, messages.size());
    }

    @Test
    public void noSuchPlayer(){
        BuyFromMarketAction action = new BuyFromMarketAction("John", false, 2);
        assertThrows(IllegalActionException.class, ()->action.execute(gameContext));
    }

    @Test
    public void nullContext(){
        BuyFromMarketAction action = new BuyFromMarketAction("John", false, 2);
        assertThrows(NullPointerException.class, ()->action.execute(null));
    }

}
