package it.polimi.ingsw;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSerializer;
import it.polimi.ingsw.common.ActionQueue;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.gamematerials.MarbleColor;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.actions.BuyFromMarketAction;
import it.polimi.ingsw.model.actions.ConfirmTidyAction;
import it.polimi.ingsw.model.actions.SelectConversionsAction;
import it.polimi.ingsw.model.actions.SelectPlayAction;
import it.polimi.ingsw.model.market.ConversionActuator;
import it.polimi.ingsw.model.market.MarbleFactory;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.parser.JSONSerializer;
import it.polimi.ingsw.server.MatchesManager;
import it.polimi.ingsw.server.clienthandling.ClientHandler;
import it.polimi.ingsw.server.clienthandling.Match;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.utils.PayloadFactory;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class App {
    public static void main(String[] args) throws GameNotStartedException, GameNotReadyException, IOException {
        MatchesManager matchesManager = new MatchesManager();
        Pair<String, ClientHandler> player1 = new Pair<>("Pippo", new ClientHandler(new Socket(), matchesManager));
        Pair<String, ClientHandler> player2 = new Pair<>("Alice", new ClientHandler(new Socket(), matchesManager));

        Match match = new Match("First game", player1, 2, false);
        try {
            match.addPlayer(player2);
        } catch (DuplicateUsernameException | GameNotInLobbyException e) {
            e.printStackTrace();
        }

        match.startGame();
        match.getStateMachine().getGameContext().setCurrentPlayer(match.getStateMachine().getGameContext().getGameModel().getPlayerById("Pippo"));
        ActionQueue actionQueue = match.getActionQueue();

        actionQueue.addAction(new SelectPlayAction("Pippo", SelectPlayAction.Play.MARKET), ActionQueue.Priority.CLIENT_ACTION.ordinal());
        actionQueue.addAction(new BuyFromMarketAction("Pippo", true, 1), 0);
        actionQueue.addAction(new SelectPlayAction("Alice", SelectPlayAction.Play.MARKET), ActionQueue.Priority.CLIENT_ACTION.ordinal());
        actionQueue.addAction(new SelectConversionsAction("Pippo", Arrays.asList(0, 0, 0, 0)), 0);
        actionQueue.addAction(new ConfirmTidyAction("Pippo"), 0);
        System.in.read();
    }
}
