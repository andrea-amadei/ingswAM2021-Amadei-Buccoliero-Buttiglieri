package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.common.exceptions.IllegalActionException;
import it.polimi.ingsw.common.exceptions.ParserException;
import it.polimi.ingsw.server.model.basetypes.ResourceSingle;
import it.polimi.ingsw.server.model.basetypes.ResourceTypeSingleton;
import it.polimi.ingsw.server.model.global.GameModel;
import it.polimi.ingsw.server.model.global.Player;
import it.polimi.ingsw.server.model.global.Shop;
import it.polimi.ingsw.server.model.actions.SelectCardFromShopAction;
import it.polimi.ingsw.server.model.fsm.GameContext;
import it.polimi.ingsw.server.model.production.Production;
import it.polimi.ingsw.server.model.production.UpgradableCrafting;
import it.polimi.ingsw.server.ServerBuilder;
import it.polimi.ingsw.common.utils.ResourceLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SelectCardFromShopActionTest {
    private GameContext gameContext;
    private final ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
    private final ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();

    private Player player1;

    @BeforeEach
    public void init() throws ParserException {
        List<String> usernames = Collections.singletonList("Ernestino");
        String config = ResourceLoader.loadFile("cfg/config.json");
        String crafting = ResourceLoader.loadFile("cfg/crafting.json");
        String faith = ResourceLoader.loadFile("cfg/faith.json");
        String leaders = ResourceLoader.loadFile("cfg/leaders.json");

        GameModel model = ServerBuilder.buildModel(config, crafting, faith, leaders, usernames, true, new Random(3));

        gameContext = new GameContext(model, true);

        player1 = model.getPlayerById("Ernestino");
    }

    @Test
    public void selectLevel1CardAndEmptyCraftingSlot(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Ernestino"));
        assertDoesNotThrow(()->new SelectCardFromShopAction("Ernestino", 0, 0, 2).execute(gameContext));
        Shop shop = gameContext.getGameModel().getShop();

        assertEquals(2, player1.getBoard().getProduction().getSelectedCraftingIndex());
        assertEquals(shop.getTopCard(0,0), shop.getSelectedCard());
    }

    @Test
    public void selectLevel2CardAndEmptyCraftingSlot(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Ernestino"));
        assertThrows(IllegalActionException.class,
                ()->new SelectCardFromShopAction("Ernestino", 1, 1, 2).execute(gameContext));
        assertNull(gameContext.getGameModel().getShop().getSelectedCard());
        assertThrows(NoSuchElementException.class, () -> player1.getBoard().getProduction().getSelectedCraftingIndex());
    }

    @Test
    public void selectLevel3CardAndLevel2CraftingSlot(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Ernestino"));
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
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Ernestino"));
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
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Ernestino"));
        assertThrows(IllegalActionException.class, ()->new SelectCardFromShopAction("Ernestino", 3, 0, 2).execute(gameContext));
    }

    @Test
    public void outOfBoundCraftingSlotSelection(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Ernestino"));
        assertThrows(IllegalActionException.class, ()->new SelectCardFromShopAction("Ernestino", 2, 0, 3).execute(gameContext));
    }

    @Test
    public void noSuchPlayer(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Ernestino"));
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
