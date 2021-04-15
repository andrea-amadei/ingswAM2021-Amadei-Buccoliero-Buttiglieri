package it.polimi.ingsw;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.gamematerials.*;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.actions.Action;
import it.polimi.ingsw.model.actions.BuyFromShopAction;
import it.polimi.ingsw.model.actions.SelectCardFromShopAction;
import it.polimi.ingsw.model.actions.SelectResourcesAction;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.production.CraftingCard;
import it.polimi.ingsw.model.production.UpgradableCrafting;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BuyFromShopActionTest {

    private GameContext gameContext;
    private final ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
    private final ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
    private final ResourceSingle shield = ResourceTypeSingleton.getInstance().getShieldResource();
    private CraftingCard craftingCard1;

    @BeforeEach
    public void init(){

        Player player1 = new Player("Ernestino", 0);
        Player player2 = new Player("Ottone", 1);
        GameModel model = new GameModel(Arrays.asList(player1, player2));
        gameContext = new GameContext(model);

        assertDoesNotThrow(()->player1.getBoard().getStorage().getCupboard().getShelfById("BottomShelf").addResources(gold, 3));
        assertDoesNotThrow(()-> player1.getBoard().getStorage().getChest().addResources(servant, 2));
        assertDoesNotThrow(()-> player2.getBoard().getStorage().getChest().addResources(shield, 1));
        //Ernestino has 3 gold resources in his bottom shelf and 2 servant resources in his chest
        //Ottone has 1 shield

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
        //crafting card 1

        gameContext.getGameModel().getShop().addCard(craftingCard1);

        Action action = new SelectCardFromShopAction("Ernestino", 0, 3, 1);
        assertDoesNotThrow(()-> action.execute(gameContext));
        Action action1 = new SelectResourcesAction("Ernestino", "BottomShelf", gold, 2);
        Action action2 = new SelectResourcesAction("Ernestino", "Chest", servant, 2);
        assertDoesNotThrow(()-> action1.execute(gameContext));
        assertDoesNotThrow(()-> action2.execute(gameContext));
        //Ernestino selects card 1 from shop, then he selects the right amount of resources to buy it
    }

    @Test
    public void executeMethodRemovesCardFromShop(){
        Action action = new BuyFromShopAction("Ernestino");
        assertDoesNotThrow(()->action.execute(gameContext));


        assertNotEquals(craftingCard1, gameContext.getGameModel().getShop().getTopCard(0,3));
    }

    @Test
    public void executeMethodPutsCardInCorrectProductionSlot(){
        Action action = new BuyFromShopAction("Ernestino");
        assertDoesNotThrow(()->action.execute(gameContext));


        assertEquals(craftingCard1.getCrafting(), gameContext.getGameModel().getPlayerById("Ernestino").getBoard()
        .getProduction().getUpgradableCrafting(1));
    }

    @Test
    public void executeMethodAddsFlagToFlagHolder(){
        Action action = new BuyFromShopAction("Ernestino");
        assertDoesNotThrow(()->action.execute(gameContext));

        assertEquals(1, gameContext.getGameModel().getPlayerById("Ernestino").getBoard()
        .getFlagHolder().numberOfFlagsByColor(FlagColor.YELLOW));
    }

    @Test
    public void executeMethodAddsVictoryPointsToPlayer(){
        Action action = new BuyFromShopAction("Ernestino");
        assertDoesNotThrow(()->action.execute(gameContext));

        assertEquals(5, gameContext.getGameModel().getPlayerById("Ernestino").getPoints());
    }

    @Test
    public void resetResourcesSelection(){
        Action action = new BuyFromShopAction("Ernestino");
        assertDoesNotThrow(()->action.execute(gameContext));

        assertNull(gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getStorage().getSelection());
    }

    @Test
    public void resetCardSelection(){
        Action action = new BuyFromShopAction("Ernestino");
        assertDoesNotThrow(()->action.execute(gameContext));

        assertNull(gameContext.getGameModel().getShop().getSelectedCard());
    }

    @Test
    public void nullGameContext(){
        Action action = new BuyFromShopAction("Ernestino");
        assertThrows(NullPointerException.class, ()-> action.execute(null));
    }

    @Test
    public void invalidPlayer(){
        Action action = new BuyFromShopAction("Ermenegilda");
        assertThrows(IllegalActionException.class, ()-> action.execute(gameContext));
    }

    @Test
    public void buyingCardWithoutSelectingResources() throws IllegalActionException {
        Action action = new SelectCardFromShopAction("Ottone", 0, 0, 1);
        action.execute(gameContext);
        Action action1 = new BuyFromShopAction("Ottone");
        assertThrows(IllegalActionException.class, ()-> action1.execute(gameContext));
    }

    @Test
    public void buyingCardWithoutSelectingCard() throws IllegalActionException {
        Action action = new SelectResourcesAction("Ottone", "Chest", shield, 1);
        action.execute(gameContext);
        Action action1 = new BuyFromShopAction("Ottone");

        assertThrows(IllegalActionException.class, ()-> action1.execute(gameContext));
    }

    @Test
    public void buyingCardWithNotEnoughResources() throws IllegalActionException {
        Action action = new SelectResourcesAction("Ottone", "Chest", shield, 1);
        action.execute(gameContext);
        Action action1 = new SelectCardFromShopAction("Ottone", 0, 3, 1);
        action1.execute(gameContext);
        Action action2 = new BuyFromShopAction("Ottone");

        assertThrows(IllegalActionException.class, ()-> action2.execute(gameContext));
    }
}
