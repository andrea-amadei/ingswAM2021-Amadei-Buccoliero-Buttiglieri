package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.actions.PreliminaryPickAction;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.fsm.State;
import it.polimi.ingsw.model.fsm.states.PreliminaryPickState;
import it.polimi.ingsw.model.fsm.states.PreliminaryTidyState;
import it.polimi.ingsw.server.ServerBuilder;
import it.polimi.ingsw.utils.ResourceLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PreliminaryPickStateTest {
    private GameContext gameContext;
    private State currentState;

    private final ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
    private final ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
    private final ResourceSingle shield = ResourceTypeSingleton.getInstance().getShieldResource();
    private final ResourceSingle stone = ResourceTypeSingleton.getInstance().getStoneResource();

    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;
    private GameModel model;

    @BeforeEach
    public void init() throws ParserException {

        List<String> usernames = Arrays.asList("Ernestino", "Bartolomeo", "Teofila", "Ottone");
        String config = ResourceLoader.loadFile("cfg/config.json");
        String crafting = ResourceLoader.loadFile("cfg/crafting.json");
        String faith = ResourceLoader.loadFile("cfg/faith.json");
        String leaders = ResourceLoader.loadFile("cfg/leaders.json");

        model  = ServerBuilder.buildModel(config, crafting, faith, leaders, usernames, false, new Random(3));

        gameContext = new GameContext(model, false);
        currentState = new PreliminaryPickState(gameContext);

        player1 = model.getPlayerById("Ernestino");
        player2 = model.getPlayerById("Bartolomeo");
        player3 = model.getPlayerById("Teofila");
        player4 = model.getPlayerById("Ottone");

        //each player has 4 leader cards
        player1.getBoard().addLeaderCard(model.getLeaderCards().get(0));
        player1.getBoard().addLeaderCard(model.getLeaderCards().get(1));
        player1.getBoard().addLeaderCard(model.getLeaderCards().get(2));
        player1.getBoard().addLeaderCard(model.getLeaderCards().get(3));

        player2.getBoard().addLeaderCard(model.getLeaderCards().get(4));
        player2.getBoard().addLeaderCard(model.getLeaderCards().get(5));
        player2.getBoard().addLeaderCard(model.getLeaderCards().get(6));
        player2.getBoard().addLeaderCard(model.getLeaderCards().get(7));

        player3.getBoard().addLeaderCard(model.getLeaderCards().get(8));
        player3.getBoard().addLeaderCard(model.getLeaderCards().get(9));
        player3.getBoard().addLeaderCard(model.getLeaderCards().get(10));
        player3.getBoard().addLeaderCard(model.getLeaderCards().get(11));

        player4.getBoard().addLeaderCard(model.getLeaderCards().get(12));
        player4.getBoard().addLeaderCard(model.getLeaderCards().get(13));
        player4.getBoard().addLeaderCard(model.getLeaderCards().get(14));
        player4.getBoard().addLeaderCard(model.getLeaderCards().get(15));

    }

    @Test
    public void firstPlayerPick(){
        gameContext.setCurrentPlayer(model.getPlayerById("Ernestino"));

        //List of leader to discard
        List<Integer> leaderToDiscard = new ArrayList<>();
        leaderToDiscard.add(1);
        leaderToDiscard.add(3);

        //Map of resources to get
        Map<ResourceSingle, Integer> resources = new HashMap<>();

        try{
            currentState.handleAction(new PreliminaryPickAction("Ernestino", leaderToDiscard,
                    resources));
        }catch(FSMTransitionFailedException e){
            throw new RuntimeException(e.getMessage());
        }

        assertTrue(currentState.getNextState() instanceof PreliminaryTidyState);
        assertEquals(0, player1.getBoard().getFaithHolder().getFaithPoints());
        assertEquals(2, player1.getBoard().getLeaderCards().size());
        assertEquals(0, player1.getBoard().getStorage().getHand().totalAmountOfResources());
    }

    @Test
    public void secondPlayerPick(){
        gameContext.setCurrentPlayer(model.getPlayerById("Bartolomeo"));

        //List of leader to discard
        List<Integer> leaderToDiscard = new ArrayList<>();
        leaderToDiscard.add(0);
        leaderToDiscard.add(1);

        //Map of resources to get
        Map<ResourceSingle, Integer> resources = new HashMap<>();
        resources.put(servant, 1);

        try{
            currentState.handleAction(new PreliminaryPickAction("Bartolomeo", leaderToDiscard,
                    resources));
        }catch(FSMTransitionFailedException e){
            throw new RuntimeException(e.getMessage());
        }

        assertTrue(currentState.getNextState() instanceof PreliminaryTidyState);
        assertEquals(0, player2.getBoard().getFaithHolder().getFaithPoints());
        assertEquals(2, player2.getBoard().getLeaderCards().size());
        assertEquals(1, player2.getBoard().getStorage().getHand().totalAmountOfResources());
    }

    @Test
    public void thirdPlayerPick(){
        gameContext.setCurrentPlayer(model.getPlayerById("Teofila"));

        //List of leader to discard
        List<Integer> leaderToDiscard = new ArrayList<>();
        leaderToDiscard.add(0);
        leaderToDiscard.add(3);

        //Map of resources to get
        Map<ResourceSingle, Integer> resources = new HashMap<>();
        resources.put(gold, 1);

        try{
            currentState.handleAction(new PreliminaryPickAction("Teofila", leaderToDiscard,
                    resources));
        }catch(FSMTransitionFailedException e){
            throw new RuntimeException(e.getMessage());
        }

        assertTrue(currentState.getNextState() instanceof PreliminaryTidyState);
        assertEquals(1, player3.getBoard().getFaithHolder().getFaithPoints());
        assertEquals(2, player3.getBoard().getLeaderCards().size());
        assertEquals(1, player3.getBoard().getStorage().getHand().totalAmountOfResources());
    }

    @Test
    public void fourthPlayerPick(){
        gameContext.setCurrentPlayer(model.getPlayerById("Ottone"));

        //List of leader to discard
        List<Integer> leaderToDiscard = new ArrayList<>();
        leaderToDiscard.add(2);
        leaderToDiscard.add(3);

        //Map of resources to get
        Map<ResourceSingle, Integer> resources = new HashMap<>();
        resources.put(gold, 1);
        resources.put(stone, 1);

        try{
            currentState.handleAction(new PreliminaryPickAction("Ottone", leaderToDiscard,
                    resources));
        }catch(FSMTransitionFailedException e){
            throw new RuntimeException(e.getMessage());
        }

        assertTrue(currentState.getNextState() instanceof PreliminaryTidyState);
        assertEquals(1, player4.getBoard().getFaithHolder().getFaithPoints());
        assertEquals(2, player4.getBoard().getLeaderCards().size());
        assertEquals(2, player4.getBoard().getStorage().getHand().totalAmountOfResources());
    }

    @Test
    public void wrongPlayerPick(){
        gameContext.setCurrentPlayer(model.getPlayerById("Bartolomeo"));

        //List of leader to discard
        List<Integer> leaderToDiscard = new ArrayList<>();
        leaderToDiscard.add(0);
        leaderToDiscard.add(3);

        //Map of resources to get
        Map<ResourceSingle, Integer> resources = new HashMap<>();
        resources.put(gold, 1);

        assertThrows(FSMTransitionFailedException.class, ()-> currentState.handleAction(new PreliminaryPickAction(
                "Teofila", leaderToDiscard, resources
        )));
    }

    @Test
    public void tooManyResourcesPick(){
        gameContext.setCurrentPlayer(model.getPlayerById("Ottone"));

        //List of leader to discard
        List<Integer> leaderToDiscard = new ArrayList<>();
        leaderToDiscard.add(2);
        leaderToDiscard.add(3);

        //Map of resources to get
        Map<ResourceSingle, Integer> resources = new HashMap<>();
        resources.put(gold, 1);
        resources.put(stone, 1);
        resources.put(shield, 1);

        assertThrows(FSMTransitionFailedException.class, ()-> currentState.handleAction(new PreliminaryPickAction(
                "Ottone", leaderToDiscard, resources
        )));
    }

    @Test
    public void tooFewResourcesPick(){
        gameContext.setCurrentPlayer(model.getPlayerById("Ottone"));

        //List of leader to discard
        List<Integer> leaderToDiscard = new ArrayList<>();
        leaderToDiscard.add(1);
        leaderToDiscard.add(2);

        //Map of resources to get
        Map<ResourceSingle, Integer> resources = new HashMap<>();
        resources.put(gold, 1);

        assertThrows(FSMTransitionFailedException.class, ()-> currentState.handleAction(new PreliminaryPickAction(
                "Ottone", leaderToDiscard, resources
        )));
    }

    @Test
    public void tooManyLeadersPick(){
        gameContext.setCurrentPlayer(model.getPlayerById("Bartolomeo"));

        //List of leader to discard
        List<Integer> leaderToDiscard = new ArrayList<>();
        leaderToDiscard.add(0);
        leaderToDiscard.add(1);
        leaderToDiscard.add(2);

        //Map of resources to get
        Map<ResourceSingle, Integer> resources = new HashMap<>();
        resources.put(servant, 1);

        assertThrows(FSMTransitionFailedException.class, ()->currentState.handleAction(new PreliminaryPickAction(
                "Bartolomeo", leaderToDiscard, resources)));
    }

    @Test
    public void tooFewLeadersPick(){
        gameContext.setCurrentPlayer(model.getPlayerById("Bartolomeo"));

        //List of leader to discard
        List<Integer> leaderToDiscard = new ArrayList<>();
        leaderToDiscard.add(0);

        //Map of resources to get
        Map<ResourceSingle, Integer> resources = new HashMap<>();
        resources.put(servant, 1);

        assertThrows(FSMTransitionFailedException.class, ()->currentState.handleAction(new PreliminaryPickAction(
                "Bartolomeo", leaderToDiscard, resources)));
    }

    @Test
    public void invalidLeadersPick(){
        gameContext.setCurrentPlayer(model.getPlayerById("Bartolomeo"));

        //List of leader to discard
        List<Integer> leaderToDiscard = new ArrayList<>();
        leaderToDiscard.add(0);
        leaderToDiscard.add(5);

        //Map of resources to get
        Map<ResourceSingle, Integer> resources = new HashMap<>();
        resources.put(servant, 1);

        assertThrows(FSMTransitionFailedException.class, ()->currentState.handleAction(new PreliminaryPickAction(
                "Bartolomeo", leaderToDiscard, resources)));
    }

    @Test
    public void discardingSameLeaderTwice(){
        gameContext.setCurrentPlayer(model.getPlayerById("Bartolomeo"));

        //List of leader to discard
        List<Integer> leaderToDiscard = new ArrayList<>();
        leaderToDiscard.add(0);
        leaderToDiscard.add(0);

        //Map of resources to get
        Map<ResourceSingle, Integer> resources = new HashMap<>();
        resources.put(servant, 1);

        assertThrows(FSMTransitionFailedException.class, ()->currentState.handleAction(new PreliminaryPickAction(
                "Bartolomeo", leaderToDiscard, resources)));
    }


}
