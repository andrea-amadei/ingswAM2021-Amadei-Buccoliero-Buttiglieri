package it.polimi.ingsw;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.gamematerials.MarbleColor;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.actions.BackAction;
import it.polimi.ingsw.model.actions.BuyFromMarketAction;
import it.polimi.ingsw.model.fsm.*;
import it.polimi.ingsw.model.fsm.states.ConversionSelectionState;
import it.polimi.ingsw.model.fsm.states.MarketState;
import it.polimi.ingsw.model.fsm.states.MenuState;
import it.polimi.ingsw.model.market.ConversionActuator;
import it.polimi.ingsw.server.ServerBuilder;
import it.polimi.ingsw.utils.ResourceLoader;
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
    public void invalidBuyFromMarketTransition(){
        assertThrows(FSMTransitionFailedException.class, ()->currentState.handleAction(new BuyFromMarketAction("Genoveffa", true, 5)));
        assertNull(currentState.getNextState());
        assertEquals(new ArrayList<>(), gameContext.getGameModel().getMarket().getSelectedMarbles());
    }

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

    @Test
    public void invalidGoBackRequest(){
        assertThrows(FSMTransitionFailedException.class, ()->currentState.handleAction(new BackAction("Paolo")));
        assertNull(currentState.getNextState());
    }

}
