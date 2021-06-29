package it.polimi.ingsw;

import it.polimi.ingsw.common.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.common.exceptions.ParserException;
import it.polimi.ingsw.server.model.basetypes.ResourceSingle;
import it.polimi.ingsw.server.model.basetypes.ResourceTypeSingleton;
import it.polimi.ingsw.server.model.global.GameModel;
import it.polimi.ingsw.server.model.global.Player;
import it.polimi.ingsw.server.model.actions.*;
import it.polimi.ingsw.server.model.fsm.GameContext;
import it.polimi.ingsw.server.model.fsm.State;
import it.polimi.ingsw.server.model.fsm.states.MenuState;
import it.polimi.ingsw.server.model.fsm.states.PreliminaryPickState;
import it.polimi.ingsw.server.model.fsm.states.PreliminaryTidyState;
import it.polimi.ingsw.server.ServerBuilder;
import it.polimi.ingsw.common.utils.ResourceLoader;
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

    //first player successfully goes through preliminaryTidy state
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

    //second player successfully goes through preliminaryTidy state
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

    //third player successfully goes through preliminaryTidy state
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

    //fourth player successfully goes through preliminaryTidy state
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

    //method "handleAction" throws FSMTransitionFailedException if a player tries to confirm without having emptied their hand first
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

    //method "handleAction" throws FSMTransitionFailedException if a player tries to move a resource they do not own
    @Test
    public void movingNonExistentResources(){
        gameContext.setCurrentPlayer(model.getPlayerById("Ottone"));

        assertThrows(FSMTransitionFailedException.class, ()-> currentState.handleAction(new ResourcesMoveAction(
                "Ottone", "Hand", "MiddleShelf", gold, 1)));
    }

    //method "handleAction" throws FSMTransitionFailedException if the confirm request comes from a player who is not the current player
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

    //method "handleAction" successfully manages the disconnection of a player who is not the current player
    @Test
    public void lastPlayerDisconnected(){
        //last player is disconnected
        model.getPlayers().get(3).setConnected(false);

        //current player is the third player
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

    //method "handleAction" successfully manages the disconnection two players whose turns are after the current player
    @Test
    public void twoPlayersDisconnected(){
        //second and third player are disconnected
        model.getPlayers().get(1).setConnected(false);
        model.getPlayers().get(2).setConnected(false);

        //current player is the first player
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

    //method "handleAction" successfully manages the disconnection of every player but one
    @Test
    public void onlyOnePlayerConnected(){
        //second, third and fourth player are disconnected
        model.getPlayers().get(1).setConnected(false);
        model.getPlayers().get(2).setConnected(false);
        model.getPlayers().get(3).setConnected(false);

        //current player is the first player
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

    //method "handleAction" successfully manages the reconnection of a player who is not the current player
    @Test
    public void playerReconnects(){
        //third player is disconnected
        model.getPlayers().get(2).setConnected(false);

        //current player is the first player
        gameContext.setCurrentPlayer(model.getPlayerById("Ernestino"));

        try {
            currentState.handleAction(new ReconnectPlayerAction("Teofila"));
        } catch (FSMTransitionFailedException e) {
            throw new RuntimeException();
        }

        assertTrue(gameContext.getGameModel().getPlayerById("Teofila").isConnected());
    }

    //method "handleAction" successfully manages the reconnection of a player, after the game was stalling
    @Test
    public void reconnectionAfterStall(){
        //current player is the second player
        gameContext.setCurrentPlayer(model.getPlayerById("Bartolomeo"));

        //all player are disconnected
        model.getPlayers().get(0).setConnected(false);
        model.getPlayers().get(1).setConnected(false);
        model.getPlayers().get(2).setConnected(false);
        model.getPlayers().get(3).setConnected(false);

        //fourth player reconnects
        try {
            currentState.handleAction(new ReconnectPlayerAction("Ottone"));
        } catch (FSMTransitionFailedException e) {
            throw new RuntimeException();
        }

        assertEquals("Ottone", gameContext.getCurrentPlayer().getUsername());
        assertTrue(currentState.getNextState() instanceof PreliminaryPickState);
    }

    //method "handleAction" successfully manages the reconnection of a player, after the game was stalling.
    //The reconnecting player comes before the old current player, therefore next state is set to MenuState
    @Test
    public void reconnectionAfterStall1(){
        //current player is the second player
        gameContext.setCurrentPlayer(model.getPlayerById("Bartolomeo"));

        //all player are disconnected
        model.getPlayers().get(0).setConnected(false);
        model.getPlayers().get(1).setConnected(false);
        model.getPlayers().get(2).setConnected(false);
        model.getPlayers().get(3).setConnected(false);

        //first player reconnects
        try {
            currentState.handleAction(new ReconnectPlayerAction("Ernestino"));
        } catch (FSMTransitionFailedException e) {
            throw new RuntimeException();
        }

        assertEquals("Ernestino", gameContext.getCurrentPlayer().getUsername());
        assertTrue(currentState.getNextState() instanceof MenuState);
    }

    //method "handleAction" successfully manages the reconnection of a player, after the game was stalling.
    //The reconnecting player is the old current player, therefore next state is set to this.
    @Test
    public void reconnectionAfterStall2(){
        //current player is the second player
        gameContext.setCurrentPlayer(model.getPlayerById("Bartolomeo"));

        //all player are disconnected
        model.getPlayers().get(0).setConnected(false);
        model.getPlayers().get(1).setConnected(false);
        model.getPlayers().get(2).setConnected(false);
        model.getPlayers().get(3).setConnected(false);

        //second player reconnects
        try {
            currentState.handleAction(new ReconnectPlayerAction("Bartolomeo"));
        } catch (FSMTransitionFailedException e) {
            throw new RuntimeException();
        }

        assertEquals("Bartolomeo", gameContext.getCurrentPlayer().getUsername());
        assertTrue(currentState.getNextState() instanceof PreliminaryTidyState);
    }

    //method "handleAction" throws NullPointerException if the parameter "resourcesMoveAction" is null
    @Test
    public void nullResourcesMoveAction(){
        assertThrows(NullPointerException.class, ()-> currentState.handleAction((ResourcesMoveAction) null));
    }

    //method "handleAction" throws NullPointerException if the parameter "confirmTidyAction" is null
    @Test
    public void nullConfirmTidyAction(){
        assertThrows(NullPointerException.class, ()-> currentState.handleAction((ConfirmTidyAction) null));
    }

    //method "handleAction" throws NullPointerException if the parameter "disconnectPlayerAction" is null
    @Test
    public void nullDisconnectAction(){
        assertThrows(NullPointerException.class, ()-> currentState.handleAction((DisconnectPlayerAction) null));
    }

    //method "toString" returns the correct value
    @Test
    public void toStringTest(){
        assertEquals("PreliminaryTidyState", currentState.toString());
    }

}
