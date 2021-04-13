package it.polimi.ingsw;


import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.actions.EndMarketAction;
import it.polimi.ingsw.model.fsm.GameContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EndMarketActionTest {
    private GameContext gameContext;

    private final ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
    private final ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();

    private Player player1;
    private Player player2;
    private Player player3;

    @BeforeEach
    public void init(){
        player1 = new Player("Paolo", 0);
        player2 = new Player("Genoveffa", 1);
        player3 = new Player("Gertrude", 2);

        GameModel model = new GameModel(Arrays.asList(player1, player2, player3));

        gameContext = new GameContext(model);
    }

    @Test
    public void threeResourcesDiscarded(){
        assertDoesNotThrow(()->player3.getBoard().getStorage().getMarketBasket().addResources(gold, 2));
        assertDoesNotThrow(()->player3.getBoard().getStorage().getMarketBasket().addResources(servant, 1));
        assertDoesNotThrow(()->new EndMarketAction("Gertrude").execute(gameContext));

        assertEquals(3, player1.getBoard().getFaithHolder().getFaithPoints());
        assertEquals(3, player2.getBoard().getFaithHolder().getFaithPoints());
        assertEquals(0, player3.getBoard().getFaithHolder().getFaithPoints());
        assertEquals(0, player3.getBoard().getStorage().getMarketBasket().totalAmountOfResources());
    }

    @Test
    public void noResourceDiscarded(){
        assertDoesNotThrow(()->new EndMarketAction("Gertrude").execute(gameContext));
        assertEquals(0, player1.getBoard().getFaithHolder().getFaithPoints());
        assertEquals(0, player2.getBoard().getFaithHolder().getFaithPoints());
        assertEquals(0, player3.getBoard().getFaithHolder().getFaithPoints());
        assertEquals(0, player3.getBoard().getStorage().getMarketBasket().totalAmountOfResources());
    }

    @Test
    public void nullPlayerCreation(){
        assertThrows(NullPointerException.class, ()->new EndMarketAction(null));
    }

    @Test
    public void noSuchPlayerExecute(){
        assertThrows(IllegalActionException.class, ()->new EndMarketAction("boh").execute(gameContext));
    }

    @Test
    public void nullContextExecute(){
        assertThrows(NullPointerException.class, ()->new EndMarketAction("Paolo").execute(null));
    }
}
