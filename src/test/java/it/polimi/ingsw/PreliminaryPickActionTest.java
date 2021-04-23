package it.polimi.ingsw;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.actions.PreliminaryPickAction;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.leader.LeaderCard;
import it.polimi.ingsw.server.DummyBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PreliminaryPickActionTest {

    private GameContext gameContext;

    private final ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();

    private Player player1;
    private Player player3;

    private List<Integer> leadersToDiscard;
    private Map<ResourceSingle, Integer> chosenResources;

    @BeforeEach
    public void init(){
        player1 = new Player("Ernestino", 0);
        Player player2 = new Player("Cosma", 1);
        player3 = new Player("Leopoldo", 2);
        Player player4 = new Player("Urbano", 3);

        leadersToDiscard = new ArrayList<>();
        chosenResources = new HashMap<>();

        GameModel model = new GameModel(Arrays.asList(player1, player2, player3, player4));
        gameContext = new GameContext(model);
        gameContext.setCurrentPlayer(player1);

        List<LeaderCard> allLeaders = DummyBuilder.buildLeaderCards();
        assert allLeaders != null;

        //giving 4 leaders to player 1
        player1.getBoard().addLeaderCard(allLeaders.get(0));
        player1.getBoard().addLeaderCard(allLeaders.get(1));
        player1.getBoard().addLeaderCard(allLeaders.get(2));
        player1.getBoard().addLeaderCard(allLeaders.get(3));

        //giving 4 leaders to player 3
        player3.getBoard().addLeaderCard(allLeaders.get(4));
        player3.getBoard().addLeaderCard(allLeaders.get(5));
        player3.getBoard().addLeaderCard(allLeaders.get(6));
        player3.getBoard().addLeaderCard(allLeaders.get(7));

    }

    @Test
    public void nullPlayer(){
        assertThrows(NullPointerException.class, ()-> new PreliminaryPickAction(null, leadersToDiscard, chosenResources));
    }

    @Test
    public void nullLeadersToDiscard(){
        assertThrows(NullPointerException.class, ()-> new PreliminaryPickAction("Leopoldo", null, chosenResources));
    }

    @Test
    public void nullChosenResources(){
        assertThrows(NullPointerException.class, ()-> new PreliminaryPickAction("Leopoldo", leadersToDiscard, null));
    }

    @Test
    public void invalidAmountOfLeaders(){
        leadersToDiscard.add(2);
        leadersToDiscard.add(0);
        leadersToDiscard.add(1);

        assertThrows(IllegalActionException.class, ()->
                new PreliminaryPickAction("Ernestino", leadersToDiscard, chosenResources).execute(gameContext));
    }

    @Test
    public void invalidIDsOfLeaders(){
        leadersToDiscard.add(100);
        leadersToDiscard.add(3);

        assertThrows(IllegalActionException.class, ()->
                new PreliminaryPickAction("Ernestino", leadersToDiscard, chosenResources).execute(gameContext));
    }

    @Test
    public void negativeAmountOfResources(){
        leadersToDiscard.add(1);
        leadersToDiscard.add(3);
        chosenResources.put(gold, -1);

        assertThrows(IllegalArgumentException.class, ()-> new PreliminaryPickAction("Ernestino", leadersToDiscard, chosenResources));
    }

    @Test
    public void nullGameContext(){
        leadersToDiscard.add(2);
        leadersToDiscard.add(3);
        chosenResources.put(gold, 1);

        assertThrows(NullPointerException.class, ()->
                new PreliminaryPickAction("Ernestino", leadersToDiscard, chosenResources).execute(null));
    }

    @Test
    public void invalidPlayer(){
        leadersToDiscard.add(2);
        leadersToDiscard.add(3);
        chosenResources.put(gold, 1);

        assertThrows(IllegalActionException.class, ()->
                new PreliminaryPickAction("Cosma", leadersToDiscard, chosenResources).execute(gameContext));
    }

    @Test
    public void correctlyDiscardingLeaders(){
        leadersToDiscard.add(2);
        leadersToDiscard.add(3);
        List<LeaderCard> remaining = new ArrayList<>();
        remaining.add(player1.getBoard().getLeaderCards().get(0));
        remaining.add(player1.getBoard().getLeaderCards().get(1));

        assertDoesNotThrow(()->
                new PreliminaryPickAction("Ernestino", leadersToDiscard, chosenResources).execute(gameContext));

        assertEquals(2, player1.getBoard().getLeaderCards().size());
        assertEquals(remaining, player1.getBoard().getLeaderCards());
    }

    @Test
    public void requestingIncorrectAmountOfResources(){
        leadersToDiscard.add(2);
        leadersToDiscard.add(3);
        chosenResources.put(gold, 2);

        assertThrows(IllegalActionException.class, ()->
                new PreliminaryPickAction("Ernestino", leadersToDiscard, chosenResources).execute(gameContext));
    }

    @Test
    public void correctlyAddingFaith(){
        gameContext.setCurrentPlayer(player3);

        leadersToDiscard.add(2);
        leadersToDiscard.add(3);
        chosenResources.put(gold, 1);

        assertDoesNotThrow(()->
                new PreliminaryPickAction("Leopoldo", leadersToDiscard, chosenResources).execute(gameContext));

        assertEquals(1, player3.getBoard().getFaithHolder().getFaithPoints());
    }

    @Test
    public void correctlyAddingResources(){
        gameContext.setCurrentPlayer(player3);

        leadersToDiscard.add(2);
        leadersToDiscard.add(3);
        chosenResources.put(gold, 1);

        assertDoesNotThrow(()->
                new PreliminaryPickAction("Leopoldo", leadersToDiscard, chosenResources).execute(gameContext));

        assertEquals(chosenResources, player3.getBoard().getStorage().getHand().getAllResources());
    }

}
