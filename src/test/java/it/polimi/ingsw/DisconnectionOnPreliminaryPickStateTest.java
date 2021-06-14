package it.polimi.ingsw;
import it.polimi.ingsw.common.ActionQueue;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.actions.DisconnectPlayerAction;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.fsm.StateMachine;
import it.polimi.ingsw.model.fsm.states.MenuState;
import it.polimi.ingsw.model.fsm.states.PreliminaryPickState;
import it.polimi.ingsw.model.leader.LeaderCard;
import it.polimi.ingsw.model.lorenzo.Token;
import it.polimi.ingsw.server.ServerBuilder;
import it.polimi.ingsw.utils.GameUtilities;
import it.polimi.ingsw.utils.ResourceLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DisconnectionOnPreliminaryPickStateTest {

    private GameContext gameContext;
    private StateMachine stateMachine;

    @BeforeEach
    public void init() throws ParserException, IOException {
        List<String> playersNames = Arrays.asList("Naruto", "Sasuke", "Sakura", "Gaara");


        String configJSON = Files.readString(ResourceLoader.getPathFromResource("cfg/config.json"));
        String craftingJSON = Files.readString(ResourceLoader.getPathFromResource("cfg/crafting.json"));
        String faithJSON = Files.readString(ResourceLoader.getPathFromResource("cfg/faith.json"));
        String leadersJSON = Files.readString(ResourceLoader.getPathFromResource("cfg/leaders.json"));

        GameModel model = ServerBuilder.buildModel(configJSON, craftingJSON, faithJSON, leadersJSON, playersNames, false,
                new Random(5));
        gameContext = new GameContext(model, false);
        ActionQueue actionQueue = new ActionQueue();

        stateMachine = new StateMachine(actionQueue, gameContext, new PreliminaryPickState(gameContext));
        model.getFaithPath().setListener(stateMachine);
        for(Token t : model.getLorenzoTokens())
            t.setListener(stateMachine);

        gameContext.setCurrentPlayer(model.getPlayerById("Naruto"));

        //leader cards
        List<LeaderCard> leaders = gameContext.getGameModel().getLeaderCards();

        int i = 0;
        for(Player p : gameContext.getGameModel().getPlayers()){
            for(int j = 0; j < 4; j++){
                p.getBoard().addLeaderCard(leaders.get(i));
                i++;
            }
        }
    }

    @Test
    public void disconnectionByNonCurrentPlayer(){
        stateMachine.executeAction(new DisconnectPlayerAction("Sasuke"));

        assertEquals("Naruto", gameContext.getCurrentPlayer().getUsername());
        assertFalse(gameContext.getGameModel().getPlayerById("Sasuke").isConnected());
        assertEquals("Sakura", GameUtilities.calculateNextConnectedPlayer(gameContext).getUsername());
        assertTrue(stateMachine.getCurrentState() instanceof PreliminaryPickState);
    }

    @Test
    public void disconnectionByCurrentAndLastPlayer(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Sakura"));
        gameContext.getGameModel().getPlayerById("Gaara").setConnected(false);

        stateMachine.executeAction(new DisconnectPlayerAction("Sakura"));

        assertEquals("Naruto", gameContext.getCurrentPlayer().getUsername());
        assertTrue(stateMachine.getCurrentState() instanceof MenuState);
        for(Player p : gameContext.getGameModel().getPlayers()){
            assertEquals(2, p.getBoard().getLeaderCards().size());
        }
    }

    @Test
    public void disconnectionByCurrentNotLastPlayer(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Sakura"));

        stateMachine.executeAction(new DisconnectPlayerAction("Sakura"));

        assertEquals("Gaara", gameContext.getCurrentPlayer().getUsername());
        assertTrue(stateMachine.getCurrentState() instanceof PreliminaryPickState);
        assertEquals(4, gameContext.getGameModel().getPlayerById("Sakura").getBoard().getLeaderCards().size());
    }

    @Test
    public void everyoneDisconnects(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Sakura"));
        for(Player p : gameContext.getGameModel().getPlayers()) {
            if(!p.getUsername().equals("Sakura"))
                p.setConnected(false);
        }

        stateMachine.executeAction(new DisconnectPlayerAction("Sakura"));

        assertTrue(stateMachine.getCurrentState() instanceof PreliminaryPickState);
    }

}
