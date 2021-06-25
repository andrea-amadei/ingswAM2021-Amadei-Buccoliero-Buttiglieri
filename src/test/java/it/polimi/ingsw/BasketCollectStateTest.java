package it.polimi.ingsw;
import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.actions.ConfirmAction;
import it.polimi.ingsw.model.actions.MoveFromBasketToShelfAction;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.fsm.State;
import it.polimi.ingsw.model.fsm.states.BasketCollectState;
import it.polimi.ingsw.model.fsm.states.MenuState;
import it.polimi.ingsw.server.ServerBuilder;
import it.polimi.ingsw.utils.ResourceLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BasketCollectStateTest {

    private GameContext gameContext;
    private State currentState;

    private final ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
    private final ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
    private final ResourceSingle shield = ResourceTypeSingleton.getInstance().getShieldResource();

    @BeforeEach
    public void init() throws ParserException {

        List<String> usernames = Arrays.asList("Ernestino", "Ottolengo", "Nunzio");
        String config = ResourceLoader.loadFile("cfg/config.json");
        String crafting = ResourceLoader.loadFile("cfg/crafting.json");
        String faith = ResourceLoader.loadFile("cfg/faith.json");
        String leaders = ResourceLoader.loadFile("cfg/leaders.json");

        GameModel model  = ServerBuilder.buildModel(config, crafting, faith, leaders, usernames, false, new Random(3));

        gameContext = new GameContext(model, false);
        gameContext.setCurrentPlayer(model.getPlayerById("Ernestino"));
        currentState = new BasketCollectState(gameContext);

        //Ernestino has 2 gold and one shield in basket. 1 servant in middle shelf
        assertDoesNotThrow(()-> gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getStorage()
                .getMarketBasket().addResources(gold, 2));
        assertDoesNotThrow(()-> gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getStorage()
                .getMarketBasket().addResources(shield, 1));
        assertDoesNotThrow(()-> gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getStorage()
                .getCupboard().getShelfById("MiddleShelf").addResources(servant, 1));
    }

    //method "handleAction" throws NullPointerException if parameter "moveFromBasketToShelfAction" is null
    @Test
    public void nullMoveFromBasketToShelfAction(){
        assertThrows(NullPointerException.class, ()-> currentState.handleAction((MoveFromBasketToShelfAction) null));
    }

    //method "handleAction" throws FSMTransitionFailedException if the player tries to collect resources they do not own
    @Test
    public void wrongResources(){
        assertThrows(FSMTransitionFailedException.class, ()-> currentState.handleAction(new MoveFromBasketToShelfAction("Ernestino",
                servant, 1, "MiddleShelf")));
    }

    //method "handleAction" throws NullPointerException if parameter "confirmAction" is null
    @Test
    public void nullConfirmAction(){
        assertThrows(NullPointerException.class, ()-> currentState.handleAction((ConfirmAction) null));
    }

    //method "handleAction" correctly moves all resources in market basket to a player's shelves
    @Test
    public void moveAllResourcesToShelf(){
        List<Message> messages, messages1;
        try{
            messages = currentState.handleAction(new MoveFromBasketToShelfAction("Ernestino", gold, 2, "BottomShelf"));
            messages1 = currentState.handleAction(new MoveFromBasketToShelfAction("Ernestino", shield, 1, "TopShelf"));
        } catch (FSMTransitionFailedException e){
            throw new RuntimeException();
        }

        //messages are sent
        assertTrue(messages.size() > 0);
        assertTrue(messages1.size() > 0);

        //next state is correct
        assertTrue(currentState.getNextState() instanceof BasketCollectState);

        //basket is emptied
        assertEquals(gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getStorage().getMarketBasket()
                .totalAmountOfResources(), 0);

        //resources are correctly stored
        assertEquals(2, gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getStorage()
                .getCupboard().getShelfById("BottomShelf").getAmount());
        assertEquals(gold, gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getStorage()
                .getCupboard().getShelfById("BottomShelf").getCurrentType());
    }


    //method "handleAction" correctly moves some resources in market basket to a player's shelf. Then faith points are
    // assigned to other players
    @Test
    public void moveSomeResourcesToShelfAndConfirm(){
        List<Message> messages, messages1;
        try{
            messages = currentState.handleAction(new MoveFromBasketToShelfAction("Ernestino", gold, 2, "BottomShelf"));
            messages1 = currentState.handleAction(new ConfirmAction("Ernestino"));
        }catch (FSMTransitionFailedException e){
            throw new RuntimeException();
        }

        //messages are sent
        assertTrue(messages.size() > 0);
        assertTrue(messages1.size() > 0);

        //next state is correct
        assertTrue(currentState.getNextState() instanceof MenuState);

        //basket is emptied
        assertEquals(gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getStorage().getMarketBasket()
                .totalAmountOfResources(), 0);

        //faith points are correctly assigned to other players
        assertEquals(1, gameContext.getGameModel().getPlayerById("Ottolengo").getBoard()
        .getFaithHolder().getFaithPoints());

    }

    //After having moved resources, action "confirm" is handled correctly
    @Test
    public void moveAllResourcesAndConfirm(){
        List<Message> messages, messages1, messages2;
        try{
            messages = currentState.handleAction(new MoveFromBasketToShelfAction("Ernestino", gold, 2, "BottomShelf"));
            messages1 = currentState.handleAction(new MoveFromBasketToShelfAction("Ernestino", shield, 1, "TopShelf"));
            messages2 = currentState.handleAction(new ConfirmAction("Ernestino"));
        } catch (FSMTransitionFailedException e){
            throw new RuntimeException();
        }

        //messages are sent
        assertTrue(messages.size() > 0);
        assertTrue(messages1.size() > 0);
        assertTrue(messages2.size() > 0);

        //next state is correct
        assertTrue(currentState.getNextState() instanceof MenuState);

        //no faith points are assigned to other players
        assertEquals(0, gameContext.getGameModel().getPlayerById("Ottolengo").getBoard()
                .getFaithHolder().getFaithPoints());
    }

    //method "handleAction" throws FSMTransitionFailedException if the action "confirm" comes from a player who is not the current player
    @Test
    public void confirmFromWrongPlayer(){
        assertThrows(FSMTransitionFailedException.class, ()-> currentState.handleAction(new ConfirmAction("Nunzio")));
    }

    //method "handleAction" correctly handles action "confirm" even if the player has not collected any resource from the
    // market basket. Then faith points are assigned to other players
    @Test
    public void confirmWithoutTidying(){
        List<Message> messages;
        try{
            messages = currentState.handleAction(new ConfirmAction("Ernestino"));
        }catch (FSMTransitionFailedException e){
            throw new RuntimeException();
        }

        //messages are sent
        assertTrue(messages.size() > 0);

        //next state is correct
        assertTrue(currentState.getNextState() instanceof MenuState);

        //cupboard is untouched
        assertEquals(0, gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getStorage()
                .getCupboard().getShelfById("TopShelf").getAmount());
        assertEquals(1, gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getStorage()
                .getCupboard().getShelfById("MiddleShelf").getAmount());
        assertEquals(0, gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getStorage()
                .getCupboard().getShelfById("BottomShelf").getAmount());

        //correct amount of faith points is assigned to other players
        assertEquals(3, gameContext.getGameModel().getPlayerById("Nunzio").getBoard()
                .getFaithHolder().getFaithPoints());
    }

    //method "onEntry" correctly sends messages
    @Test
    public void onEntryTest(){
        List<Message> messages;
        messages = currentState.onEntry();

        assertTrue(messages.size() > 0);
    }

    //method "toString" returns the correct value
    @Test
    public void toStringTest(){
        assertEquals("BasketCollectState", currentState.toString());
    }


}
