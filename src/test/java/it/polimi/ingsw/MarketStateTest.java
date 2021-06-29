package it.polimi.ingsw;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.common.exceptions.ParserException;
import it.polimi.ingsw.server.model.basetypes.MarbleColor;
import it.polimi.ingsw.server.model.basetypes.ResourceSingle;
import it.polimi.ingsw.server.model.basetypes.ResourceTypeSingleton;
import it.polimi.ingsw.server.model.global.GameModel;
import it.polimi.ingsw.server.model.global.Player;
import it.polimi.ingsw.server.model.actions.BackAction;
import it.polimi.ingsw.server.model.actions.BuyFromMarketAction;
import it.polimi.ingsw.server.model.fsm.*;
import it.polimi.ingsw.server.model.fsm.states.ConversionSelectionState;
import it.polimi.ingsw.server.model.fsm.states.MarketState;
import it.polimi.ingsw.server.model.fsm.states.MenuState;
import it.polimi.ingsw.server.model.market.ConversionActuator;
import it.polimi.ingsw.server.ServerBuilder;
import it.polimi.ingsw.common.utils.ResourceLoader;
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

    @BeforeEach
    public void init() throws ParserException {

        List<String> usernames = Arrays.asList("Ernestino", "Genoveffa", "Gertrude");
        String config = ResourceLoader.loadFile("cfg/config.json");
        String crafting = ResourceLoader.loadFile("cfg/crafting.json");
        String faith = ResourceLoader.loadFile("cfg/faith.json");
        String leaders = ResourceLoader.loadFile("cfg/leaders.json");

        GameModel model  = ServerBuilder.buildModel(config, crafting, faith, leaders, usernames, false, new Random(3));

        Player player2 = model.getPlayerById("Genoveffa");
        player2.getBoard().getConversionHolder().addConversionActuator(MarbleColor.WHITE,
                new ConversionActuator(Collections.singletonList(servant), 0));

        player2.getBoard().getConversionHolder().addConversionActuator(MarbleColor.WHITE,
                new ConversionActuator(Collections.singletonList(gold), 0));
        gameContext = new GameContext(model, false);
        gameContext.setCurrentPlayer(model.getPlayerById("Genoveffa"));
        currentState = new MarketState(gameContext);
    }

    //method "handleAction" throws NullPointerException if the parameter "buyFromMarketAction" is null
    @Test
    public void nullBuyFromMarket(){
        assertThrows(NullPointerException.class, ()-> currentState.handleAction((BuyFromMarketAction) null));
    }

    //method "handleAction" successfully manages a "buyFromMarket" action
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

    //method "handleAction" throws FSMTransitionFailedException if the player tries to buy a non existent row or col
    @Test
    public void invalidBuyFromMarketTransition(){
        assertThrows(FSMTransitionFailedException.class, ()->currentState.handleAction(new BuyFromMarketAction("Genoveffa", true, 5)));
        assertNull(currentState.getNextState());
        assertEquals(new ArrayList<>(), gameContext.getGameModel().getMarket().getSelectedMarbles());
    }

    //method "handleAction" successfully manages a "back" action
    @Test
    public void validGoBackRequest(){
        try{
            currentState.handleAction(new BackAction("Genoveffa"));
        }catch(FSMTransitionFailedException e){
            throw new RuntimeException();
        }

        assertTrue(currentState.getNextState() instanceof MenuState);
        assertFalse(gameContext.hasPlayerMoved());
    }

    //method "handleAction" throws FSMTransitionFailedException if the back request comes from a player who is not the current player
    @Test
    public void invalidGoBackRequest(){
        assertThrows(FSMTransitionFailedException.class, ()->currentState.handleAction(new BackAction("Paolo")));
        assertNull(currentState.getNextState());
    }

    //method "handleAction" throws NullPointerException if the parameter "backAction" is null
    @Test
    public void nullBack(){
        assertThrows(NullPointerException.class, ()-> currentState.handleAction((BackAction) null));
    }

    //method "onEntry" successfully sends messages
    @Test
    public void onEntryTest(){
        List<Message> messages;
        messages = currentState.onEntry();

        assertTrue(messages.size() > 0);
    }

    //method "toString" returns the correct value
    @Test
    public void toStringTest(){
        assertEquals("MarketState", currentState.toString());
    }

}
