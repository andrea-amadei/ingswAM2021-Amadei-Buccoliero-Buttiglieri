package it.polimi.ingsw;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.gamematerials.*;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.actions.BackAction;
import it.polimi.ingsw.model.actions.ConfirmAction;
import it.polimi.ingsw.model.actions.SelectCraftingAction;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.fsm.State;
import it.polimi.ingsw.model.fsm.states.CraftingResourceSelectionState;
import it.polimi.ingsw.model.fsm.states.CraftingState;
import it.polimi.ingsw.model.fsm.states.MenuState;
import it.polimi.ingsw.model.fsm.states.OutputSelectionState;
import it.polimi.ingsw.model.production.Crafting;
import it.polimi.ingsw.model.production.Production;
import it.polimi.ingsw.model.production.UpgradableCrafting;
import it.polimi.ingsw.server.ServerBuilder;
import it.polimi.ingsw.utils.ResourceLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CraftingStateTest {

    private GameContext gameContext;
    private State currentState;

    private final ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
    private final ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
    private final ResourceSingle shield = ResourceTypeSingleton.getInstance().getServantResource();
    private final ResourceSingle stone = ResourceTypeSingleton.getInstance().getStoneResource();

    @BeforeEach
    public void init() throws ParserException {

        List<String> usernames = Arrays.asList("Ernestino", "Geonna", "Alrigo");
        String config = ResourceLoader.loadFile("cfg/config.json");
        String crafting = ResourceLoader.loadFile("cfg/crafting.json");
        String faith = ResourceLoader.loadFile("cfg/faith.json");
        String leaders = ResourceLoader.loadFile("cfg/leaders.json");

        GameModel model  = ServerBuilder.buildModel(config, crafting, faith, leaders, usernames, false, new Random(3));

        gameContext = new GameContext(model, false);
        gameContext.setCurrentPlayer(model.getPlayerById("Ernestino"));
        currentState = new CraftingState(gameContext);

        //base crafting 0
        Map<ResourceType, Integer> input0 = new HashMap<>();
        Map<ResourceType, Integer> output0 = new HashMap<>();
        input0.put(ResourceTypeSingleton.getInstance().getAnyResource(), 2);
        output0.put(ResourceTypeSingleton.getInstance().getAnyResource(), 1);
        Crafting baseCrafting = new Crafting(input0, output0, 0);

        //crafting 1
        Map<ResourceType, Integer> input1 = new HashMap<>();
        Map<ResourceType, Integer> output1 = new HashMap<>();
        input1.put(gold, 2);
        output1.put(shield, 4);
        UpgradableCrafting crafting1 = new UpgradableCrafting(input1, output1, 0, 1);

        //crafting 2
        Map<ResourceType, Integer> input2 = new HashMap<>();
        Map<ResourceType, Integer> output2 = new HashMap<>();
        input2.put(gold, 1);
        input2.put(servant, 2);
        output2.put(shield, 2);
        output2.put(stone, 4);
        UpgradableCrafting crafting2 = new UpgradableCrafting(input2, output2, 1, 1);


        //Ernestino has 7 servants in his chest and 3 gold in his bottom shelf
        assertDoesNotThrow(()-> gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getStorage()
                .getChest().addResources(servant, 7));
        assertDoesNotThrow(()-> gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getStorage()
                .getCupboard().getShelfById("BottomShelf").addResources(gold, 3));

        //Ernestino has baseCrafting, crafting1 and crafting2
        assertDoesNotThrow(()-> gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getProduction()
                .addBaseCrafting(baseCrafting));
        assertDoesNotThrow(()-> gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getProduction()
                .setUpgradableCrafting(0, crafting1));
        assertDoesNotThrow(()-> gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getProduction()
        .setUpgradableCrafting(1, crafting2));

        //Ernestino has a base crafting with undecided output

        //Alrigo has 1 base crafting
        Map<ResourceType, Integer> input3 = new HashMap<>();
        Map<ResourceType, Integer> output3 = new HashMap<>();
        input3.put(ResourceTypeSingleton.getInstance().getGoldResource(), 2);
        output3.put(ResourceTypeSingleton.getInstance().getStoneResource(), 1);
        Crafting baseCrafting3 = new Crafting(input3, output3, 0);
        assertDoesNotThrow(()-> gameContext.getGameModel().getPlayerById("Alrigo").getBoard().getProduction()
                .addBaseCrafting(baseCrafting3));

    }

    //method "handleAction" successfully manages action "BackAction"
    @Test
    public void successfulBack(){
        try{
            currentState.handleAction(new BackAction("Ernestino"));
        }catch(FSMTransitionFailedException e){
            throw new RuntimeException();
        }

        assertTrue(currentState.getNextState() instanceof MenuState);
        assertFalse(gameContext.hasPlayerMoved());
    }

    //method "handleAction" throws NullPointerException if parameter "backAction" is null
    @Test
    public void nullBackAction(){
        assertThrows(NullPointerException.class, ()-> currentState.handleAction((BackAction) null));
    }

    //method "handleAction" throws FSMTransitionFailedException if it receives a "BackAction" from a player who is not
    //the current player
    @Test
    public void backRequestFromInvalidPlayer(){
        assertThrows(FSMTransitionFailedException.class, ()->currentState.handleAction(new BackAction("Alrigo")));
    }

    //method "handleAction" throws FSMTransitionFailedException if it receives a "BackAction" when crafting ready
    @Test
    public void backWhenCraftingReady(){
        assertDoesNotThrow(()-> gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getProduction()
        .selectCrafting(Production.CraftingType.UPGRADABLE, 0));
        gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getProduction().getCrafting(Production.CraftingType.UPGRADABLE, 0).setAllResourcesTransferred(true);

        assertThrows(FSMTransitionFailedException.class, ()-> currentState.handleAction(new BackAction("Ernestino")));
    }

    //method "handleAction" throws NullPointerException if parameter "confirmAction" is null
    @Test
    public void nullConfirmAction(){
        assertThrows(NullPointerException.class, ()-> currentState.handleAction((ConfirmAction) null));
    }

    //method "handleAction" throws FSMTransitionFailedException if it receives a "ConfirmAction" from a player who is not
    //the current player
    @Test
    public void confirmRequestFromInvalidPlayer(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Ernestino"));
        gameContext.getGameModel().getPlayerById("Alrigo").getBoard().getProduction().getCrafting(Production.CraftingType.BASE, 0).setAllResourcesTransferred(true);

        assertThrows(FSMTransitionFailedException.class, ()->currentState.handleAction(new ConfirmAction("Alrigo")));
    }

    //method "handleAction" throws NullPointerException if parameter "confirmAction" is null
    @Test
    public void nullSelectCraftingAction(){
        assertThrows(NullPointerException.class, ()-> currentState.handleAction((SelectCraftingAction) null));
    }

    //method "handleAction" throws FSMTransitionFailedException if it receives a "SelectCraftingAction" from a player who is not
    //the current player
    @Test
    public void selectCraftingFromInvalidPlayer(){
        assertThrows(FSMTransitionFailedException.class, ()-> currentState.handleAction(new SelectCraftingAction("Geonna",
                Production.CraftingType.UPGRADABLE, 0)));
    }

    //method "handleAction" successfully selects a crafting if the request comes from the current player and the crafting is
    //ad existing one
    @Test
    public void successfulSelectCrafting(){
        List<Message> messages;

        try{
            messages = currentState.handleAction(new SelectCraftingAction("Ernestino", Production.CraftingType.UPGRADABLE,
                    0));
        }catch(FSMTransitionFailedException e){
            throw new RuntimeException();
        }

        assertTrue(currentState.getNextState() instanceof CraftingResourceSelectionState);
        assertFalse(gameContext.hasPlayerMoved());
        assertTrue(messages.size() > 0);
    }

    //method "handleAction" successfully selects a crafting even when said crafting has an undecided output
    @Test
    public void successfulSelectCraftingWithUndecidedOutput(){
        List<Message> messages;

        try{
            messages = currentState.handleAction(new SelectCraftingAction("Ernestino", Production.CraftingType.BASE, 0));
        }catch(FSMTransitionFailedException e){
            throw new RuntimeException();
        }

        assertTrue(currentState.getNextState() instanceof OutputSelectionState);
        assertFalse(gameContext.hasPlayerMoved());
        assertTrue(messages.size() > 0);
    }

    //method "handleAction" throws FSMTransitionFailedException if it receives a "ConfirmAction" when there are no selected crafting
    @Test
    public void activatingProductionWithNoSelectedCrafting(){
        assertThrows(FSMTransitionFailedException.class, ()-> currentState.handleAction(new ConfirmAction("Ernestino")));
    }

    //TODO: test after testing activate production
    @Test
    public void activateProductionTriggersPopeCheck(){

    }

    //method "handleAction" successfully activates production if all requirements are met
    @Test
    public void successfulActivateProduction(){
        //if the player is here, it means they already moved
        gameContext.setPlayerMoved(true);
        //crafting1 is set as selected, as well as all required resources

        assertDoesNotThrow(()-> gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getStorage()
                .addToSelection(gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getStorage().getCupboard()
                        .getShelfById("BottomShelf"), gold, 2));
        assertDoesNotThrow(()->gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getProduction().getUpgradableCrafting(0)
        .setAllResourcesTransferred(true));

        //crafting2 is set as selected, as well as all required resources
        assertDoesNotThrow(()-> gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getProduction()
                .selectCrafting(Production.CraftingType.UPGRADABLE, 1));
        assertDoesNotThrow(()-> gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getStorage()
                .addToSelection(gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getStorage().getCupboard()
                        .getShelfById("BottomShelf"), gold, 1));
        assertDoesNotThrow(()-> gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getStorage()
                .addToSelection(gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getStorage()
                        .getChest(), servant, 2));

        assertDoesNotThrow(()->gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getProduction().getUpgradableCrafting(1)
                .setAllResourcesTransferred(true));


        List<Message> messages;

        try{
            messages = currentState.handleAction(new ConfirmAction("Ernestino"));
        }catch(FSMTransitionFailedException e){
            throw new RuntimeException();
        }

        assertTrue(currentState.getNextState() instanceof MenuState);
        assertTrue(gameContext.hasPlayerMoved());
        assertTrue(messages.size() > 0);

    }

    //method "onEntry" correctly sends messages
    @Test
    public void onEntryTest(){
        List<Message> messages;
        messages = currentState.onEntry();

        assertTrue(messages.size() > 0);
    }

    //method "onEntry" correctly sends messages when crafting ready
    @Test
    public void onEntryTestWhenCraftingReady(){
        gameContext.getCurrentPlayer().getBoard().getProduction().getCrafting(Production.CraftingType.UPGRADABLE, 0).setAllResourcesTransferred(true);
        List<Message> messages;
        messages = currentState.onEntry();

        assertTrue(messages.size() > 0);
    }

    //method "toString" returns the correct value
    @Test
    public void toStringTest(){
        assertEquals("CraftingState", currentState.toString());
    }

}
