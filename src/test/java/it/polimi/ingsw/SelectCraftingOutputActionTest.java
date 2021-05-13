package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.gamematerials.*;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.actions.SelectCraftingOutputAction;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.production.Crafting;
import it.polimi.ingsw.model.production.Production;
import it.polimi.ingsw.model.production.UpgradableCrafting;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SelectCraftingOutputActionTest {
    private GameContext gameContext;

    private final ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
    private final ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
    private final ResourceSingle shield = ResourceTypeSingleton.getInstance().getShieldResource();
    private final ResourceSingle stone = ResourceTypeSingleton.getInstance().getStoneResource();
    private final ResourceGroup any = ResourceTypeSingleton.getInstance().getAnyResource();

    Player player1;
    @BeforeEach
    public void init(){
        player1 = new Player("Paolo", 0);

        Map<ResourceType, Integer> input = new HashMap<>(){{put(gold, 2);put(shield, 1);put(any, 2);}};
        Map<ResourceType, Integer> output = new HashMap<>(){{put(servant, 2);put(stone, 1);put(any, 3);}};
        player1.getBoard().getProduction().setUpgradableCrafting(0, new UpgradableCrafting(input, output, 0, 1));
        player1.getBoard().getProduction().addLeaderCrafting(new Crafting(input, output, 2));
        GameModel model = new GameModel(Collections.singletonList(player1));
        gameContext = new GameContext(model);
    }


    @Test
    public void selectBaseCraftingConversion(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Paolo"));
        player1.getBoard().getProduction().selectCrafting(Production.CraftingType.UPGRADABLE, 0);
        assertDoesNotThrow(()->new SelectCraftingOutputAction("Paolo", new HashMap<>(){{put(gold, 2);put(shield, 1);}}).execute(gameContext));
    }

    @Test
    public void selectBaseCraftingWithWrongConversion(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Paolo"));
        player1.getBoard().getProduction().selectCrafting(Production.CraftingType.UPGRADABLE, 0);
        assertThrows(IllegalActionException.class, ()->new SelectCraftingOutputAction("Paolo", new HashMap<>(){{put(gold, 1);put(shield, 1);}}).execute(gameContext));
    }

    @Test
    public void selectConversionWithoutCraftingSelected(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Paolo"));
        assertThrows(IllegalActionException.class, ()->new SelectCraftingOutputAction("Paolo", new HashMap<>(){{put(gold, 1);put(shield, 1);}}).execute(gameContext));
    }

    @Test
    public void selectBaseCraftingWithTooBigConversion(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Paolo"));
        player1.getBoard().getProduction().selectCrafting(Production.CraftingType.UPGRADABLE, 0);
        assertDoesNotThrow(()->new SelectCraftingOutputAction("Paolo", new HashMap<>(){{put(gold, 2);put(shield, 1);put(shield, 1);}}).execute(gameContext));
    }

    @Test
    public void noSuchPlayer(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Paolo"));
        assertThrows(IllegalActionException.class, ()->new SelectCraftingOutputAction("Ugo", new HashMap<>(){{put(gold, 1);put(shield, 1);}}).execute(gameContext));
    }

    @Test
    public void nullGameContext(){
        assertThrows(NullPointerException.class, ()->new SelectCraftingOutputAction("Ugo", new HashMap<>(){{put(gold, 1);put(shield, 1);}}).execute(null));
    }

    @Test
    public void nullPlayer(){
        assertThrows(NullPointerException.class, ()->new SelectCraftingOutputAction(null, new HashMap<>(){{put(gold, 1);put(shield, 1);}}));
    }

    @Test
    public void nullConversion(){
        assertThrows(NullPointerException.class, ()->new SelectCraftingOutputAction("Paolo", null));
    }
}
