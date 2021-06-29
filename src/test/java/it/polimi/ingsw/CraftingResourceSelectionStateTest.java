package it.polimi.ingsw;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.common.exceptions.ParserException;
import it.polimi.ingsw.server.model.basetypes.ResourceSingle;
import it.polimi.ingsw.server.model.basetypes.ResourceType;
import it.polimi.ingsw.server.model.basetypes.ResourceTypeSingleton;
import it.polimi.ingsw.server.model.global.GameModel;
import it.polimi.ingsw.server.model.actions.BackAction;
import it.polimi.ingsw.server.model.actions.ConfirmAction;
import it.polimi.ingsw.server.model.actions.SelectResourcesAction;
import it.polimi.ingsw.server.model.fsm.GameContext;
import it.polimi.ingsw.server.model.fsm.State;
import it.polimi.ingsw.server.model.fsm.states.CraftingResourceSelectionState;
import it.polimi.ingsw.server.model.fsm.states.CraftingState;
import it.polimi.ingsw.server.model.production.Production;
import it.polimi.ingsw.server.model.production.UpgradableCrafting;
import it.polimi.ingsw.server.ServerBuilder;
import it.polimi.ingsw.common.utils.ResourceLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CraftingResourceSelectionStateTest {

    private GameContext gameContext;
    private State currentState;

    private final ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
    private final ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();

    @BeforeEach
    public void init() throws ParserException {

        List<String> usernames = Arrays.asList("Ernestino", "Brigitta", "Pantaleone");
        String config = ResourceLoader.loadFile("cfg/config.json");
        String crafting = ResourceLoader.loadFile("cfg/crafting.json");
        String faith = ResourceLoader.loadFile("cfg/faith.json");
        String leaders = ResourceLoader.loadFile("cfg/leaders.json");

        GameModel model  = ServerBuilder.buildModel(config, crafting, faith, leaders, usernames, false, new Random(3));

        gameContext = new GameContext(model, false);
        gameContext.setCurrentPlayer(model.getPlayerById("Ernestino"));
        currentState = new CraftingResourceSelectionState(gameContext);


        //crafting 1 (Undecided output)
        Map<ResourceType, Integer> input1 = new HashMap<>();
        Map<ResourceType, Integer> output1 = new HashMap<>();
        input1.put(gold, 2);
        output1.put(ResourceTypeSingleton.getInstance().getAnyResource(), 4);
        UpgradableCrafting crafting1 = new UpgradableCrafting(input1, output1, 0, 1);

        //Ernestino has 3 gold in his bottom shelf
        assertDoesNotThrow(()-> gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getStorage()
                .getCupboard().getShelfById("BottomShelf").addResources(gold, 3));

        //Ernestino has 1 servant in his top shelf
        assertDoesNotThrow(()-> gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getStorage()
                .getCupboard().getShelfById("TopShelf").addResources(servant, 1));

        //Ernestino has crafting1
        assertDoesNotThrow(()-> gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getProduction()
                .setUpgradableCrafting(0, crafting1));

        //Ernestino has selected crafting1
        assertDoesNotThrow(()->gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getProduction()
                .selectCrafting(Production.CraftingType.UPGRADABLE, 0));

        //Ernestino has selected 4 servants as output
        assertDoesNotThrow(()-> gameContext.getGameModel().getPlayerById("Ernestino").getBoard()
        .getProduction().getSelectedCrafting().setGroupConversion(ResourceTypeSingleton.getInstance().getAnyResource(),
                        new HashMap<>(){{put(servant, 4);}}));
    }

    //method "handleAction" throws NullPointerException" if parameter "backAction" is null
    @Test
    public void nullBackAction(){
        assertThrows(NullPointerException.class, ()-> currentState.handleAction((BackAction)null));
    }

    //method "handleAction" throws NullPointerException" if parameter "selectResourcesAction" is null
    @Test
    public void nullSelectResourcesAction(){
        assertThrows(NullPointerException.class, ()-> currentState.handleAction((SelectResourcesAction) null));
    }

    //method "handleAction" throws NullPointerException" if parameter "confirmAction" is null
    @Test
    public void nullConfirmAction(){
        assertThrows(NullPointerException.class, ()-> currentState.handleAction((ConfirmAction)null));
    }

    //method "handleAction" successfully manages action "BackAction"
    @Test
    public void successfulBack(){
        try{
            currentState.handleAction(new BackAction("Ernestino"));
        }catch(FSMTransitionFailedException e){
            throw new RuntimeException();
        }

        assertTrue(currentState.getNextState() instanceof CraftingState);
        assertFalse(gameContext.hasPlayerMoved());
    }

    //method "handleAction" throws FSMTransitionFailedException if the "BackAction" target is not the corrent player
    @Test
    public void backRequestFromInvalidPlayer(){
        assertThrows(FSMTransitionFailedException.class, ()-> currentState.handleAction(new BackAction("Brigitta")));
    }

    //method "handleAction" successfully menages action "SelectResourcesAction"
    @Test
    public void successfulSelectResources(){

        try{
            currentState.handleAction(new SelectResourcesAction("Ernestino",
                    "BottomShelf", gold, 2));
        }catch(FSMTransitionFailedException e){
            throw new RuntimeException();
        }

        assertTrue(currentState.getNextState() instanceof CraftingResourceSelectionState);
        assertFalse(gameContext.hasPlayerMoved());
    }

    //method "handleAction" throws FSMTransitionFailedException if trying to select non owned resources
    @Test
    public void invalidSelectResource(){
        assertThrows(FSMTransitionFailedException.class, ()-> currentState.handleAction(
                new SelectResourcesAction("Ernestino", "BottomShelf", servant, 1)));
    }

    //method "handleAction" successfully menages action "confirmAction" with crafting with undecided output
    @Test
    public void successfulConfirmWithUndecidedOutput(){
        List<Message> messages;

        try{
            currentState.handleAction(new SelectResourcesAction("Ernestino",
                    "BottomShelf", gold, 2));
        }catch(FSMTransitionFailedException e){
            throw new RuntimeException();
        }

        try{
            messages = currentState.handleAction(new ConfirmAction("Ernestino"));
        }catch(FSMTransitionFailedException e){
            throw new RuntimeException();
        }

        assertTrue(gameContext.hasPlayerMoved());
        assertTrue(currentState.getNextState() instanceof CraftingState);
        assertEquals(1, gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getStorage()
                .getCupboard().getShelfById("BottomShelf").getAmount());
        assertNull(gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getStorage()
        .getSelection());
        assertTrue(messages.size() > 0);
    }

    //method "handleAction" throws FSMTransitionFailedException if it receives a "confirmAction" without having
    //selected any resources
    @Test
    public void confirmWithNoSelectedResources(){
        assertThrows(FSMTransitionFailedException.class,
                ()->currentState.handleAction(new ConfirmAction("Ernestino")));
    }

    //method "handleAction" throws FSMTransitionFailedException if it receives a "confirmAction" with wrong resources
    //selected
    @Test
    public void confirmWithWrongResourcesSelected(){
        try{
            currentState.handleAction(new SelectResourcesAction("Ernestino",
                    "TopShelf", servant, 1));
        }catch(FSMTransitionFailedException e){
            throw new RuntimeException();
        }

        assertThrows(FSMTransitionFailedException.class,
                ()->currentState.handleAction(new ConfirmAction("Ernestino")));
    }

    //method "handleAction" throws FSMTransitionFailedException if it receives a "confirmAction" with too few resource
    //selected
    @Test
    public void confirmWithTooFewResourcesSelected(){
        try{
            currentState.handleAction(new SelectResourcesAction("Ernestino",
                    "BottomShelf", gold, 1));
        }catch(FSMTransitionFailedException e){
            throw new RuntimeException();
        }

        assertThrows(FSMTransitionFailedException.class,
                ()->currentState.handleAction(new ConfirmAction("Ernestino")));
    }

    //method "handleAction" throws FSMTransitionFailedException if it receives a "ConfirmAction" from the wrong player
    @Test
    public void confirmFromInvalidPlayer(){
        assertThrows(FSMTransitionFailedException.class,
                ()->currentState.handleAction(new ConfirmAction("Brigitta")));
    }

    //method "handleAction" throws FSMTransitionFailedException if it receives a "ConfirmAction" when no resources are
    //selected
    @Test
    public void confirmWithNoSelectedCrafting(){
        gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getProduction().resetCraftingSelection();

        assertThrows(FSMTransitionFailedException.class,
                ()->currentState.handleAction(new ConfirmAction("Ernestino")));
    }

    //method "onEntry" correctly sends messages
    @Test
    public void onEntrySendMessages(){
        List<Message> messages;
        messages = currentState.onEntry();

        assertTrue(messages.size() > 0);
    }

}
