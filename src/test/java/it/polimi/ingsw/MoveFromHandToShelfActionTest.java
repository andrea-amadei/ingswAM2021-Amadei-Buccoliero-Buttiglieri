package it.polimi.ingsw;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.actions.Action;
import it.polimi.ingsw.model.actions.MoveFromHandToShelfAction;
import it.polimi.ingsw.model.fsm.GameContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MoveFromHandToShelfActionTest {
    private GameContext gameContext;

    private final ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
    private final ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
    private final ResourceSingle shield = ResourceTypeSingleton.getInstance().getShieldResource();

    @BeforeAll
    public void init(){
        Player player1 = new Player("Paolo", 0);
        Player player2 = new Player("Genoveffa", 1);
        Player player3 = new Player("Gertrude", 2);

        GameModel model = new GameModel(Arrays.asList(player1, player2, player3));

        assertDoesNotThrow(()->player1.getBoard().getStorage().getHand().addResources(gold, 3));
        assertDoesNotThrow(()->player1.getBoard().getStorage().getHand().addResources(servant, 3));

        gameContext = new GameContext(model);
    }

    @Test
    public void validActionExecution(){
        Action action = new MoveFromHandToShelfAction("Paolo", servant, 2, "BottomShelf");
        List<Message> messages;
        try{
            messages = action.execute(gameContext);
        }catch(IllegalActionException e){
            throw new RuntimeException();
        }

        assertEquals(new HashMap<ResourceSingle, Integer>(){{put(servant, 2);}}
          ,gameContext.getGameModel().getPlayerById("Paolo").getBoard().getStorage().getCupboard()
                      .getShelfById("BottomShelf").getAllResources());

        assertEquals("INFO: 2 of Servant have been moved from Paolo's hand to BottomShelf", messages.get(0).toString());
    }

    @Test
    public void nullPlayerCreation(){
        assertThrows(NullPointerException.class, ()->new MoveFromHandToShelfAction(null, gold, 2, "asdf"));
    }

    @Test
    public void nullResourceCreation(){
        assertThrows(NullPointerException.class, ()->new MoveFromHandToShelfAction("Paolo", null, 2, "asdf"));
    }

    @Test
    public void negativeAmountCreation(){
        assertThrows(IllegalArgumentException.class, ()->new MoveFromHandToShelfAction("Paolo", shield, 0, "asdf"));
    }

    @Test
    public void nullContext(){
        Action action = new MoveFromHandToShelfAction("Paolo", gold, 2, "MiddleShelf");
        assertThrows(NullPointerException.class, ()->action.execute(null));
    }
    @Test
    public void noSuchPlayer(){
        Action action = new MoveFromHandToShelfAction("Nessuno", shield, 2, "BottomShelf");
        assertThrows(IllegalActionException.class, ()->action.execute(gameContext));
    }

    @Test
    public void noSuchShield(){
        Action action = new MoveFromHandToShelfAction("Genoveffa", shield, 2, "BohShelf");
        assertThrows(IllegalActionException.class, ()->action.execute(gameContext));
    }

    @Test
    public void impossibleTransaction(){
        Action action = new MoveFromHandToShelfAction("Genoveffa", shield, 2, "BottomShelf");
        assertThrows(IllegalActionException.class, ()->action.execute(gameContext));
    }
}
