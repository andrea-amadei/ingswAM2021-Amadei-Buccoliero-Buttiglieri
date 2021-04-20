package it.polimi.ingsw;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.gamematerials.MarbleColor;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.actions.BackAction;
import it.polimi.ingsw.model.actions.BuyFromMarketAction;
import it.polimi.ingsw.model.fsm.*;
import it.polimi.ingsw.model.market.ConversionActuator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MarketStateTest {
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
        currentState = new MarketState(gameContext);
    }

    @Test
    public void validBuyFromMarketTransition(){
        List<Message> messages;
        try{
            messages = currentState.handleAction(new BuyFromMarketAction("Genoveffa", true, 1));
        }catch(FSMTransitionFailedException e){
            throw new RuntimeException();
        }
        assertTrue(messages.size() > 0);
        assertTrue(gameContext.hasPlayerMoved());
        assertTrue(currentState.getNextState() instanceof ConversionSelectionState);
    }

    @Test
    public void nonEmptyOnEntryMethod(){
        assertTrue(currentState.onEntry().size() > 0);
    }

    @Test
    public void invalidBuyFromMarketTransition(){
        assertThrows(FSMTransitionFailedException.class, ()->currentState.handleAction(new BuyFromMarketAction("Genoveffa", true, 5)));
        assertNull(currentState.getNextState());
        assertEquals(new ArrayList<>(), gameContext.getGameModel().getMarket().getSelectedMarbles());
    }

    @Test
    public void validGoBackRequest(){
        List<Message> messages;

        try{
            messages = currentState.handleAction(new BackAction("Genoveffa"));
        }catch(FSMTransitionFailedException e){
            throw new RuntimeException();
        }

        assertTrue(messages.size() > 0);
        assertTrue(currentState.getNextState() instanceof MenuState);
        assertFalse(gameContext.hasPlayerMoved());
    }

    @Test
    public void invalidGoBackRequest(){
        assertThrows(FSMTransitionFailedException.class, ()->currentState.handleAction(new BackAction("Paolo")));
        assertNull(currentState.getNextState());
    }

}
