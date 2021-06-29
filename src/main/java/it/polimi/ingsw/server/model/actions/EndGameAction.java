package it.polimi.ingsw.server.model.actions;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.InfoPayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.updates.EndGameResultsUpdatePayloadComponent;
import it.polimi.ingsw.common.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.common.exceptions.IllegalActionException;
import it.polimi.ingsw.server.model.basetypes.ResourceSingle;
import it.polimi.ingsw.server.model.global.Player;
import it.polimi.ingsw.server.model.fsm.ActionHandler;
import it.polimi.ingsw.server.model.fsm.GameContext;
import it.polimi.ingsw.common.utils.Triplet;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class implements interface Action. This action informs the state machine that the game is ended.
 */
public class EndGameAction implements Action{
    /**
     * Calls the appropriate method of the handler
     *
     * @param handler the handler that will execute this action
     * @return the list of messages to send to the client
     * @throws NullPointerException         if handler is null
     * @throws FSMTransitionFailedException if the state fails to execute this action
     */
    @Override
    public List<Message> acceptHandler(ActionHandler handler) throws FSMTransitionFailedException {
        if(handler == null)
            throw new NullPointerException();
        return handler.handleAction(this);
    }

    /**
     * Informs the state machine that the game is ended.
     *
     * @param gameContext the current context of the game
     * @return a list of messages that contains information about the change applied to the model
     * @throws IllegalActionException if the action cannot be performed on the model
     * @throws NullPointerException   if gameContext is null
     */
    @Override
    public List<Message> execute(GameContext gameContext) throws IllegalActionException {
        if(gameContext == null)
            throw new NullPointerException();

        List<PayloadComponent> payload = new ArrayList<>();
        gameContext.setGameEnded();

        //each triplet contains (username, points, arrivalId)
        List<Triplet<String, Integer, Integer>> finalScores = new ArrayList<>();

        //for each player calculate the final score
        for(Player p : gameContext.getGameModel().getPlayers()){
            Map<ResourceSingle, Integer> storedResources = p.getBoard().getStorage().getStoredResources();
            int resourceCount = storedResources.values().stream().reduce(0, Integer::sum);

            int bonus = resourceCount / 5;

            finalScores.add(new Triplet<>(p.getUsername(), p.getPoints() + bonus, p.getArrivalId()));
        }
        finalScores.sort((a, b) -> {
            if (a.getSecond() > b.getSecond()) return -1;
            if (a.getSecond().equals(b.getSecond()) && a.getThird() < b.getThird()) return -1;
            if(a.getSecond().equals(b.getSecond()) && a.getThird().equals(b.getThird())) return 0;
            return 1;
        });

        payload.add(new InfoPayloadComponent("Game is ended"));

        List<String> usernames = finalScores.stream().map(Triplet::getFirst).collect(Collectors.toList());
        List<Integer> scores = finalScores.stream().map(Triplet::getSecond).collect(Collectors.toList());
        payload.add(new EndGameResultsUpdatePayloadComponent(gameContext.hasLorenzoWon(), usernames, scores));

        return Collections.singletonList(new Message(gameContext.getGameModel().getPlayerNames(),
                payload));
    }

    /**
     * Returns the sender of this action
     *
     * @return the sender of this action
     */
    @Override
    public String getSender() {
        return "AI";
    }

    /**
     * Checks if all attributes are set and have meaningful values.
     * In case they are not, this throws the appropriate RuntimeException.
     * It needs to be used since this class can be created by deserialization
     */
    @Override
    public void checkFormat() {

    }
}
