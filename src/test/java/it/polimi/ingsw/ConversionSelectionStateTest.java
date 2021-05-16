package it.polimi.ingsw;

import it.polimi.ingsw.common.ActionQueue;
import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.gamematerials.MarbleColor;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.actions.Action;
import it.polimi.ingsw.model.actions.BuyFromMarketAction;
import it.polimi.ingsw.model.actions.PopeCheckAction;
import it.polimi.ingsw.model.actions.SelectConversionsAction;
import it.polimi.ingsw.model.fsm.*;
import it.polimi.ingsw.model.fsm.states.ConversionSelectionState;
import it.polimi.ingsw.model.fsm.states.MarketState;
import it.polimi.ingsw.model.fsm.states.ResourceTidyingState;
import it.polimi.ingsw.model.holder.FaithHolder;
import it.polimi.ingsw.model.market.ConversionActuator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ConversionSelectionStateTest {

    private GameContext gameContext;
    private State currentState;

    private final ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
    private final ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();

    private GameModel model;
    private ActionQueue actionQueue;


    @BeforeEach
    public void init(){

        Player player1 = new Player("Paolo", 0);
        Player player2 = new Player("Genoveffa", 1);
        Player player3 = new Player("Gertrude", 2);

        model = new GameModel(Arrays.asList(player1, player2, player3), new Random(3));

        player2.getBoard().getConversionHolder().addConversionActuator(MarbleColor.WHITE,
                new ConversionActuator(Collections.singletonList(servant), 0));

        player2.getBoard().getConversionHolder().addConversionActuator(MarbleColor.WHITE,
                new ConversionActuator(Collections.singletonList(gold), 0));
        gameContext = new GameContext(model);
        gameContext.setCurrentPlayer(model.getPlayerById("Genoveffa"));

        actionQueue = new ActionQueue();

    }

    @Test
    public void handleValidConversionSelection(){
        //execute action from previous state
        assertDoesNotThrow(()->new MarketState(gameContext).handleAction(new BuyFromMarketAction("Genoveffa", true, 1)));
        currentState = new ConversionSelectionState(gameContext);

        //SELECTED MARBLES: G W Y P
        //POSSIBLE CONVERSIONS: {W->{(servant, 0), (gold, 0)}}

        List<Message> messages;

        try{
            messages = currentState.handleAction(new SelectConversionsAction("Genoveffa", Arrays.asList(0, 1, 0, 0)));
        }catch(FSMTransitionFailedException e){
            throw new RuntimeException(e);
        }

        assertTrue(messages.size() > 0);
        assertTrue(currentState.getNextState() instanceof ResourceTidyingState);
    }

    @Test
    public void popeCheckOnConversionSelection() throws InterruptedException {
        //Genoveffa is one faith point from triggering a pope check
        model.getFaithPath().executeMovement(5, model.getPlayerById("Genoveffa"));

        //Genoveffa buys a row that includes a faith point
        assertDoesNotThrow(()->new MarketState(gameContext).handleAction(new BuyFromMarketAction("Genoveffa", true, 2)));
        currentState = new ConversionSelectionState(gameContext);

        StateMachine stateMachine = new StateMachine(actionQueue, gameContext, currentState);
        model.getFaithPath().setListener(stateMachine);

        try{
            currentState.handleAction(new SelectConversionsAction("Genoveffa", Arrays.asList(0, 0, 0, 0)));
        }catch(FSMTransitionFailedException e){
            throw new RuntimeException(e);
        }

        Action popeAction = actionQueue.pop();
        assertTrue(popeAction instanceof PopeCheckAction);

        stateMachine.executeAction(popeAction);

        assertTrue(model.getPlayerById("Genoveffa").getBoard().getFaithHolder().isPopeCardActive(0));
        assertEquals(FaithHolder.CheckpointStatus.INACTIVE, model.getPlayerById("Paolo").getBoard().
                getFaithHolder().getPopeCardStatus(0));
    }
}


