package it.polimi.ingsw;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.actions.Action;
import it.polimi.ingsw.model.actions.ResourcesMoveAction;
import it.polimi.ingsw.model.fsm.GameContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ResourcesMoveActionTest {

    private GameContext gameContext;

    private final ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
    private final ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
    private final ResourceSingle shield = ResourceTypeSingleton.getInstance().getShieldResource();

    @BeforeEach
    public void init(){
        Player player1 = new Player("Ernestino", 0);
        Player player2 = new Player("Genoveffa", 1);
        Player player3 = new Player("Gertrude", 2);

        GameModel model = new GameModel(Arrays.asList(player1, player2, player3));

        assertDoesNotThrow(()->player1.getBoard().getStorage().getHand().addResources(gold, 2));
        assertDoesNotThrow(()->player1.getBoard().getStorage().getHand().addResources(servant, 1));
        //Ernestino has 2 gold and 1 servant in his hand

        assertDoesNotThrow(()->player3.getBoard().getStorage().getCupboard().getShelfById("MiddleShelf").addResources(gold, 2));
        assertDoesNotThrow(()->player3.getBoard().getStorage().getHand().addResources(gold, 1));
        assertDoesNotThrow(()->player3.getBoard().getStorage().getCupboard().getShelfById("BottomShelf").addResources(servant, 1));
        //Gertrude has a full middle shelf, 1 gold in hand and 1 servant in bottom shelf

        assertDoesNotThrow(()->player2.getBoard().getStorage().getCupboard().getShelfById("BottomShelf").addResources(shield, 1));
        assertDoesNotThrow(()->player2.getBoard().getStorage().getCupboard().getShelfById("MiddleShelf").addResources(servant, 1));
        assertDoesNotThrow(()->player2.getBoard().getStorage().getHand().addResources(servant, 1));
        //Genoveffa has 1 shield in bottom shelf, 1 servant in middle shelf and one servant in hand

        gameContext = new GameContext(model);
    }

    @Test
    public void resourcesAreMovedToHand(){
        Action action = new ResourcesMoveAction("Gertrude", "BottomShelf", "Hand", servant, 1);
        assertDoesNotThrow(()-> action.execute(gameContext));

        assertEquals(new HashMap<ResourceSingle, Integer>(){{put(servant, 1);}{put(gold, 1);}}
                ,gameContext.getGameModel().getPlayerById("Gertrude").getBoard().getStorage().getHand().getAllResources());
    }

    @Test
    public void resourcesAreMovedToShelfFromShelf(){
        Action action = new ResourcesMoveAction("Genoveffa", "BottomShelf", "TopShelf", shield, 1);
        assertDoesNotThrow(()-> action.execute(gameContext));

        assertEquals(new HashMap<ResourceSingle, Integer>(){{put(shield, 1);}}
                ,gameContext.getGameModel().getPlayerById("Genoveffa").getBoard().getStorage().getCupboard()
                        .getShelfById("TopShelf").getAllResources());
    }

    @Test
    public void resourcesAreMovedToShelfFromHand(){
        Action action = new ResourcesMoveAction("Ernestino", "Hand", "MiddleShelf", gold, 2);
        assertDoesNotThrow(()-> action.execute(gameContext));

        assertEquals(new HashMap<ResourceSingle, Integer>(){{put(gold, 2);}}
                ,gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getStorage().getCupboard()
                        .getShelfById("MiddleShelf").getAllResources());
    }

    @Test
    public void resourcesAreRemovedFromHand(){
        Action action = new ResourcesMoveAction("Ernestino", "Hand", "MiddleShelf", gold, 2);
        assertDoesNotThrow(()-> action.execute(gameContext));

        assertEquals(new HashMap<ResourceSingle, Integer>(){{put(servant, 1);}}
                ,gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getStorage().getHand().getAllResources());
    }

    @Test
    public void resourcesAreRemovedFromShelf(){
        Action action = new ResourcesMoveAction("Genoveffa", "BottomShelf", "TopShelf", shield, 1);
        Action action1 = new ResourcesMoveAction("Gertrude", "BottomShelf", "Hand", servant, 1);
        assertDoesNotThrow(()-> action.execute(gameContext));
        assertDoesNotThrow(()-> action1.execute(gameContext));

        assertEquals(gameContext.getGameModel().getPlayerById("Genoveffa").getBoard().getStorage().getCupboard()
                .getShelfById("BottomShelf").getAmount(), 0);
        assertEquals(0, gameContext.getGameModel().getPlayerById("Gertrude").getBoard().getStorage()
        .getCupboard().getShelfById("BottomShelf").getAmount());
    }

    @Test
    public void shelfToShelfMessage(){
        Action action = new ResourcesMoveAction("Genoveffa", "BottomShelf", "TopShelf", shield, 1);
        List<Message> messages;

        try{
            messages = action.execute(gameContext);
        }catch(IllegalActionException e){
            throw new RuntimeException();
        }

    }

    @Test
    public void handToShelfMessage(){
        Action action = new ResourcesMoveAction("Ernestino", "Hand", "MiddleShelf", gold, 2);
        List<Message> messages;

        try{
            messages = action.execute(gameContext);
        }catch(IllegalActionException e){
            throw new RuntimeException();
        }

    }

    @Test
    public void shelfToHandMessage(){
        Action action = new ResourcesMoveAction("Gertrude", "BottomShelf", "Hand", servant, 1);
        List<Message> messages;

        try{
            messages = action.execute(gameContext);
        }catch(IllegalActionException e){
            throw new RuntimeException();
        }

    }

    @Test
    public void nullPlayer(){
        assertThrows(NullPointerException.class, ()-> new ResourcesMoveAction(null, "BottomShelf", "Hand", shield, 1));
    }

    @Test
    public void nullOrigin(){
        assertThrows(NullPointerException.class, ()-> new ResourcesMoveAction("Ernestino", null, "Hand", shield, 1));
    }

    @Test
    public void nullDestination(){
        assertThrows(NullPointerException.class, ()-> new ResourcesMoveAction("Ernestino", "BottomShelf", null, shield, 1));
    }

    @Test
    public void nullResourceToMove(){
        assertThrows(NullPointerException.class, ()-> new ResourcesMoveAction("Ernestino", "BottomShelf", "Hand", null, 1));
    }

    @Test
    public void invalidAmount(){
        assertThrows(IllegalArgumentException.class, ()-> new ResourcesMoveAction("Ernestino", "BottomShelf", "Hand", shield, 0));
        assertThrows(IllegalArgumentException.class, ()-> new ResourcesMoveAction("Ernestino", "BottomShelf", "Hand", shield, -1));
    }

    @Test
    public void nullGameContext(){
        Action action = new ResourcesMoveAction("Ernestino", "Hand", "MiddleShelf", gold, 2);
        assertThrows(NullPointerException.class, ()-> action.execute(null));
    }

    @Test
    public void noSuchPlayer(){
        Action action = new ResourcesMoveAction("Pollo", "Hand", "BottomShelf", shield, 1);
        assertThrows(IllegalActionException.class, ()-> action.execute(gameContext));
    }

    @Test
    public void noSuchOrigin(){
        Action action = new ResourcesMoveAction("Genoveffa", "Landfill", "BottomShelf", shield, 1);
        assertThrows(IllegalActionException.class, ()-> action.execute(gameContext));
    }

    @Test
    public void noSuchDestination(){
        Action action = new ResourcesMoveAction("Genoveffa", "BottomShelf", "DisneyLand", servant, 2);
        assertThrows(IllegalActionException.class, ()-> action.execute(gameContext));
    }

    @Test
    public void fullDestinationShelf(){
        Action action = new ResourcesMoveAction("Gertrude", "Hand", "MiddleShelf", gold, 1);
        Action action1 = new ResourcesMoveAction("Gertrude", "BottomShelf", "MiddleShelf", servant, 1);
        assertThrows(IllegalActionException.class, ()-> action.execute(gameContext));
        assertThrows(IllegalActionException.class, ()-> action1.execute(gameContext));
    }

    @Test
    public void movingResourceNotPresentInHand(){
        Action action = new ResourcesMoveAction("Gertrude", "Hand", "TopShelf", shield, 1);
        assertThrows(IllegalActionException.class, ()-> action.execute(gameContext));
    }

    @Test
    public void movingResourceNotPresentInShelf(){
        Action action = new ResourcesMoveAction("Genoveffa", "BottomShelf", "MiddleShelf", gold, 1);
        Action action1 = new ResourcesMoveAction("Genoveffa", "BottomShelf", "Hand", gold, 1);
        assertThrows(IllegalActionException.class, ()-> action.execute(gameContext));
        assertThrows(IllegalActionException.class, ()-> action1.execute(gameContext));
    }

    @Test
    public void movingResourceToIncompatibleShelf(){
        Action action = new ResourcesMoveAction("Genoveffa", "MiddleShelf", "BottomShelf", servant, 1);
        Action action1 = new ResourcesMoveAction("Genoveffa", "Hand", "BottomShelf", servant, 1);
        assertThrows(IllegalActionException.class, ()-> action.execute(gameContext));
        assertThrows(IllegalActionException.class, ()-> action1.execute(gameContext));
    }

}
