package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.actions.ConfirmTidyAction;
import it.polimi.ingsw.model.actions.ResourcesMoveAction;
import it.polimi.ingsw.model.fsm.*;
import it.polimi.ingsw.model.fsm.states.BasketCollectState;
import it.polimi.ingsw.model.fsm.states.ResourceTidyingState;
import it.polimi.ingsw.model.storage.Cupboard;
import it.polimi.ingsw.model.storage.LeaderDecorator;
import it.polimi.ingsw.model.storage.Shelf;
import it.polimi.ingsw.model.storage.Storage;
import it.polimi.ingsw.server.ServerBuilder;
import it.polimi.ingsw.utils.ResourceLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ResourceTidyingStateTest {

    private State currentState;

    private final ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
    private final ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
    private final ResourceSingle shield = ResourceTypeSingleton.getInstance().getShieldResource();

    @BeforeEach
    public void init() throws ParserException {

        List<String> usernames = Arrays.asList("Paolo", "Genoveffa", "Gertrude");
        String config = ResourceLoader.loadFile("cfg/config.json");
        String crafting = ResourceLoader.loadFile("cfg/crafting.json");
        String faith = ResourceLoader.loadFile("cfg/faith.json");
        String leaders = ResourceLoader.loadFile("cfg/leaders.json");

        GameModel model  = ServerBuilder.buildModel(config, crafting, faith, leaders, usernames, false, new Random(3));
        Player player2 = model.getPlayerById("Genoveffa");

        GameContext gameContext = new GameContext(model, false);
        gameContext.setCurrentPlayer(player2);

        Storage player2Storage = player2.getBoard().getStorage();
        Cupboard player2Cupboard = player2Storage.getCupboard();
        assertDoesNotThrow(() -> player2Cupboard.addResource(player2Cupboard.getShelfById("BottomShelf"), gold, 2));
        assertDoesNotThrow(() -> player2Cupboard.addResource(player2Cupboard.getShelfById("MiddleShelf"), servant, 2));
        assertDoesNotThrow(() -> player2Storage.decorate(new LeaderDecorator(new Shelf("LeaderShelf1", shield, 2), player2Cupboard)));
        Cupboard player2CupboardNew = player2Storage.getCupboard();
        assertDoesNotThrow(() -> player2CupboardNew.addResource(player2CupboardNew.getShelfById("LeaderShelf1"), shield, 1));

        currentState = new ResourceTidyingState(gameContext);
    }

    @Test
    public void validMoveToHand(){
        assertDoesNotThrow(() -> currentState.handleAction(new ResourcesMoveAction("Genoveffa", "LeaderShelf1", "Hand", shield, 1)));
        assertEquals(currentState, currentState.getNextState());
    }

    @Test
    public void invalidMoveToHand(){
        assertThrows(FSMTransitionFailedException.class, () -> currentState.handleAction(new ResourcesMoveAction("Genoveffa", "LeaderShelf1", "Hand", gold, 1)));
        assertNull(currentState.getNextState());
    }

    @Test
    public void validConfirmTidy(){
        assertDoesNotThrow(() -> currentState.handleAction(new ConfirmTidyAction("Genoveffa")));
        assertTrue(currentState.getNextState() instanceof BasketCollectState);
    }

    @Test
    public void confirmTidyWithoutEmptyHand(){
        assertDoesNotThrow(() -> currentState.handleAction(new ResourcesMoveAction("Genoveffa", "LeaderShelf1", "Hand", shield, 1)));
        assertTrue(currentState.getNextState() instanceof ResourceTidyingState);
        currentState.resetNextState();

        assertThrows(FSMTransitionFailedException.class, () -> currentState.handleAction(new ConfirmTidyAction("Genoveffa")));
        assertNull(currentState.getNextState());
        currentState.resetNextState();

        assertDoesNotThrow(() -> currentState.handleAction(new ResourcesMoveAction("Genoveffa", "Hand", "LeaderShelf1", shield, 1)));
        assertTrue(currentState.getNextState() instanceof ResourceTidyingState);
        currentState.resetNextState();

        assertDoesNotThrow(() -> currentState.handleAction(new ConfirmTidyAction("Genoveffa")));
        assertTrue(currentState.getNextState() instanceof BasketCollectState);
    }


}
