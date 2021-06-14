package it.polimi.ingsw;
import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.gamematerials.*;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.actions.BackAction;
import it.polimi.ingsw.model.actions.ConfirmAction;
import it.polimi.ingsw.model.actions.SelectResourcesAction;
import it.polimi.ingsw.model.fsm.*;
import it.polimi.ingsw.model.fsm.states.MenuState;
import it.polimi.ingsw.model.fsm.states.ShopResourceSelectionState;
import it.polimi.ingsw.model.fsm.states.ShopState;
import it.polimi.ingsw.model.production.CraftingCard;
import it.polimi.ingsw.model.production.Production;
import it.polimi.ingsw.model.production.UpgradableCrafting;
import it.polimi.ingsw.model.storage.ResourceContainer;
import it.polimi.ingsw.server.ServerBuilder;
import it.polimi.ingsw.utils.ResourceLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ShopResourceSelectionStateTest {

    private GameContext gameContext;
    private State currentState;

    private final ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
    private final ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
    private final ResourceSingle shield = ResourceTypeSingleton.getInstance().getServantResource();
    private CraftingCard craftingCard1;

    @BeforeEach
    public void init() throws ParserException {

        List<String> usernames = Arrays.asList("Ernestino", "Mariola", "Augusta");
        String config = ResourceLoader.loadFile("cfg/config.json");
        String crafting = ResourceLoader.loadFile("cfg/crafting.json");
        String faith = ResourceLoader.loadFile("cfg/faith.json");
        String leaders = ResourceLoader.loadFile("cfg/leaders.json");

        GameModel model  = ServerBuilder.buildModel(config, crafting, faith, leaders, usernames, false, new Random(3));

        gameContext = new GameContext(model, false);
        gameContext.setCurrentPlayer(model.getPlayerById("Ernestino"));
        currentState = new ShopResourceSelectionState(gameContext);

        assertDoesNotThrow(()-> gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getStorage()
                .getChest().addResources(gold, 5));
        assertDoesNotThrow(()-> gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getStorage()
                .getChest().addResources(servant, 7));

        LevelFlag levelFlagOfCard1 = new LevelFlag(FlagColor.YELLOW, 1);
        HashMap<ResourceSingle, Integer> costOfCard1 = new HashMap<>();
        costOfCard1.put(gold, 2);
        costOfCard1.put(servant, 2);
        HashMap<ResourceType, Integer> inputOfCard1 = new HashMap<>();
        HashMap<ResourceType, Integer> outputOfCard1 = new HashMap<>();
        inputOfCard1.put(shield, 2);
        outputOfCard1.put(gold, 4);
        UpgradableCrafting craftingOfCard1 = new UpgradableCrafting(inputOfCard1, outputOfCard1,1, 1);
        craftingCard1 = new CraftingCard(1, levelFlagOfCard1, costOfCard1, craftingOfCard1, 5);
        gameContext.getGameModel().getShop().addCard(craftingCard1);
        //crafting card 1 has already been selected (In previous state)
        assertDoesNotThrow(()-> gameContext.getGameModel().getShop().selectCard(0, 3));
        assertDoesNotThrow(()-> gameContext.getCurrentPlayer().getBoard().getProduction().selectCrafting(Production.CraftingType.BASE, 0));
    }

    @Test
    public void successfulBack(){
        try{
            currentState.handleAction(new BackAction("Ernestino"));
        }catch(FSMTransitionFailedException e){
            throw new RuntimeException();
        }

        assertTrue(currentState.getNextState() instanceof ShopState);
        assertFalse(gameContext.hasPlayerMoved());
    }

    @Test
    public void successfulBackAfterOneResourceSelection(){
        try{
            currentState.handleAction(new SelectResourcesAction("Ernestino", "Chest", gold, 2));
        }catch(FSMTransitionFailedException e){
            throw new RuntimeException();
        }
        try{
            currentState.handleAction(new BackAction("Ernestino"));
        }catch(FSMTransitionFailedException e){
            throw new RuntimeException();
        }

        assertTrue(currentState.getNextState() instanceof ShopState);
        assertFalse(gameContext.hasPlayerMoved());
        assertNull(gameContext.getCurrentPlayer().getBoard().getStorage().getSelection());
    }

    @Test
    public void backRequestFromInvalidPlayer(){
        assertThrows(FSMTransitionFailedException.class, ()->currentState.handleAction(new BackAction("Augusta")));
    }

    @Test
    public void successfulBuyFromShop(){
        List<Message> messages1, messages2;
        try{
            messages1 = currentState.handleAction(new SelectResourcesAction("Ernestino", "Chest", gold, 2));
        }catch(FSMTransitionFailedException e){
            throw new RuntimeException();
        }
        try{
            messages2 = currentState.handleAction(new SelectResourcesAction("Ernestino", "Chest", servant, 2));
        }catch(FSMTransitionFailedException e){
            throw new RuntimeException();
        }
        try{
            currentState.handleAction(new ConfirmAction("Ernestino"));
        }catch(FSMTransitionFailedException e){
            throw new RuntimeException();
        }

        assertTrue(messages1.size() > 0);
        assertTrue(messages2.size() > 0);
        assertTrue(currentState.getNextState() instanceof MenuState);
        assertTrue(gameContext.hasPlayerMoved());
        assertNull(gameContext.getCurrentPlayer().getBoard().getStorage().getSelection());
        assertNull(gameContext.getGameModel().getShop().getSelectedCard());
        assertEquals(craftingCard1.getCrafting(), gameContext.getCurrentPlayer().getBoard().getProduction().getUpgradableCrafting(0));
    }

    @Test
    public void successfulBuyFromShopWithDiscounts(){

        List<Message> messages2;

        try{
            messages2 = currentState.handleAction(new SelectResourcesAction("Ernestino", "Chest", servant, 2));
        }catch(FSMTransitionFailedException e){
            throw new RuntimeException();
        }

        gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getDiscountHolder().addDiscount(gold, 2);

        try{
            currentState.handleAction(new ConfirmAction("Ernestino"));
        }catch(FSMTransitionFailedException e){
            e.printStackTrace(System.out);
            throw new RuntimeException();
        }

        assertTrue(messages2.size() > 0);
        assertTrue(currentState.getNextState() instanceof MenuState);
        assertTrue(gameContext.hasPlayerMoved());
        assertNull(gameContext.getCurrentPlayer().getBoard().getStorage().getSelection());
        assertNull(gameContext.getGameModel().getShop().getSelectedCard());
        assertEquals(craftingCard1.getCrafting(), gameContext.getCurrentPlayer().getBoard().getProduction().getUpgradableCrafting(0));
    }

    @Test
    public void successfulBuyFromShopFreeCard(){

        gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getDiscountHolder().addDiscount(gold, 2);
        gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getDiscountHolder().addDiscount(servant, 2);

        try{
            currentState.handleAction(new ConfirmAction("Ernestino"));
        }catch(FSMTransitionFailedException e){
            e.printStackTrace(System.out);
            throw new RuntimeException();
        }

        assertTrue(currentState.getNextState() instanceof MenuState);
        assertTrue(gameContext.hasPlayerMoved());
        assertNull(gameContext.getCurrentPlayer().getBoard().getStorage().getSelection());
        assertNull(gameContext.getGameModel().getShop().getSelectedCard());
        assertEquals(craftingCard1.getCrafting(), gameContext.getCurrentPlayer().getBoard().getProduction().getUpgradableCrafting(0));
    }



    @Test
    public void failedBuyDueToInsufficientFunds(){
        try{
            currentState.handleAction(new SelectResourcesAction("Ernestino", "Chest", gold, 2));
        }catch(FSMTransitionFailedException e){
            throw new RuntimeException();
        }

        assertThrows(FSMTransitionFailedException.class, ()-> currentState.handleAction(new ConfirmAction("Ernestino")));
    }

    @Test
    public void successfulResourceSelect(){
        List<Message> messages1;
        try{
            messages1 = currentState.handleAction(new SelectResourcesAction("Ernestino", "Chest", gold, 2));
        }catch(FSMTransitionFailedException e){
            throw new RuntimeException();
        }
        Map<ResourceContainer, Map<ResourceSingle, Integer>> expectedSelected = new HashMap<>(){
            {
                put(gameContext.getCurrentPlayer().getBoard().getStorage().getChest(), new HashMap<>(){{put(gold, 2);}});
            }
        };

        assertTrue(messages1.size() > 0);
        assertTrue(currentState.getNextState() instanceof ShopResourceSelectionState);
        assertFalse(gameContext.hasPlayerMoved());
        assertEquals(expectedSelected,gameContext.getCurrentPlayer().getBoard().getStorage().getSelection());
    }

    @Test
    public void selectingNonOwnedResources(){
        assertThrows(FSMTransitionFailedException.class, ()-> currentState.handleAction(new SelectResourcesAction("Ernestino", "Chest", gold, 8)));
    }
}
