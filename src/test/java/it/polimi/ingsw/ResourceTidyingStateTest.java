package it.polimi.ingsw;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.common.exceptions.ParserException;
import it.polimi.ingsw.server.model.basetypes.ResourceSingle;
import it.polimi.ingsw.server.model.basetypes.ResourceTypeSingleton;
import it.polimi.ingsw.server.model.global.GameModel;
import it.polimi.ingsw.server.model.global.Player;
import it.polimi.ingsw.server.model.actions.ConfirmTidyAction;
import it.polimi.ingsw.server.model.actions.ResourcesMoveAction;
import it.polimi.ingsw.server.model.fsm.*;
import it.polimi.ingsw.server.model.fsm.states.BasketCollectState;
import it.polimi.ingsw.server.model.fsm.states.ResourceTidyingState;
import it.polimi.ingsw.server.model.storage.Cupboard;
import it.polimi.ingsw.server.model.storage.LeaderDecorator;
import it.polimi.ingsw.server.model.storage.Shelf;
import it.polimi.ingsw.server.model.storage.Storage;
import it.polimi.ingsw.server.ServerBuilder;
import it.polimi.ingsw.common.utils.ResourceLoader;
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

    //method "handleAction" successfully moves resources to the player's hand if said resources is owned by the player
    @Test
    public void validMoveToHand(){
        assertDoesNotThrow(() -> currentState.handleAction(new ResourcesMoveAction("Genoveffa", "LeaderShelf1", "Hand", shield, 1)));
        assertEquals(currentState, currentState.getNextState());
    }

    //method "handleAction" throws NullPointerException if parameter "resourcesMoveAction" is null
    @Test
    public void nullResourcesMoveAction(){
        assertThrows(NullPointerException.class, ()-> currentState.handleAction((ResourcesMoveAction) null));
    }

    //method "handleAction" throws NullPointerException if parameter "confirmTidyAction" is null
    @Test
    public void nullConfirmTidyAction(){
        assertThrows(NullPointerException.class, ()-> currentState.handleAction((ConfirmTidyAction) null));
    }

    //method "handleAction" throws FSMTransitionFailedException if the player tries to move a resource they do not own
    @Test
    public void invalidMoveToHand(){
        assertThrows(FSMTransitionFailedException.class, () -> currentState.handleAction(new ResourcesMoveAction("Genoveffa", "LeaderShelf1", "Hand", gold, 1)));
        assertNull(currentState.getNextState());
    }

    //method "handleAction" successfully manages action "confirmTidyAction" if hand is empty
    @Test
    public void validConfirmTidy(){
        assertDoesNotThrow(() -> currentState.handleAction(new ConfirmTidyAction("Genoveffa")));
        assertTrue(currentState.getNextState() instanceof BasketCollectState);
    }

    //this test makes sure a player cannot under any circumstances confirm tidying while still having something in their hand
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

    //method "onEntry" correctly sends messages
    @Test
    public void onEntryTest(){
        List<Message> messages;
        messages = currentState.onEntry();

        assertTrue(messages.size() > 0);
    }


}
