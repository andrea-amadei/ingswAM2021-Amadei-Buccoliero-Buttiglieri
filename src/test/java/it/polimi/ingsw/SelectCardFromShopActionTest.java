package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Shop;
import it.polimi.ingsw.model.actions.SelectCardFromShopAction;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.production.Production;
import it.polimi.ingsw.model.production.UpgradableCrafting;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Collections;
import java.util.HashMap;
import java.util.NoSuchElementException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SelectCardFromShopActionTest {
    private GameContext gameContext;
    private final ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
    private final ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();

    private Player player1;

    @BeforeEach
    public void init(){
        player1 = new Player("Ernestino", 0);
        GameModel model = new GameModel(Collections.singletonList(player1));
        gameContext = new GameContext(model);
    }

    @Test
    public void selectLevel1CardAndEmptyCraftingSlot(){
        assertDoesNotThrow(()->new SelectCardFromShopAction("Ernestino", 0, 0, 2).execute(gameContext));
        Shop shop = gameContext.getGameModel().getShop();

        assertEquals(2, player1.getBoard().getProduction().getSelectedCraftingIndex());
        assertEquals(shop.getTopCard(0,0), shop.getSelectedCard());
    }

    @Test
    public void selectLevel2CardAndEmptyCraftingSlot(){
        assertThrows(IllegalActionException.class,
                ()->new SelectCardFromShopAction("Ernestino", 1, 1, 2).execute(gameContext));
        assertNull(gameContext.getGameModel().getShop().getSelectedCard());
        assertThrows(NoSuchElementException.class, () -> player1.getBoard().getProduction().getSelectedCraftingIndex());
    }

    @Test
    public void selectLevel3CardAndLevel2CraftingSlot(){
        Production production = player1.getBoard().getProduction();
        Shop shop = gameContext.getGameModel().getShop();
        production.setUpgradableCrafting(2, new UpgradableCrafting(
                new HashMap<>(){{put(gold, 2);}}, new HashMap<>(){{put(servant, 2);}}, 0, 2
        ));

        assertDoesNotThrow(()->new SelectCardFromShopAction("Ernestino", 2, 0, 2).execute(gameContext));

        assertEquals(2, production.getSelectedCraftingIndex());
        assertEquals(shop.getTopCard(2, 0), shop.getSelectedCard());
    }

    @Test
    public void selectLevel3CardAndLevel1CraftingSlot(){
        Production production = player1.getBoard().getProduction();
        Shop shop = gameContext.getGameModel().getShop();
        production.setUpgradableCrafting(2, new UpgradableCrafting(
                new HashMap<>(){{put(gold, 2);}}, new HashMap<>(){{put(servant, 2);}}, 0, 1
        ));

        assertThrows(IllegalActionException.class, ()->new SelectCardFromShopAction("Ernestino", 2, 0, 2).execute(gameContext));

        assertThrows(NoSuchElementException.class, production::getSelectedCraftingIndex);
        assertNull(shop.getSelectedCard());
    }

    @Test
    public void outOfBoundShopCardSelection(){
        assertThrows(IllegalActionException.class, ()->new SelectCardFromShopAction("Ernestino", 3, 0, 2).execute(gameContext));
    }

    @Test
    public void outOfBoundCraftingSlotSelection(){
        assertThrows(IllegalActionException.class, ()->new SelectCardFromShopAction("Ernestino", 2, 0, 3).execute(gameContext));
    }

    @Test
    public void noSuchPlayer(){
        assertThrows(IllegalActionException.class, ()->new SelectCardFromShopAction("Ernestina", 2, 0, 2).execute(gameContext));
    }

    @Test
    public void nullPlayer(){
        assertThrows(NullPointerException.class, ()->new SelectCardFromShopAction(null, 2, 0, 2).execute(gameContext));
    }

    @Test
    public void negativeIndex(){
        assertThrows(IllegalArgumentException.class, ()->new SelectCardFromShopAction("Ernestino", 2, -1, 3).execute(gameContext));
    }

    @Test
    public void nullContext(){
        assertThrows(NullPointerException.class, ()->new SelectCardFromShopAction("Ernestino", 2, 2, 3).execute(null));
    }
}
