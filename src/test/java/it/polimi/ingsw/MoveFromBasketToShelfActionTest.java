package it.polimi.ingsw;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.actions.Action;
import it.polimi.ingsw.model.actions.MoveFromBasketToShelfAction;
import it.polimi.ingsw.model.fsm.GameContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MoveFromBasketToShelfActionTest {

    private GameContext gameContext;
    private final ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
    private final ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();

    @BeforeEach
    public void init(){

        Player player1 = new Player("Ernestino", 0);
        Player player2 = new Player("Pollo", 1);
        GameModel model = new GameModel(Arrays.asList(player1, player2));
        gameContext = new GameContext(model);

        assertDoesNotThrow(()->player2.getBoard().getStorage().getCupboard().getShelfById("MiddleShelf").addResources(servant, 1));
        assertDoesNotThrow(()->player2.getBoard().getStorage().getCupboard().getShelfById("BottomShelf").addResources(gold, 3));
        assertDoesNotThrow(()->player2.getBoard().getStorage().getMarketBasket().addResources(gold, 1));
        assertDoesNotThrow(()->player1.getBoard().getStorage().getMarketBasket().addResources(servant, 1));

        //lultima mensola di pollo è piena, la mezza ha un servo
        //in mano ha un oro

    }

    @Test
    public void executeTest(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Ernestino"));
        Action action = new MoveFromBasketToShelfAction("Ernestino", servant, 1, "BottomShelf");
        List<Message> messages;

        try{
            messages = action.execute(gameContext);
        }catch(IllegalActionException e){
            throw new RuntimeException();
        }

        assertEquals(new HashMap<ResourceSingle, Integer>(){{put(servant, 1);}}
                ,gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getStorage().getCupboard()
                        .getShelfById("BottomShelf").getAllResources());
        assertEquals(0, gameContext.getGameModel().getPlayerById("Ernestino").getBoard().
                getStorage().getMarketBasket().totalAmountOfResources());
    }

    @Test
    public void nullPlayer(){
        assertThrows(NullPointerException.class, ()-> new MoveFromBasketToShelfAction(null, gold, 1,
                "BottomShelf"));
    }

    @Test
    public void nullResourceToMove(){
        assertThrows(NullPointerException.class, ()-> new MoveFromBasketToShelfAction("Ernestino", null,
                1, "TopShelf"));
    }

    @Test
    public void nullShelfID(){
        assertThrows(NullPointerException.class, ()-> new MoveFromBasketToShelfAction("Ernestino", gold, 1,
                null));
    }

    @Test
    public void invalidAmount(){
        assertThrows(IllegalArgumentException.class, ()-> new MoveFromBasketToShelfAction("Ernestino", gold, 0,
                "BottomShelf"));
        assertThrows(IllegalArgumentException.class, ()-> new MoveFromBasketToShelfAction("Ernestino", gold, -2,
                "BottomShelf"));
    }

    @Test
    public void invalidPlayer(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Ernestino"));
        Action action = new MoveFromBasketToShelfAction("Ernestina", gold, 1, "MiddleShelf");
        assertThrows(IllegalActionException.class, ()-> action.execute(gameContext));
    }

    @Test
    public void invalidShelf(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Ernestino"));
        Action action = new MoveFromBasketToShelfAction("Ernestino", gold, 1, "Ehilà");
        assertThrows(IllegalActionException.class, ()-> action.execute(gameContext));
    }

    @Test
    public void nullGameContext(){
        Action action = new MoveFromBasketToShelfAction("Ernestino", gold, 1, "BottomShelf");
        assertThrows(NullPointerException.class, ()-> action.execute(null));
    }

    @Test
    public void movingAResourceToAFullShelf(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Pollo"));
        Action action = new MoveFromBasketToShelfAction("Pollo", gold, 1, "BottomShelf");
        assertThrows(IllegalActionException.class, ()-> action.execute(gameContext));
    }

    @Test
    public void movingAResourceToAnInvalidShelf(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Pollo"));
        Action action = new MoveFromBasketToShelfAction("Pollo", gold, 1, "MiddleShelf");
        assertThrows(IllegalActionException.class, ()-> action.execute(gameContext));
    }

    @Test
    public void movingAResourceNotPresent(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Pollo"));
        Action action = new MoveFromBasketToShelfAction("Pollo", servant, 1, "MiddleShelf");
        assertThrows(IllegalActionException.class, ()-> action.execute(gameContext));
    }

}
