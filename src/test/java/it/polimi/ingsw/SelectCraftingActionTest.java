package it.polimi.ingsw;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.exceptions.IllegalActionException;
import it.polimi.ingsw.common.exceptions.ParserException;
import it.polimi.ingsw.server.model.basetypes.ResourceSingle;
import it.polimi.ingsw.server.model.basetypes.ResourceType;
import it.polimi.ingsw.server.model.basetypes.ResourceTypeSingleton;
import it.polimi.ingsw.server.model.global.GameModel;
import it.polimi.ingsw.server.model.actions.Action;
import it.polimi.ingsw.server.model.actions.SelectCraftingAction;
import it.polimi.ingsw.server.model.fsm.GameContext;
import it.polimi.ingsw.server.model.production.Production;
import it.polimi.ingsw.server.model.production.UpgradableCrafting;
import it.polimi.ingsw.server.ServerBuilder;
import it.polimi.ingsw.common.utils.ResourceLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SelectCraftingActionTest {

    private GameContext gameContext;
    private final ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
    private final ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();

    @BeforeEach
    public void init() throws ParserException {

        List<String> usernames = Arrays.asList("Ernestino", "Letteria");
        String config = ResourceLoader.loadFile("cfg/config.json");
        String crafting = ResourceLoader.loadFile("cfg/crafting.json");
        String faith = ResourceLoader.loadFile("cfg/faith.json");
        String leaders = ResourceLoader.loadFile("cfg/leaders.json");

        GameModel model = ServerBuilder.buildModel(config, crafting, faith, leaders, usernames, false, new Random(3));

        gameContext = new GameContext(model, false);

        HashMap<ResourceType, Integer> input = new HashMap<>();
        HashMap<ResourceType, Integer> output = new HashMap<>();
        input.put(gold, 1);
        output.put(servant, 3);
        UpgradableCrafting upgradableCrafting = new UpgradableCrafting(input, output, 1, 1);
        assertDoesNotThrow(()-> gameContext.getGameModel().getPlayerById("Ernestino").getBoard()
        .getProduction().setUpgradableCrafting(0, upgradableCrafting));
    }

    @Test
    public void selectsCorrectCraftingType(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Ernestino"));
        Action action = new SelectCraftingAction("Ernestino", Production.CraftingType.UPGRADABLE, 0);
        assertDoesNotThrow(()-> action.execute(gameContext));

        assertEquals(Production.CraftingType.UPGRADABLE, gameContext.getGameModel().getPlayerById("Ernestino")
        .getBoard().getProduction().getSelectedCraftingType());
    }

    @Test
    public void selectsCorrectIndex(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Ernestino"));
        Action action = new SelectCraftingAction("Ernestino", Production.CraftingType.UPGRADABLE, 0);
        assertDoesNotThrow(()-> action.execute(gameContext));

        assertEquals(0, gameContext.getGameModel().getPlayerById("Ernestino").getBoard()
        .getProduction().getSelectedCraftingIndex());
    }

    @Test
    public void messages(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Ernestino"));
        Action action = new SelectCraftingAction("Ernestino", Production.CraftingType.UPGRADABLE, 0);
        List<Message> messages;
        try{
            messages = action.execute(gameContext);
        }catch(IllegalActionException e){
            throw new RuntimeException();
        }

        assertTrue(messages.size() > 0);
    }

    @Test
    public void nullPlayer(){
        assertThrows(NullPointerException.class, ()-> new SelectCraftingAction(null, Production.CraftingType.BASE, 1));
    }

    @Test
    public void nullCraftingType(){
        assertThrows(NullPointerException.class, ()-> new SelectCraftingAction("Letteria", null, 1));
    }

    @Test
    public void invalidIndex(){
        assertThrows(IllegalArgumentException.class, ()-> new SelectCraftingAction("Letteria", Production.CraftingType.BASE, -1));
    }

    @Test
    public void nullGameContext(){
        Action action = new SelectCraftingAction("Letteria", Production.CraftingType.BASE, 1);
        assertThrows(NullPointerException.class, ()-> action.execute(null));
    }

    @Test
    public void invalidPlayer(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Ernestino"));
        Action action = new SelectCraftingAction("Andrea Dipré", Production.CraftingType.BASE, 1);
        assertThrows(IllegalActionException.class, ()-> action.execute(gameContext));
    }

    @Test
    public void indexOutOfBound(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Letteria"));
        Action action = new SelectCraftingAction("Letteria", Production.CraftingType.BASE, 3);
        assertThrows(IllegalActionException.class, ()-> action.execute(gameContext));

    }

    @Test
    public void selectingNonExistentSlot(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Letteria"));
        Action action = new SelectCraftingAction("Letteria", Production.CraftingType.LEADER, 2);
        assertThrows(IllegalActionException.class, ()-> action.execute(gameContext));
    }

    @Test
    public void selectEmptySlot(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Letteria"));
        Action action = new SelectCraftingAction("Letteria", Production.CraftingType.UPGRADABLE, 0);
        assertThrows(IllegalActionException.class, ()-> action.execute(gameContext));
    }

}
