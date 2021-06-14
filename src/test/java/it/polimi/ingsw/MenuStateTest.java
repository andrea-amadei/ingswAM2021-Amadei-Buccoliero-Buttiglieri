package it.polimi.ingsw;

import it.polimi.ingsw.common.ActionQueue;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.gamematerials.FlagColor;
import it.polimi.ingsw.gamematerials.LevelFlag;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.actions.*;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.fsm.State;
import it.polimi.ingsw.model.fsm.StateMachine;
import it.polimi.ingsw.model.fsm.states.*;
import it.polimi.ingsw.model.holder.FaithHolder;
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
public class MenuStateTest {

    private GameContext gameContext;
    private State currentState;

    private GameModel model;
    private ActionQueue actionQueue;
    private StateMachine stateMachine;

    private final ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
    private final ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
    private final ResourceSingle stone = ResourceTypeSingleton.getInstance().getStoneResource();

    @BeforeEach
    public void init() throws ParserException {

        List<String> usernames = Arrays.asList("Ernestino", "Trump", "Biden");
        String config = ResourceLoader.loadFile("cfg/config.json");
        String crafting = ResourceLoader.loadFile("cfg/crafting.json");
        String faith = ResourceLoader.loadFile("cfg/faith.json");
        String leaders = ResourceLoader.loadFile("cfg/leaders.json");

        model  = ServerBuilder.buildModel(config, crafting, faith, leaders, usernames, false, new Random(3));

        gameContext = new GameContext(model, false);
        currentState = new MenuState(gameContext);

        actionQueue = new ActionQueue();
        stateMachine = new StateMachine(actionQueue, gameContext, currentState);
        model.getFaithPath().setListener(stateMachine);

        Player player1 = model.getPlayerById("Ernestino");
        Player player2 = model.getPlayerById("Trump");

        //Ernestino has leader card ID 1 and ID 2
        player1.getBoard().addLeaderCard(model.getLeaderCards().get(0));
        player1.getBoard().addLeaderCard(model.getLeaderCards().get(1));

        //Ernestino has the required flags to activate leader ID 1
        player1.getBoard().getFlagHolder().addFlag(new LevelFlag(FlagColor.YELLOW, 1));
        player1.getBoard().getFlagHolder().addFlag(new LevelFlag(FlagColor.GREEN, 1));

        //Trump has 1 servant in his top shelf
        assertDoesNotThrow(()->player2.getBoard().getStorage().getCupboard().getShelfById("TopShelf")
                .addResources(servant, 1));


    }

    @Test
    public void successfulActivateLeader(){
        gameContext.setCurrentPlayer(model.getPlayerById("Ernestino"));

        try{
            currentState.handleAction(new ActivateLeaderAction("Ernestino", 1));
        }catch (FSMTransitionFailedException e){
            throw new RuntimeException(e.getMessage());
        }

        assertTrue(model.getPlayerById("Ernestino").getBoard().getLeaderCardByID(1).isActive());
        assertFalse(gameContext.hasPlayerMoved());
        assertEquals(1, model.getPlayerById("Ernestino").getBoard().getDiscountHolder().
                totalDiscountByResource(servant));
        assertTrue(currentState.getNextState() instanceof MenuState);
    }

    @Test
    public void activatingNonOwnedLeader(){
        gameContext.setCurrentPlayer(model.getPlayerById("Ernestino"));

        assertThrows(FSMTransitionFailedException.class, ()-> currentState.handleAction(new ActivateLeaderAction(
                "Ernestino", 4)));
    }

    @Test
    public void successfulDiscardLeader(){
        gameContext.setCurrentPlayer(model.getPlayerById("Ernestino"));

        try{
            currentState.handleAction(new DiscardLeaderAction("Ernestino", 2));
        }catch (FSMTransitionFailedException e){
            throw new RuntimeException(e.getMessage());
        }

        assertEquals(1, model.getPlayerById("Ernestino").getBoard().getFaithHolder().getFaithPoints());
        assertEquals(1, model.getPlayerById("Ernestino").getBoard().getLeaderCards().size());
        assertFalse(gameContext.hasPlayerMoved());
        assertTrue(currentState.getNextState() instanceof MenuState);
    }

    @Test
    public void discardingNonOwnedLeader(){
        gameContext.setCurrentPlayer(model.getPlayerById("Ernestino"));

        assertThrows(FSMTransitionFailedException.class, ()-> currentState.handleAction(new DiscardLeaderAction(
                "Ernestino", 5)));
    }

    @Test
    public void successfulResourceMove(){
        gameContext.setCurrentPlayer(model.getPlayerById("Trump"));

        try {
            currentState.handleAction(new ResourcesMoveAction("Trump", "TopShelf", "BottomShelf",
                    servant, 1));
        }catch (FSMTransitionFailedException e){
            throw new RuntimeException(e.getMessage());
        }

        assertTrue(currentState.getNextState() instanceof MenuState);
        assertFalse(gameContext.hasPlayerMoved());
        assertEquals(0, model.getPlayerById("Trump").getBoard().getStorage().getCupboard()
                    .getShelfById("TopShelf").getAmount());
        assertEquals(1, model.getPlayerById("Trump").getBoard().getStorage().getCupboard()
                .getShelfById("BottomShelf").getAmount());
    }

    @Test
    public void movingNonOwnedResource(){
        gameContext.setCurrentPlayer(model.getPlayerById("Trump"));

        assertThrows(FSMTransitionFailedException.class, ()-> currentState.handleAction(new ResourcesMoveAction(
                "Trump", "MiddleShelf", "BottomShelf", gold, 1)));
    }

    @Test
    public void successfulSelectPlayMarket(){
        gameContext.setCurrentPlayer(model.getPlayerById("Ernestino"));

        try{
            currentState.handleAction(new SelectPlayAction("Ernestino", SelectPlayAction.Play.MARKET));
        }catch (FSMTransitionFailedException e){
            throw new RuntimeException(e.getMessage());
        }

        assertTrue(currentState.getNextState() instanceof MarketState);
        assertFalse(gameContext.hasPlayerMoved());
    }

    @Test
    public void selectPlayMarketWhenAlreadyMoved(){
        gameContext.setCurrentPlayer(model.getPlayerById("Ernestino"));
        gameContext.setPlayerMoved(true);

        assertThrows(FSMTransitionFailedException.class, ()-> currentState.handleAction(new SelectPlayAction(
                "Ernestino", SelectPlayAction.Play.MARKET)));
    }

    @Test
    public void successfulSelectPlayCrafting(){
        gameContext.setCurrentPlayer(model.getPlayerById("Trump"));

        try{
            currentState.handleAction(new SelectPlayAction("Trump", SelectPlayAction.Play.CRAFTING));
        }catch (FSMTransitionFailedException e){
            throw new RuntimeException(e.getMessage());
        }

        assertTrue(currentState.getNextState() instanceof CraftingState);
        assertFalse(gameContext.hasPlayerMoved());
    }

    @Test
    public void selectPlayCraftingWhenAlreadyMoved(){
        gameContext.setCurrentPlayer(model.getPlayerById("Trump"));
        gameContext.setPlayerMoved(true);

        assertThrows(FSMTransitionFailedException.class, ()-> currentState.handleAction(new SelectPlayAction(
                "Trump", SelectPlayAction.Play.CRAFTING)));
    }

    @Test
    public void successfulSelectPlayShop(){
        gameContext.setCurrentPlayer(model.getPlayerById("Biden"));

        try{
            currentState.handleAction(new SelectPlayAction("Biden", SelectPlayAction.Play.SHOP));
        }catch (FSMTransitionFailedException e){
            throw new RuntimeException(e.getMessage());
        }

        assertTrue(currentState.getNextState() instanceof ShopState);
        assertFalse(gameContext.hasPlayerMoved());
    }

    @Test
    public void selectPlayShopWhenAlreadyMoved(){
        gameContext.setCurrentPlayer(model.getPlayerById("Biden"));
        gameContext.setPlayerMoved(true);

        assertThrows(FSMTransitionFailedException.class, ()-> currentState.handleAction(new SelectPlayAction(
                "Biden", SelectPlayAction.Play.SHOP)));
    }

    @Test
    public void successfulEndTurn(){
        gameContext.setCurrentPlayer(model.getPlayerById("Biden"));
        gameContext.setPlayerMoved(true);

        try{
            currentState.handleAction(new ConfirmAction("Biden"));
        }catch (FSMTransitionFailedException e){
            throw new RuntimeException(e.getMessage());
        }

        assertTrue(currentState.getNextState() instanceof EndTurnState);
    }

    @Test
    public void endingTurnWithoutMoving(){
        gameContext.setCurrentPlayer(model.getPlayerById("Biden"));
        gameContext.setPlayerMoved(false);

        assertThrows(FSMTransitionFailedException.class, ()-> currentState.handleAction(new ConfirmAction("Biden")));
    }

    @Test
    public void endingTurnWithFullHand(){
        gameContext.setCurrentPlayer(model.getPlayerById("Biden"));

        assertDoesNotThrow(()-> model.getPlayerById("Biden").getBoard().getStorage().getHand().
                addResources(stone, 1));

        assertThrows(FSMTransitionFailedException.class, ()-> currentState.handleAction(new ConfirmAction("Biden")));
    }

    @Test
    public void popeCheckOnDiscardLeader() throws InterruptedException {
        gameContext.setCurrentPlayer(model.getPlayerById("Ernestino"));
        gameContext.setPlayerMoved(true);

        //Ernestino is one faith point from activating the pope check
        model.getFaithPath().executeMovement(7, model.getPlayerById("Ernestino"));

        try{
            currentState.handleAction(new DiscardLeaderAction("Ernestino", 2));
        }catch (FSMTransitionFailedException e){
            throw new RuntimeException(e.getMessage());
        }

        Action popeAction = actionQueue.pop();
        assertTrue(popeAction instanceof PopeCheckAction);

        stateMachine.executeAction(popeAction);

        assertTrue(model.getPlayerById("Ernestino").getBoard().getFaithHolder().isPopeCardActive(0));
        assertEquals(FaithHolder.CheckpointStatus.INACTIVE, model.getPlayerById("Trump").getBoard().
                getFaithHolder().getPopeCardStatus(0));

    }

    @Test
    public void popeCheckEndsMatch() throws InterruptedException {
        gameContext.setCurrentPlayer(model.getPlayerById("Ernestino"));
        gameContext.setPlayerMoved(true);

        //Ernestino's first two pope checks are active
        model.getFaithPath().executeMovement(23, model.getPlayerById("Ernestino"));

        Action popeAction = actionQueue.pop();
        stateMachine.executeAction(popeAction);

        try{
            currentState.handleAction(new DiscardLeaderAction("Ernestino", 2));
        }catch (FSMTransitionFailedException e){
            throw new RuntimeException(e.getMessage());
        }

        Action popeAction1 = actionQueue.pop();
        stateMachine.executeAction(popeAction1);

        assertTrue(gameContext.hasCountdownStarted());
    }


}

