package it.polimi.ingsw;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.gamematerials.MarbleColor;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.actions.BuyFromMarketAction;
import it.polimi.ingsw.model.actions.SelectConversionsAction;
import it.polimi.ingsw.model.fsm.*;
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
    private final ResourceSingle shield = ResourceTypeSingleton.getInstance().getShieldResource();


    @BeforeEach
    public void init(){

        Player player1 = new Player("Paolo", 0);
        Player player2 = new Player("Genoveffa", 1);
        Player player3 = new Player("Gertrude", 2);

        GameModel model = new GameModel(Arrays.asList(player1, player2, player3), new Random(3));

        player2.getBoard().getConversionHolder().addConversionActuator(MarbleColor.WHITE,
                new ConversionActuator(Collections.singletonList(servant), 0));

        player2.getBoard().getConversionHolder().addConversionActuator(MarbleColor.WHITE,
                new ConversionActuator(Collections.singletonList(gold), 0));
        gameContext = new GameContext(model);
        gameContext.setCurrentPlayer(model.getPlayerById("Genoveffa"));

        //execute action from previous state
        assertDoesNotThrow(()->new MarketState(gameContext).handleAction(new BuyFromMarketAction("Genoveffa", true, 1)));
        currentState = new ConversionSelectionState(gameContext);

        //SELECTED MARBLES: G W Y P
        //POSSIBLE CONVERSIONS: {W->{(servant, 0), (gold, 0)}}
    }

    @Test
    public void handleValidConversionSelection(){
        List<Message> messages;

        try{
            messages = currentState.handleAction(new SelectConversionsAction("Genoveffa", Arrays.asList(0, 1, 0, 0)));
        }catch(FSMTransitionFailedException e){
            throw new RuntimeException(e);
        }

        assertTrue(messages.size() > 0);
        assertTrue(currentState.getNextState() instanceof BasketCollectState);
    }
}


