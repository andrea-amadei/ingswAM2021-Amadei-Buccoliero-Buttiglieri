package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceType;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.actions.BackAction;
import it.polimi.ingsw.model.actions.SelectCraftingOutputAction;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.fsm.State;
import it.polimi.ingsw.model.fsm.states.CraftingResourceSelectionState;
import it.polimi.ingsw.model.fsm.states.CraftingState;
import it.polimi.ingsw.model.fsm.states.OutputSelectionState;
import it.polimi.ingsw.model.production.Production;
import it.polimi.ingsw.model.production.UpgradableCrafting;
import it.polimi.ingsw.server.ServerBuilder;
import it.polimi.ingsw.utils.ResourceLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OutputSelectionStateTest {

    private GameContext gameContext;
    private State currentState;

    private final ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
    private final ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
    private final ResourceSingle shield = ResourceTypeSingleton.getInstance().getShieldResource();

    @BeforeEach
    public void init() throws ParserException {

        List<String> usernames = Arrays.asList("Ernestino", "Panfilo", "Sharonna");
        String config = ResourceLoader.loadFile("cfg/config.json");
        String crafting = ResourceLoader.loadFile("cfg/crafting.json");
        String faith = ResourceLoader.loadFile("cfg/faith.json");
        String leaders = ResourceLoader.loadFile("cfg/leaders.json");

        GameModel model  = ServerBuilder.buildModel(config, crafting, faith, leaders, usernames, false, new Random(3));

        gameContext = new GameContext(model, false);
        gameContext.setCurrentPlayer(model.getPlayerById("Ernestino"));
        currentState = new OutputSelectionState(gameContext);


        //crafting 1
        Map<ResourceType, Integer> input1 = new HashMap<>();
        Map<ResourceType, Integer> output1 = new HashMap<>();
        input1.put(gold, 2);
        output1.put(ResourceTypeSingleton.getInstance().getAnyResource(), 4);
        UpgradableCrafting crafting1 = new UpgradableCrafting(input1, output1, 0, 1);

        //Ernestino has 3 gold in his bottom shelf
        assertDoesNotThrow(()-> gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getStorage()
                .getCupboard().getShelfById("BottomShelf").addResources(gold, 3));

        //Ernestino has crafting1
        assertDoesNotThrow(()-> gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getProduction()
                .setUpgradableCrafting(0, crafting1));

        //Ernestino has selected crafting1
        assertDoesNotThrow(()->gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getProduction()
        .selectCrafting(Production.CraftingType.UPGRADABLE, 0));

    }

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

    @Test
    public void backRequestFromInvalidPlayer(){
        assertThrows(FSMTransitionFailedException.class, ()-> currentState.handleAction(new BackAction("Sharonna")));
    }

    @Test
    public void successfulSelectOutput(){

        try{
            currentState.handleAction(new SelectCraftingOutputAction("Ernestino",
                    new HashMap<>(){{
                        put(gold, 1);
                        put(servant, 2);
                        put(shield, 1);
                    }}));
        }catch(FSMTransitionFailedException e){
            throw new RuntimeException();
        }

        assertTrue(currentState.getNextState() instanceof CraftingResourceSelectionState);
        assertFalse(gameContext.hasPlayerMoved());
        assertFalse(gameContext.hasPlayerMoved());
    }

    @Test
    public void invalidSelectOutput(){
        assertThrows(FSMTransitionFailedException.class, ()-> currentState.handleAction(
                new SelectCraftingOutputAction("Ernestino", new HashMap<>(){{
                    put(gold, 1);
                    put(servant, 3);
                    put(shield, 1);
                }})
        ));

        assertThrows(FSMTransitionFailedException.class, ()-> currentState.handleAction(
                new SelectCraftingOutputAction("Ernestino", new HashMap<>(){{
                    put(gold, 1);
                }})
        ));
    }




}
