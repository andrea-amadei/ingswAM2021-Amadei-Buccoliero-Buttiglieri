package it.polimi.ingsw;


import it.polimi.ingsw.common.exceptions.IllegalActionException;
import it.polimi.ingsw.common.exceptions.ParserException;
import it.polimi.ingsw.server.model.basetypes.ResourceSingle;
import it.polimi.ingsw.server.model.basetypes.ResourceTypeSingleton;
import it.polimi.ingsw.server.model.global.GameModel;
import it.polimi.ingsw.server.model.global.Player;
import it.polimi.ingsw.server.model.actions.ConfirmTidyAction;
import it.polimi.ingsw.server.model.fsm.GameContext;
import it.polimi.ingsw.server.ServerBuilder;
import it.polimi.ingsw.common.utils.ResourceLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ConfirmTidyActionTest {
    private GameContext gameContext;

    private final ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();


    @BeforeEach
    public void init() throws ParserException {

        List<String> usernames = Arrays.asList("Paolo", "Genoveffa", "Gertrude");
        String config = ResourceLoader.loadFile("cfg/config.json");
        String crafting = ResourceLoader.loadFile("cfg/crafting.json");
        String faith = ResourceLoader.loadFile("cfg/faith.json");
        String leaders = ResourceLoader.loadFile("cfg/leaders.json");

        GameModel model = ServerBuilder.buildModel(config, crafting, faith, leaders, usernames, false, new Random(3));

        gameContext = new GameContext(model, false);
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
