package it.polimi.ingsw;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.gamematerials.*;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.actions.BackAction;
import it.polimi.ingsw.model.actions.SelectCardFromShopAction;
import it.polimi.ingsw.model.fsm.*;
import it.polimi.ingsw.model.fsm.states.MenuState;
import it.polimi.ingsw.model.fsm.states.ShopResourceSelectionState;
import it.polimi.ingsw.model.fsm.states.ShopState;
import it.polimi.ingsw.model.production.CraftingCard;
import it.polimi.ingsw.model.production.UpgradableCrafting;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ShopStateTest {

    private GameContext gameContext;
    private State currentState;

    private final ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
    private final ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
    private final ResourceSingle shield = ResourceTypeSingleton.getInstance().getServantResource();
    private CraftingCard craftingCard1;

    @BeforeEach
    public void init() {

        Player player1 = new Player("Ernestino", 0);
        Player player2 = new Player("Mariola", 1);
        Player player3 = new Player("Augusta", 2);
        GameModel model = new GameModel(Arrays.asList(player1, player2, player3), new Random(3));

        gameContext = new GameContext(model);
        gameContext.setCurrentPlayer(model.getPlayerById("Ernestino"));
        currentState = new ShopState(gameContext);

        assertDoesNotThrow(()-> gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getStorage()
                .getChest().addResources(gold, 5));
        assertDoesNotThrow(()-> gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getStorage()
                .getChest().addResources(servant, 7));

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
        gameContext.getGameModel().getShop().addCard(craftingCard1);
        //crafting card 1
    }

    @Test
    public void successfulBack(){
        List<Message> messages;

        try{
            messages = currentState.handleAction(new BackAction("Ernestino"));
        }catch(FSMTransitionFailedException e){
            throw new RuntimeException();
        }

        assertTrue(messages.size() > 0);
        assertTrue(currentState.getNextState() instanceof MenuState);
        assertFalse(gameContext.hasPlayerMoved());
    }

    @Test
    public void successfulSelectCard(){
        List<Message> messages;

        try{
            messages = currentState.handleAction(new SelectCardFromShopAction("Ernestino", 0, 3, 1));
        }catch(FSMTransitionFailedException e){
            throw new RuntimeException();
        }

        assertTrue(messages.size() > 0);
        assertTrue(currentState.getNextState() instanceof ShopResourceSelectionState);
        assertFalse(gameContext.hasPlayerMoved());
        assertEquals(craftingCard1, gameContext.getGameModel().getShop().getSelectedCard());
    }

    @Test
    public void selectingNonExistentCard(){
        assertThrows(FSMTransitionFailedException.class, ()-> currentState.handleAction(new SelectCardFromShopAction("Ernestino", 0, 4, 99)));
        assertNull(gameContext.getGameModel().getShop().getSelectedCard());
        assertNull(currentState.getNextState());
    }

    //TODO: togliere il commento una volta implementato il controllo di correttezza del player nelle azioni
    //@Test
    //public void selectRequestFromInvalidPlayer(){
    //   assertThrows(FSMTransitionFailedException.class, ()-> currentState.handleAction(new SelectCardFromShopAction("Mariola", 0, 3, 1)));
    //}

    @Test
    public void backRequestFromInvalidPlayer(){
        assertThrows(FSMTransitionFailedException.class, ()->currentState.handleAction(new BackAction("Mariola")));
    }
}
