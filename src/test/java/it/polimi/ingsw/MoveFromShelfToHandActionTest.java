package it.polimi.ingsw;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.actions.Action;
import it.polimi.ingsw.model.actions.MoveFromShelfToHandAction;
import it.polimi.ingsw.model.fsm.GameContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MoveFromShelfToHandActionTest {

    private GameContext gameContext;
    private final ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
    private final ResourceSingle shield = ResourceTypeSingleton.getInstance().getShieldResource();

    @BeforeEach
    public void init(){
        Player player1 = new Player("Ernestino", 1);
        Player player2 = new Player("Cornelio", 2);
        GameModel model = new GameModel(Arrays.asList(player1, player2));
        gameContext = new GameContext(model);

        assertDoesNotThrow(()->player1.getBoard().getStorage().getCupboard().getShelfById("BottomShelf").addResources(servant, 3));
    }

    @Test
    public void executeTest(){
        Action action = new MoveFromShelfToHandAction("Ernestino", servant, 1, "BottomShelf");
        List<Message> messages;

        try{
            messages = action.execute(gameContext);
        }catch(IllegalActionException e){
            throw new RuntimeException();
        }

        assertEquals(new HashMap<ResourceSingle, Integer>(){{put(servant, 2);}}
                ,gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getStorage().getCupboard()
                        .getShelfById("BottomShelf").getAllResources());

        assertEquals("INFO: 1 of Servant have been moved from Ernestino's BottomShelf to their hand", messages.get(0).toString());
    }

    @Test
    public void nullPlayer(){
        assertThrows(NullPointerException.class, ()-> new MoveFromShelfToHandAction(null, servant, 2, "MiddleShelf"));
    }

    @Test
    public void nullResourceToMove(){
        assertThrows(NullPointerException.class, ()-> new MoveFromShelfToHandAction("Ernestino", null, 1,"BottomShelf"));
    }

    @Test
    public void nullShelfID(){
        assertThrows(NullPointerException.class, ()-> new MoveFromShelfToHandAction("Ernestino", shield, 1, null));
    }

    @Test
    public void invalidAmount(){
        assertThrows(IllegalArgumentException.class, ()-> new MoveFromShelfToHandAction("Ernestino", shield, 0, "MiddleShelf"));
        assertThrows(IllegalArgumentException.class, ()-> new MoveFromShelfToHandAction("Ernestino", servant, -6, "MiddleShelf"));
    }

    @Test
    public void invalidPlayer(){
        Action action = new MoveFromShelfToHandAction("Bottiglia", shield, 1, "BottomShelf");
        assertThrows(IllegalActionException.class, ()-> action.execute(gameContext));
    }

    @Test
    public void invalidShelf(){
        Action action = new MoveFromShelfToHandAction("Ernestino", servant, 1, "eheh");
        assertThrows(IllegalActionException.class, ()-> action.execute(gameContext));
    }

    @Test
    public void nullGameContext(){
        Action action = new MoveFromShelfToHandAction("Ernestino", shield, 1, "TopShelf");
        assertThrows(NullPointerException.class, ()-> action.execute(null));
    }

    @Test
    public void movingAResourceNotPresent(){
        Action action = new MoveFromShelfToHandAction("Ernestino", shield, 1, "BottomShelf");
        assertThrows(IllegalActionException.class, ()-> action.execute(gameContext));
    }


}
