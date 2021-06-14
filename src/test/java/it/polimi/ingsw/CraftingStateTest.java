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

    }

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

    @Test
    public void backRequestFromInvalidPlayer(){
        assertThrows(FSMTransitionFailedException.class, ()->currentState.handleAction(new BackAction("Alrigo")));
    }

    //TODO: prevent player from choosing back action when resources are selected
    /*
    @Test
    public void backWhenCraftingReady(){
        assertDoesNotThrow(()-> gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getProduction()
        .selectCrafting(Production.CraftingType.UPGRADABLE, 0));
        assertDoesNotThrow(()-> gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getStorage()
        .addToSelection(gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getStorage().getCupboard()
        .getShelfById("BottomShelf"), gold, 2));

        assertThrows(FSMTransitionFailedException.class, ()-> currentState.handleAction(new BackAction("Ernestino")));
    }

     */

    @Test
    public void selectCraftingFromInvalidPlayer(){
        assertThrows(FSMTransitionFailedException.class, ()-> currentState.handleAction(new SelectCraftingAction("Geonna",
                Production.CraftingType.UPGRADABLE, 0)));
    }

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

    @Test
    public void activatingProductionWithNoSelectedCrafting(){
        assertThrows(FSMTransitionFailedException.class, ()-> currentState.handleAction(new ConfirmAction("Ernestino")));
    }

    //TODO: test after testing activate production
    @Test
    public void activateProductionTriggersPopeCheck(){

    }

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

}
