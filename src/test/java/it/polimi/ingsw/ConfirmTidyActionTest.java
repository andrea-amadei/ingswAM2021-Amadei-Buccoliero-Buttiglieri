package it.polimi.ingsw;


import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.actions.ConfirmTidyAction;
import it.polimi.ingsw.model.fsm.GameContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ConfirmTidyActionTest {
    private GameContext gameContext;

    private final ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();


    @BeforeEach
    public void init(){
        Player player1 = new Player("Paolo", 0);
        Player player2 = new Player("Genoveffa", 1);
        Player player3 = new Player("Gertrude", 2);

        GameModel model = new GameModel(Arrays.asList(player1, player2, player3));

        gameContext = new GameContext(model);
    }

    @Test
    public void nullCreation(){
        assertThrows(NullPointerException.class, ()->new ConfirmTidyAction(null));
    }

    @Test
    public void validEmptyHand(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Paolo"));
        assertDoesNotThrow(()->new ConfirmTidyAction("Paolo").execute(gameContext));
    }

    @Test
    public void invalidHandNotEmpty(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Paolo"));
        Player paolo = gameContext.getGameModel().getPlayerById("Paolo");
        assertDoesNotThrow(()->paolo.getBoard().getStorage().getHand().addResources(gold, 1));
        assertThrows(IllegalActionException.class, ()->new ConfirmTidyAction("Paolo").execute(gameContext));
    }

    @Test
    public void nullGameContext(){
        assertThrows(NullPointerException.class, ()->new ConfirmTidyAction("Paolo").execute(null));
    }

    @Test
    public void noSuchPlayer(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Paolo"));
        assertThrows(IllegalActionException.class, ()->new ConfirmTidyAction("test").execute(gameContext));
    }
}
