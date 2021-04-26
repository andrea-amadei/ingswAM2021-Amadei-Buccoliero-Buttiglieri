package it.polimi.ingsw;
import it.polimi.ingsw.common.ActionQueue;
import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.actions.PreliminaryPickAction;
import it.polimi.ingsw.model.actions.ResourcesMoveAction;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.fsm.State;
import it.polimi.ingsw.model.fsm.StateMachine;
import it.polimi.ingsw.model.fsm.states.MenuState;
import it.polimi.ingsw.model.leader.LeaderCard;
import it.polimi.ingsw.server.DummyBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StateMachineTest {

    private GameContext gameContext;

    private final ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
    private final ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();

    private Player player1;
    private Player player3;

    private List<Integer> leadersToDiscard;
    private Map<ResourceSingle, Integer> chosenResources;
    private ActionQueue actionQueue;

    @BeforeEach
    public void init(){
        player1 = new Player("Ernestino", 0);
        Player player2 = new Player("Cosma", 1);
        player3 = new Player("Leopoldo", 2);
        Player player4 = new Player("Urbano", 3);

        leadersToDiscard = new ArrayList<>();
        chosenResources = new HashMap<>();

        GameModel model = new GameModel(Arrays.asList(player1, player2, player3, player4));
        gameContext = new GameContext(model);
        gameContext.setCurrentPlayer(player1);
        actionQueue = new ActionQueue();

        List<LeaderCard> allLeaders = DummyBuilder.buildLeaderCards();
        assert allLeaders != null;

        //giving 4 leaders to player 1
        player1.getBoard().addLeaderCard(allLeaders.get(0));
        player1.getBoard().addLeaderCard(allLeaders.get(1));
        player1.getBoard().addLeaderCard(allLeaders.get(2));
        player1.getBoard().addLeaderCard(allLeaders.get(3));

        //giving 4 leaders to player 3
        player3.getBoard().addLeaderCard(allLeaders.get(4));
        player3.getBoard().addLeaderCard(allLeaders.get(5));
        player3.getBoard().addLeaderCard(allLeaders.get(6));
        player3.getBoard().addLeaderCard(allLeaders.get(7));

        //giving some resources to players
        player1.getBoard().getStorage().getHand().addResources(gold, 2);
        player1.getBoard().getStorage().getHand().addResources(servant, 2);

        player2.getBoard().getStorage().getHand().addResources(gold, 1);
        player2.getBoard().getStorage().getHand().addResources(servant, 3);
    }

    @Test
    public void createStateMachineWithInitialMenuState(){
        State menuState = new MenuState(gameContext);
        StateMachine fsm = new StateMachine(actionQueue, gameContext, menuState);
        assertTrue(fsm.getCurrentState() instanceof MenuState);
    }

    @Test
    public void executeResourceMoveInTheMenuState(){
        State menuState = new MenuState(gameContext);
        StateMachine fsm = new StateMachine(actionQueue, gameContext, menuState);

        actionQueue.appendLast(new ResourcesMoveAction("Ernestino", "Hand", "BottomShelf", gold, 1));
        List<Message> messages = fsm.executeAction(actionQueue.pop());

        assertTrue(fsm.getCurrentState() instanceof MenuState);

    }

    @Test
    public void executeInvalidResourceMoveInMenuState(){

        State menuState = new MenuState(gameContext);
        StateMachine fsm = new StateMachine(actionQueue, gameContext, menuState);

        actionQueue.appendLast(new ResourcesMoveAction("Ernestino", "Hand", "BottomShelf", gold, 4));
        List<Message> messages = fsm.executeAction(actionQueue.pop());

        assertTrue(fsm.getCurrentState() instanceof MenuState);
    }
}
