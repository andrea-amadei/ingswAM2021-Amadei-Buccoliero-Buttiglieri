package it.polimi.ingsw;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceType;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.GameParameters;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.actions.Action;
import it.polimi.ingsw.model.actions.SelectCraftingAction;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.production.Production;
import it.polimi.ingsw.model.production.UpgradableCrafting;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SelectCraftingActionTest {

    private GameContext gameContext;
    private final ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
    private final ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();

    @BeforeEach
    public void init() {

        Player player1 = new Player("Ernestino", 0);
        Player player2 = new Player("Letteria", 1);
        GameModel model = new GameModel(Arrays.asList(player1, player2));
        gameContext = new GameContext(model);

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
        Action action = new SelectCraftingAction("Ernestino", Production.CraftingType.UPGRADABLE, 0);
        assertDoesNotThrow(()-> action.execute(gameContext));

        assertEquals(Production.CraftingType.UPGRADABLE, gameContext.getGameModel().getPlayerById("Ernestino")
        .getBoard().getProduction().getSelectedCraftingType());
    }

    @Test
    public void selectsCorrectIndex(){
        Action action = new SelectCraftingAction("Ernestino", Production.CraftingType.UPGRADABLE, 0);
        assertDoesNotThrow(()-> action.execute(gameContext));

        assertEquals(0, gameContext.getGameModel().getPlayerById("Ernestino").getBoard()
        .getProduction().getSelectedCraftingIndex());
    }

    @Test
    public void messages(){
        Action action = new SelectCraftingAction("Ernestino", Production.CraftingType.UPGRADABLE, 0);
        List<Message> messages;
        try{
            messages = action.execute(gameContext);
        }catch(IllegalActionException e){
            throw new RuntimeException();
        }

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
        Action action = new SelectCraftingAction("Andrea Dipré", Production.CraftingType.BASE, 1);
        assertThrows(IllegalActionException.class, ()-> action.execute(gameContext));
    }

    @Test
    public void indexOutOfBound(){
        Action action = new SelectCraftingAction("Letteria", Production.CraftingType.BASE, GameParameters.UPGRADABLE_CRAFTING_NUMBER);
        assertThrows(IllegalActionException.class, ()-> action.execute(gameContext));

    }

    @Test
    public void selectingNonExistentSlot(){
        Action action = new SelectCraftingAction("Letteria", Production.CraftingType.LEADER, 2);
        assertThrows(IllegalActionException.class, ()-> action.execute(gameContext));
    }

    @Test
    public void selectEmptySlot(){
        Action action = new SelectCraftingAction("Letteria", Production.CraftingType.UPGRADABLE, 0);
        assertThrows(IllegalActionException.class, ()-> action.execute(gameContext));
    }

}
