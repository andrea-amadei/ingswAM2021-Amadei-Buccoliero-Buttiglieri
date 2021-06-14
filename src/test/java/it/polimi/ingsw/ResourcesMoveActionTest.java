package it.polimi.ingsw;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.actions.Action;
import it.polimi.ingsw.model.actions.ResourcesMoveAction;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.server.ServerBuilder;
import it.polimi.ingsw.utils.ResourceLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ResourcesMoveActionTest {

    private GameContext gameContext;

    private final ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
    private final ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
    private final ResourceSingle shield = ResourceTypeSingleton.getInstance().getShieldResource();

    @BeforeEach
    public void init() throws ParserException {

        List<String> usernames = Arrays.asList("Ernestino", "Genoveffa", "Gertrude");
        String config = ResourceLoader.loadFile("cfg/config.json");
        String crafting = ResourceLoader.loadFile("cfg/crafting.json");
        String faith = ResourceLoader.loadFile("cfg/faith.json");
        String leaders = ResourceLoader.loadFile("cfg/leaders.json");

        GameModel model = ServerBuilder.buildModel(config, crafting, faith, leaders, usernames, false, new Random(3));

        Player player1 = model.getPlayerById("Ernestino");
        Player player2 = model.getPlayerById("Genoveffa");
        Player player3 = model.getPlayerById("Gertrude");

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

        gameContext = new GameContext(model, false);
    }

    @Test
    public void resourcesAreMovedToHand(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Gertrude"));
        Action action = new ResourcesMoveAction("Gertrude", "BottomShelf", "Hand", servant, 1);
        assertDoesNotThrow(()-> action.execute(gameContext));

        assertEquals(new HashMap<ResourceSingle, Integer>(){{put(servant, 1);}{put(gold, 1);}}
                ,gameContext.getGameModel().getPlayerById("Gertrude").getBoard().getStorage().getHand().getAllResources());
    }

    @Test
    public void resourcesAreMovedToShelfFromShelf(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Genoveffa"));
        Action action = new ResourcesMoveAction("Genoveffa", "BottomShelf", "TopShelf", shield, 1);
        assertDoesNotThrow(()-> action.execute(gameContext));

        assertEquals(new HashMap<ResourceSingle, Integer>(){{put(shield, 1);}}
                ,gameContext.getGameModel().getPlayerById("Genoveffa").getBoard().getStorage().getCupboard()
                        .getShelfById("TopShelf").getAllResources());
    }

    @Test
    public void resourcesAreMovedToShelfFromHand(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Ernestino"));
        Action action = new ResourcesMoveAction("Ernestino", "Hand", "MiddleShelf", gold, 2);
        assertDoesNotThrow(()-> action.execute(gameContext));

        assertEquals(new HashMap<ResourceSingle, Integer>(){{put(gold, 2);}}
                ,gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getStorage().getCupboard()
                        .getShelfById("MiddleShelf").getAllResources());
    }

    @Test
    public void resourcesAreRemovedFromHand(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Ernestino"));
        Action action = new ResourcesMoveAction("Ernestino", "Hand", "MiddleShelf", gold, 2);
        assertDoesNotThrow(()-> action.execute(gameContext));

        assertEquals(new HashMap<ResourceSingle, Integer>(){{put(servant, 1);}}
                ,gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getStorage().getHand().getAllResources());
    }

    @Test
    public void resourcesAreRemovedFromShelf(){
        Action action = new ResourcesMoveAction("Genoveffa", "BottomShelf", "TopShelf", shield, 1);
        Action action1 = new ResourcesMoveAction("Gertrude", "BottomShelf", "Hand", servant, 1);

        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Genoveffa"));
        assertDoesNotThrow(()-> action.execute(gameContext));
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Gertrude"));
        assertDoesNotThrow(()-> action1.execute(gameContext));

        assertEquals(gameContext.getGameModel().getPlayerById("Genoveffa").getBoard().getStorage().getCupboard()
                .getShelfById("BottomShelf").getAmount(), 0);
        assertEquals(0, gameContext.getGameModel().getPlayerById("Gertrude").getBoard().getStorage()
        .getCupboard().getShelfById("BottomShelf").getAmount());
    }

    @Test
    public void shelfToShelfMessage(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Genoveffa"));
        Action action = new ResourcesMoveAction("Genoveffa", "BottomShelf", "TopShelf", shield, 1);
        List<Message> messages;

        try{
            messages = action.execute(gameContext);
        }catch(IllegalActionException e){
            throw new RuntimeException();
        }

        assertTrue(messages.size() > 0);

    }

    @Test
    public void handToShelfMessage(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Ernestino"));
        Action action = new ResourcesMoveAction("Ernestino", "Hand", "MiddleShelf", gold, 2);
        List<Message> messages;

        try{
            messages = action.execute(gameContext);
        }catch(IllegalActionException e){
            throw new RuntimeException();
        }

        assertTrue(messages.size() > 0);
    }

    @Test
    public void shelfToHandMessage(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Gertrude"));
        Action action = new ResourcesMoveAction("Gertrude", "BottomShelf", "Hand", servant, 1);
        List<Message> messages;

        try{
            messages = action.execute(gameContext);
        }catch(IllegalActionException e){
            throw new RuntimeException();
        }

        assertTrue(messages.size() > 0);
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
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Gertrude"));
        Action action = new ResourcesMoveAction("Pollo", "Hand", "BottomShelf", shield, 1);
        assertThrows(IllegalActionException.class, ()-> action.execute(gameContext));
    }

    @Test
    public void noSuchOrigin(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Genoveffa"));
        Action action = new ResourcesMoveAction("Genoveffa", "Landfill", "BottomShelf", shield, 1);
        assertThrows(IllegalActionException.class, ()-> action.execute(gameContext));
    }

    @Test
    public void noSuchDestination(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Genoveffa"));
        Action action = new ResourcesMoveAction("Genoveffa", "BottomShelf", "DisneyLand", servant, 2);
        assertThrows(IllegalActionException.class, ()-> action.execute(gameContext));
    }

    @Test
    public void fullDestinationShelf(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Gertrude"));
        Action action = new ResourcesMoveAction("Gertrude", "Hand", "MiddleShelf", gold, 1);
        Action action1 = new ResourcesMoveAction("Gertrude", "BottomShelf", "MiddleShelf", servant, 1);
        assertThrows(IllegalActionException.class, ()-> action.execute(gameContext));
        assertThrows(IllegalActionException.class, ()-> action1.execute(gameContext));
    }

    @Test
    public void movingResourceNotPresentInHand(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Gertrude"));
        Action action = new ResourcesMoveAction("Gertrude", "Hand", "TopShelf", shield, 1);
        assertThrows(IllegalActionException.class, ()-> action.execute(gameContext));
    }

    @Test
    public void movingResourceNotPresentInShelf(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Genoveffa"));
        Action action = new ResourcesMoveAction("Genoveffa", "BottomShelf", "MiddleShelf", gold, 1);
        Action action1 = new ResourcesMoveAction("Genoveffa", "BottomShelf", "Hand", gold, 1);
        assertThrows(IllegalActionException.class, ()-> action.execute(gameContext));
        assertThrows(IllegalActionException.class, ()-> action1.execute(gameContext));
    }

    @Test
    public void movingResourceToIncompatibleShelf(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Genoveffa"));
        Action action = new ResourcesMoveAction("Genoveffa", "MiddleShelf", "BottomShelf", servant, 1);
        Action action1 = new ResourcesMoveAction("Genoveffa", "Hand", "BottomShelf", servant, 1);
        assertThrows(IllegalActionException.class, ()-> action.execute(gameContext));
        assertThrows(IllegalActionException.class, ()-> action1.execute(gameContext));
    }

}
