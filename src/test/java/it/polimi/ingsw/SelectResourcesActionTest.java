package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.actions.SelectResourcesAction;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.storage.Cupboard;
import it.polimi.ingsw.model.storage.ResourceContainer;
import it.polimi.ingsw.model.storage.Shelf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SelectResourcesActionTest {
    private GameContext gameContext;

    private final ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
    private final ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();

    private Player player1;


    @BeforeEach
    public void init(){
        player1 = new Player("Paolo", 0);


        GameModel model = new GameModel(Collections.singletonList(player1));

        assertDoesNotThrow(()->player1.getBoard().getStorage().getChest().addResources(gold, 3));
        assertDoesNotThrow(()->player1.getBoard().getStorage().getChest().addResources(servant, 3));

        gameContext = new GameContext(model);
    }

    @Test
    public void validSelection(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Paolo"));
        Cupboard cupboard = player1.getBoard().getStorage().getCupboard();
        Shelf bottomShelf = cupboard.getShelfById("BottomShelf");
        assertDoesNotThrow(()->bottomShelf.addResources(servant, 2));

        assertDoesNotThrow(()->new SelectResourcesAction("Paolo", "Chest", servant, 1).execute(gameContext));
        assertDoesNotThrow(()->new SelectResourcesAction("Paolo", "BottomShelf", servant, 1).execute(gameContext));
        assertDoesNotThrow(()->new SelectResourcesAction("Paolo", "BottomShelf", servant, 1).execute(gameContext));

        Map<ResourceContainer, Map<ResourceSingle, Integer>> expectedSelected = new HashMap<>(){
            {
                put(bottomShelf, new HashMap<>(){{put(servant, 2);}});
                put(player1.getBoard().getStorage().getChest(), new HashMap<>(){{put(servant, 1);}});
            }
        };

        assertEquals(expectedSelected, player1.getBoard().getStorage().getSelection());
    }

    @Test
    public void nullPlayerCreation(){
        assertThrows(NullPointerException.class, ()->new SelectResourcesAction(null, "prova", gold, 3));
    }

    @Test
    public void nullContainerId(){
        assertThrows(NullPointerException.class, ()->new SelectResourcesAction("Paolo", null, gold, 3));
    }

    @Test
    public void nullResource(){
        assertThrows(NullPointerException.class, ()->new SelectResourcesAction("Genoveffa", "prova", null, 3));
    }

    @Test
    public void zeroAmount(){
        assertThrows(IllegalArgumentException.class, ()->new SelectResourcesAction("Gertrude", "prova", gold, 0));
    }

    @Test
    public void nullGameContext(){
        assertThrows(NullPointerException.class, ()->new SelectResourcesAction("Pippo", "prova", gold, 3).execute(null));
    }

    @Test
    public void noSuchPlayer(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Paolo"));
        assertThrows(IllegalActionException.class, ()->new SelectResourcesAction("Pippo", "BottomShelf", gold, 3).execute(gameContext));
    }

    @Test
    public void noSuchContainer(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Paolo"));
        assertThrows(IllegalActionException.class, ()->new SelectResourcesAction("Paolo", "Hand", gold, 3).execute(gameContext));
    }

    @Test
    public void wrongSelection(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Paolo"));
        Cupboard cupboard = player1.getBoard().getStorage().getCupboard();
        Shelf bottomShelf = cupboard.getShelfById("BottomShelf");
        assertDoesNotThrow(()->bottomShelf.addResources(servant, 2));

        assertDoesNotThrow(()->new SelectResourcesAction("Paolo", "BottomShelf", servant, 1).execute(gameContext));
        assertDoesNotThrow(()->new SelectResourcesAction("Paolo", "BottomShelf", servant, 1).execute(gameContext));
        assertThrows(IllegalActionException.class,()->new SelectResourcesAction("Paolo", "BottomShelf", servant, 1).execute(gameContext));
    }
}
