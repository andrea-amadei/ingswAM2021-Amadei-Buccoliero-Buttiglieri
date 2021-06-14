package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.actions.ConfirmTidyAction;
import it.polimi.ingsw.model.actions.ResourcesMoveAction;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.fsm.State;
import it.polimi.ingsw.model.fsm.states.MenuState;
import it.polimi.ingsw.model.fsm.states.PreliminaryPickState;
import it.polimi.ingsw.model.fsm.states.PreliminaryTidyState;
import it.polimi.ingsw.server.ServerBuilder;
import it.polimi.ingsw.utils.ResourceLoader;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PreliminaryTidyStateTest {

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
        currentState = new PreliminaryTidyState(gameContext);

        player1 = model.getPlayerById("Ernestino");
        player2 = model.getPlayerById("Bartolomeo");
        player3 = model.getPlayerById("Teofila");
        player4 = model.getPlayerById("Ottone");

        //each player has 2 leader cards
        player1.getBoard().addLeaderCard(model.getLeaderCards().get(0));
        player1.getBoard().addLeaderCard(model.getLeaderCards().get(1));

        player2.getBoard().addLeaderCard(model.getLeaderCards().get(4));
        player2.getBoard().addLeaderCard(model.getLeaderCards().get(5));

        player3.getBoard().addLeaderCard(model.getLeaderCards().get(8));
        player3.getBoard().addLeaderCard(model.getLeaderCards().get(9));

        player4.getBoard().addLeaderCard(model.getLeaderCards().get(12));
        player4.getBoard().addLeaderCard(model.getLeaderCards().get(13));

        //first player hand is empty. He has 0 faith points

        //second player has 1 gold in his hand. He has 0 faith points.
        player2.getBoard().getStorage().getHand().addResources(gold, 1);

        //third player has 1 servant in her hand. She has 1 faith point.
        player3.getBoard().getStorage().getHand().addResources(servant, 1);
        model.getFaithPath().executeMovement(1, player3);

        //fourth player has 1 shield and 1 stone in his hand. He has 1 faith point.
        player4.getBoard().getStorage().getHand().addResources(shield, 1);
        player4.getBoard().getStorage().getHand().addResources(stone, 1);
        model.getFaithPath().executeMovement(1, player4);

    }

    @Test
    public void firstPlayer(){
        gameContext.setCurrentPlayer(model.getPlayerById("Ernestino"));

        //first player confirms tidy
        try{
            currentState.handleAction(new ConfirmTidyAction("Ernestino"));
        }catch (FSMTransitionFailedException e){
            throw new RuntimeException(e.getMessage());
        }

        assertTrue(currentState.getNextState() instanceof PreliminaryPickState);
        assertSame(gameContext.getCurrentPlayer(), player2);
    }

    @Test
    public void secondPlayer(){
        gameContext.setCurrentPlayer(model.getPlayerById("Bartolomeo"));

        //second player moves his resource to his bottom shelf
        try{
            currentState.handleAction(new ResourcesMoveAction("Bartolomeo", "Hand", "BottomShelf",
                    gold, 1));
        }catch (FSMTransitionFailedException e){
            throw new RuntimeException(e.getMessage());
        }

        //second player confirms tidy
        try{
            currentState.handleAction(new ConfirmTidyAction("Bartolomeo"));
        }catch (FSMTransitionFailedException e){
            throw new RuntimeException(e.getMessage());
        }

        assertTrue(currentState.getNextState() instanceof PreliminaryPickState);
        assertSame(gameContext.getCurrentPlayer(), player3);
        assertEquals(0, player2.getBoard().getStorage().getHand().totalAmountOfResources());
    }

    @Test
    public void thirdPlayer(){
        gameContext.setCurrentPlayer(model.getPlayerById("Teofila"));

        //third player moves her resource to her middle shelf
        try{
            currentState.handleAction(new ResourcesMoveAction("Teofila", "Hand", "MiddleShelf",
                    servant, 1));
        }catch (FSMTransitionFailedException e){
            throw new RuntimeException(e.getMessage());
        }

        //third player confirms tidy
        try{
            currentState.handleAction(new ConfirmTidyAction("Teofila"));
        }catch (FSMTransitionFailedException e){
            throw new RuntimeException(e.getMessage());
        }

        assertTrue(currentState.getNextState() instanceof PreliminaryPickState);
        assertSame(gameContext.getCurrentPlayer(), player4);
        assertEquals(0, player3.getBoard().getStorage().getHand().totalAmountOfResources());
    }

    @Test
    public void fourthPlayer(){
        gameContext.setCurrentPlayer(model.getPlayerById("Ottone"));
        gameContext.addPlayerWhoPicked(model.getPlayerById("Ottone"));

        //fourth player moves his resource to his middle shelf and top shelf
        try{
            currentState.handleAction(new ResourcesMoveAction("Ottone", "Hand", "MiddleShelf",
                    shield, 1));
        }catch (FSMTransitionFailedException e){
            throw new RuntimeException(e.getMessage());
        }
        try{
            currentState.handleAction(new ResourcesMoveAction("Ottone", "Hand", "TopShelf",
                    stone, 1));
        }catch (FSMTransitionFailedException e){
            throw new RuntimeException(e.getMessage());
        }

        //fourth player confirms tidy
        try{
            currentState.handleAction(new ConfirmTidyAction("Ottone"));
        }catch (FSMTransitionFailedException e){
            throw new RuntimeException(e.getMessage());
        }

        assertTrue(currentState.getNextState() instanceof MenuState);
        assertSame(gameContext.getCurrentPlayer(), player1);
        assertEquals(0, player4.getBoard().getStorage().getHand().totalAmountOfResources());
    }

    @Test
    public void confirmWithoutEmptyHand(){
        gameContext.setCurrentPlayer(model.getPlayerById("Ottone"));

        //fourth player moves only part of his resources
        try{
            currentState.handleAction(new ResourcesMoveAction("Ottone", "Hand", "MiddleShelf",
                    shield, 1));
        }catch (FSMTransitionFailedException e){
            throw new RuntimeException(e.getMessage());
        }

        assertThrows(FSMTransitionFailedException.class, ()-> currentState.handleAction(
                new ConfirmTidyAction("Ottone")));
    }

    @Test
    public void movingNonExistentResources(){
        gameContext.setCurrentPlayer(model.getPlayerById("Ottone"));

        assertThrows(FSMTransitionFailedException.class, ()-> currentState.handleAction(new ResourcesMoveAction(
                "Ottone", "Hand", "MiddleShelf", gold, 1)));
    }

    @Test
    public void confirmFromWrongPlayer(){
        gameContext.setCurrentPlayer(model.getPlayerById("Teofila"));

        //third player moves her resource to her middle shelf
        try{
            currentState.handleAction(new ResourcesMoveAction("Teofila", "Hand", "MiddleShelf",
                    servant, 1));
        }catch (FSMTransitionFailedException e){
            throw new RuntimeException(e.getMessage());
        }

        //fourth player confirms action
        assertThrows(FSMTransitionFailedException.class, ()-> currentState.handleAction(new ConfirmTidyAction(
                "Ottone")));
    }

    @Test
    public void lastPlayerDisconnected(){
        //last player is disconnected
        model.getPlayers().get(3).setConnected(false);

        //current player is third player
        gameContext.setCurrentPlayer(model.getPlayerById("Teofila"));

        //third player moves her resource to her middle shelf
        try{
            currentState.handleAction(new ResourcesMoveAction("Teofila", "Hand", "MiddleShelf",
                    servant, 1));
        }catch (FSMTransitionFailedException e){
            throw new RuntimeException(e.getMessage());
        }

        //third player confirms tidy
        try{
            currentState.handleAction(new ConfirmTidyAction("Teofila"));
        }catch (FSMTransitionFailedException e){
            throw new RuntimeException(e.getMessage());
        }

        assertTrue(currentState.getNextState() instanceof MenuState);
        assertSame(gameContext.getCurrentPlayer(), player1);
    }

    @Test
    public void twoPlayersDisconnected(){
        //second and third player are disconnected
        model.getPlayers().get(1).setConnected(false);
        model.getPlayers().get(2).setConnected(false);

        //current player is first player
        gameContext.setCurrentPlayer(model.getPlayerById("Ernestino"));

        //first player confirms tidy
        try{
            currentState.handleAction(new ConfirmTidyAction("Ernestino"));
        }catch (FSMTransitionFailedException e){
            throw new RuntimeException(e.getMessage());
        }

        //assess next player is fourth player
        assertTrue(currentState.getNextState() instanceof PreliminaryPickState);
        assertSame(gameContext.getCurrentPlayer(), player4);
    }

    @Test
    public void onlyOnePlayerConnected(){
        //second, third and fourth player are disconnected
        model.getPlayers().get(1).setConnected(false);
        model.getPlayers().get(2).setConnected(false);
        model.getPlayers().get(3).setConnected(false);

        //current player is first player
        gameContext.setCurrentPlayer(model.getPlayerById("Ernestino"));

        //first player confirms tidy
        try{
            currentState.handleAction(new ConfirmTidyAction("Ernestino"));
        }catch (FSMTransitionFailedException e){
            throw new RuntimeException(e.getMessage());
        }

        //assess next player is still first player and next state is MenuState
        assertTrue(currentState.getNextState() instanceof MenuState);
        assertSame(gameContext.getCurrentPlayer(), player1);
    }



}
