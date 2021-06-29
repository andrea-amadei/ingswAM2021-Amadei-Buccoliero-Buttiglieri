package it.polimi.ingsw;
import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.common.exceptions.ParserException;
import it.polimi.ingsw.server.model.basetypes.*;
import it.polimi.ingsw.server.model.global.GameModel;
import it.polimi.ingsw.server.model.actions.BackAction;
import it.polimi.ingsw.server.model.actions.ConfirmAction;
import it.polimi.ingsw.server.model.actions.SelectResourcesAction;
import it.polimi.ingsw.server.model.fsm.*;
import it.polimi.ingsw.server.model.fsm.states.MenuState;
import it.polimi.ingsw.server.model.fsm.states.ShopResourceSelectionState;
import it.polimi.ingsw.server.model.fsm.states.ShopState;
import it.polimi.ingsw.server.model.production.CraftingCard;
import it.polimi.ingsw.server.model.production.Production;
import it.polimi.ingsw.server.model.production.UpgradableCrafting;
import it.polimi.ingsw.server.model.storage.ResourceContainer;
import it.polimi.ingsw.server.ServerBuilder;
import it.polimi.ingsw.common.utils.ResourceLoader;
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
    private final ResourceSingle shield = ResourceTypeSingleton.getInstance().getShieldResource();
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
        assertDoesNotThrow(()-> gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getStorage()
                .getChest().addResources(shield, 1));

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

    //method "handleAction" throws NullPointerException if parameter "backAction" is null
    @Test
    public void nullBackAction(){
        assertThrows(NullPointerException.class, ()-> currentState.handleAction((BackAction) null));
    }

    //method "handleAction" successfully manages action "backAction" when the player has not previously selected any resources
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

    //method "handleAction" successfully manages action "backAction" and unselects previously selected resources
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

    //method "handleAction" throws FSMTransitionFailedException if the back request comes from a player who is not the current player
    @Test
    public void backRequestFromInvalidPlayer(){
        assertThrows(FSMTransitionFailedException.class, ()->currentState.handleAction(new BackAction("Augusta")));
    }

    //method "handleAction" throws NullPointerException if parameter "selectResourceAction" is null
    @Test
    public void nullSelectResourcesAction(){
        assertThrows(NullPointerException.class, ()-> currentState.handleAction((SelectResourcesAction) null));
    }

    //method "handleAction" throws NullPointerException if parameter "confirmAction" is null
    @Test
    public void nullConfirmAction(){
        assertThrows(NullPointerException.class, ()-> currentState.handleAction((ConfirmAction) null));
    }

    //method "handleAction" throws FSMTransitionFailedException if the confirm request comes from a player who is not the current player
    @Test
    public void confirmRequestFromInvalidPlayer(){
        assertThrows(FSMTransitionFailedException.class, ()->currentState.handleAction(new ConfirmAction("Augusta")));
    }

    //method "handleAction" throws FSMTransitionFailedException if the player has not selected any crafting slot
    @Test
    public void nonExistentProductionSlot(){
        gameContext.getCurrentPlayer().getBoard().getProduction().resetCraftingSelection();
        assertThrows(FSMTransitionFailedException.class, ()->currentState.handleAction(new ConfirmAction("Ernestino")));
    }

    //method "handleAction" throws FSMTransitionFailedException if the player has selected the wrong resources
    @Test
    public void wrongResourcesSelected(){
        try{
            currentState.handleAction(new SelectResourcesAction("Ernestino", "Chest", shield, 1));
        }catch(FSMTransitionFailedException e){
            throw new RuntimeException();
        }

        assertThrows(FSMTransitionFailedException.class, ()-> currentState.handleAction(new ConfirmAction("Ernestino")));
    }

    //method "handleAction" triggers the end sequence when a player is buying their seventh card
    @Test
    public void endTrigger(){
        for(int i = 0; i<=5; i++)
            gameContext.getCurrentPlayer().getBoard().getFlagHolder().addFlag(new LevelFlag(FlagColor.BLUE, 1));

        try{
            currentState.handleAction(new SelectResourcesAction("Ernestino", "Chest", gold, 2));
        }catch(FSMTransitionFailedException e){
            throw new RuntimeException();
        }

        try{
            currentState.handleAction(new SelectResourcesAction("Ernestino", "Chest", servant, 2));
        }catch(FSMTransitionFailedException e){
            throw new RuntimeException();
        }

        try{
            currentState.handleAction(new ConfirmAction("Ernestino"));
        }catch(FSMTransitionFailedException e){
            throw new RuntimeException();
        }

        assertTrue(gameContext.hasCountdownStarted());
    }

    //method "handleAction" successfully manages action "confirmAction", effectively buying from the shop
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

    //method "handleAction" successfully manages action "confirmAction", effectively buying from the shop and applying the correct discounts
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

    //method "handleAction" successfully manages action "confirmAction", effectively buying from the shop and applying
    // the correct discounts, even when said discounts result in the selected card being free
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

    //method "handleAction" throws FSMTransitionFailedException if the player tries to buy a card without selecting
    // all the resources needed to buy it
    @Test
    public void failedBuyDueToInsufficientFunds(){
        try{
            currentState.handleAction(new SelectResourcesAction("Ernestino", "Chest", gold, 2));
        }catch(FSMTransitionFailedException e){
            throw new RuntimeException();
        }

        assertThrows(FSMTransitionFailedException.class, ()-> currentState.handleAction(new ConfirmAction("Ernestino")));
    }

    //method "handleAction" successfully selects resources if said resources are owned by the player
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

    //method "handleAction" throws FSMTransitionFailedException if the player tries to select resources they do not own
    @Test
    public void selectingNonOwnedResources(){
        assertThrows(FSMTransitionFailedException.class, ()-> currentState.handleAction(new SelectResourcesAction("Ernestino", "Chest", gold, 8)));
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
        assertEquals("ShopResourceSelectionState", currentState.toString());
    }
}
