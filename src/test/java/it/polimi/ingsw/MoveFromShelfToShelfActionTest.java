package it.polimi.ingsw;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.actions.Action;
import it.polimi.ingsw.model.actions.MoveFromShelfToShelfAction;
import it.polimi.ingsw.model.fsm.GameContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MoveFromShelfToShelfActionTest {

    private GameContext gameContext;
    private final ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
    private final ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();

    @BeforeEach
    public void init(){

        Player player1 = new Player("Ernestino", 0);
        Player player2 = new Player("Teodolinda", 1);
        GameModel model = new GameModel(Arrays.asList(player1, player2));
        gameContext = new GameContext(model);

        assertDoesNotThrow(()->player1.getBoard().getStorage().getCupboard().getShelfById("BottomShelf").addResources(gold, 3));
        assertDoesNotThrow(()->player1.getBoard().getStorage().getCupboard().getShelfById("MiddleShelf").addResources(servant, 2));
        assertDoesNotThrow(()->player1.getBoard().getStorage().getCupboard().getShelfById("TopShelf").addResources(gold, 1));
        assertDoesNotThrow(()->player2.getBoard().getStorage().getCupboard().getShelfById("MiddleShelf").addResources(servant, 1));
        assertDoesNotThrow(()->player2.getBoard().getStorage().getCupboard().getShelfById("BottomShelf").addResources(gold, 1));

    }

    @Test
    public void executeTest(){

        Action action = new MoveFromShelfToShelfAction("Teodolinda", gold, 1, "BottomShelf", "TopShelf");
        List<Message> messages;

        try{
            messages = action.execute(gameContext);
        }catch(IllegalActionException e){
            throw new RuntimeException();
        }

        assertEquals(new HashMap<ResourceSingle, Integer>(){{put(gold, 1);}}
                ,gameContext.getGameModel().getPlayerById("Teodolinda").getBoard().getStorage().getCupboard()
                        .getShelfById("TopShelf").getAllResources());
        assertEquals(0, gameContext.getGameModel().getPlayerById("Teodolinda").getBoard().getStorage().getCupboard()
                .getShelfById("BottomShelf").getAmount());
        assertEquals("INFO: 1 of gold have been moved from Teodolinda's BottomShelf to their TopShelf", messages.get(0).toString());
    }

    @Test
    public void nullPlayer(){
        assertThrows(NullPointerException.class, ()-> new MoveFromShelfToShelfAction(null, gold, 2,
                "BottomShelf", "MiddleShelf"));
    }

    @Test
    public void nullResourceToMove(){
        assertThrows(NullPointerException.class, ()-> new MoveFromShelfToShelfAction("Ernestino", null,
                1, "MiddleShelf", "TopShelf"));
    }

    @Test
    public void nullFormerShelfID(){
        assertThrows(NullPointerException.class, ()-> new MoveFromShelfToShelfAction("Ernestino", gold, 1,
                null, "BottomShelf"));
    }

    @Test
    public void nullDestinationShelfID(){
        assertThrows(NullPointerException.class, ()-> new MoveFromShelfToShelfAction("Ernestino", servant, 1,
                "TopShelf", null));
    }

    @Test
    public void invalidAmount(){
        assertThrows(IllegalArgumentException.class, ()-> new MoveFromShelfToShelfAction("Ernestino", gold, 0,
                "BottomShelf", "TopShelf"));
        assertThrows(IllegalArgumentException.class, ()-> new MoveFromShelfToShelfAction("Ernestino", servant, -1,
                "TopShelf", "MiddleShelf"));
    }

    @Test
    public void invalidPlayer() {
        Action action = new MoveFromShelfToShelfAction("Ottone", gold, 2, "BottomShelf", "MiddleShelf");
        assertThrows(IllegalActionException.class, ()-> action.execute(gameContext));
    }

    @Test
    public void invalidFormerShelf(){
        Action action = new MoveFromShelfToShelfAction("Ernestino", gold, 1, "NotBottomShelf", "TopShelf");
        assertThrows(IllegalActionException.class, ()-> action.execute(gameContext));
    }

    @Test
    public void invalidDestinationShelf(){
        Action action = new MoveFromShelfToShelfAction("Ernestino", gold, 1, "BottomShelf", "NotTopShelf");
        assertThrows(IllegalActionException.class, ()-> action.execute(gameContext));
    }

    @Test
    public void nullGameContext(){
        Action action = new MoveFromShelfToShelfAction("Ernestino", gold, 1, "BottomShelf", "TopShelf");
        assertThrows(NullPointerException.class, ()-> action.execute(null));
    }

    @Test
    public void movingAResourceToAnInvalidShelf(){
        Action action = new MoveFromShelfToShelfAction("Teodolinda", gold, 1, "BottomShelf", "MiddleShelf");
        assertThrows(IllegalActionException.class, ()-> action.execute(gameContext));
    }

    @Test
    public void movingAResourceToAFullShelf(){
        Action action = new MoveFromShelfToShelfAction("Ernestino", gold, 1, "TopShelf", "BottomShelf");
        assertThrows(IllegalActionException.class, ()-> action.execute(gameContext));
    }

    @Test
    public void movingAResourceNotPresent(){
        Action action = new MoveFromShelfToShelfAction("Teodolinda", gold, 1, "TopShelf", "BottomShelf");
        assertThrows(IllegalActionException.class, ()-> action.execute(gameContext));
    }

}
