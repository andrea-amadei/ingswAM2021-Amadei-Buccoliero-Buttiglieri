package it.polimi.ingsw.model.fsm.states;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.actions.EndGameAction;
import it.polimi.ingsw.model.actions.LorenzoMoveAction;
import it.polimi.ingsw.model.actions.NextTurnAction;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.fsm.State;

import java.util.ArrayList;
import java.util.List;

public class EndTurnState extends State {

    private boolean hasLorenzoPlayed;
    /**
     * A new State is created with the provided game context. The nextState and the interruptListener
     * are both initially null
     *
     * @param gameContext the context of the game
     * @throws NullPointerException if gameContext is null
     */
    public EndTurnState(GameContext gameContext) {
        super(gameContext);
        hasLorenzoPlayed = false;
    }

    /**
     * Executed everytime the fsm enters in this state.
     * If the game mode is single player and Lorenzo's play has not been launched, Lorenzo's play is launched.
     * Otherwise the next (connected) player is computed.
     * If no player is connected, nothing is done.
     * If the winning conditions are met, EndGameAction is launched, otherwise the NextTurnAction is launched.
     * @return the list of messages to be sent to the client
     */
    @Override
    public List<Message> onEntry() {
        if(getGameContext().isSinglePlayer() && !hasLorenzoPlayed && !getGameContext().hasCountdownStarted()){
            launchInterrupt(new LorenzoMoveAction());
            hasLorenzoPlayed = true;
            return new ArrayList<>();
        }
        //compute the next connected player

        List<Player> players = getGameContext().getGameModel().getPlayers();
        if(players.stream().anyMatch(Player::isConnected)) {
            int currentPlayerIndex = players.indexOf(getGameContext().getCurrentPlayer());
            int nextPlayerIndex = currentPlayerIndex;

            //find the next connected player
            for (int i = 0; i < players.size() - 1; i++) {
                int nextPlayerCandidateIndex = (currentPlayerIndex + (i + 1)) % players.size();
                if (players.get(nextPlayerCandidateIndex).isConnected()) {
                    nextPlayerIndex = nextPlayerCandidateIndex;
                    break;
                }
            }
            //if the end of the list of players has been reached and the end game sequence has been
            //triggered, then the game is ended
            if (nextPlayerIndex <= currentPlayerIndex && getGameContext().hasCountdownStarted()) {
                launchInterrupt(new EndGameAction());
                return new ArrayList<>();
            }

            //else we can proceed with the next turn
            getGameContext().setCurrentPlayer(players.get(nextPlayerIndex));
            launchInterrupt(new NextTurnAction());
            return new ArrayList<>();
        }

        return new ArrayList<>();

    }


    /**
     * The game goes to the EndGameState
     * @param endGameAction the action to be executed
     * @return the list of messages to be sent to the client
     * @throws NullPointerException         if the action is null
     * @throws FSMTransitionFailedException if the action cannot be handled or failed
     */
    @Override
    public List<Message> handleAction(EndGameAction endGameAction) throws FSMTransitionFailedException {
        if(endGameAction == null)
            throw new NullPointerException();

        List<Message> messages;
        try{
            messages = new ArrayList<>(endGameAction.execute(getGameContext()));
        }catch(IllegalActionException e){
            throw new FSMTransitionFailedException(e.getMessage());
        }

        setNextState(new EndGameState(getGameContext()));

        return messages;
    }

    /**
     * The Lorenzo's move is executed.
     * An interrupt may be raised here.
     * If the action can be performed (it should be since it is an internal action), next state is this state.
     * @param lorenzoMoveAction the action to be executed
     * @return the list of messages to be sent to the client
     * @throws NullPointerException         if the action is null
     * @throws FSMTransitionFailedException if the action cannot be handled or failed
     */
    @Override
    public List<Message> handleAction(LorenzoMoveAction lorenzoMoveAction) throws FSMTransitionFailedException {
        if(lorenzoMoveAction == null)
            throw new NullPointerException();

        List<Message> messages;
        try{
            messages = new ArrayList<>(lorenzoMoveAction.execute(getGameContext()));
        }catch(IllegalActionException e){
            throw new FSMTransitionFailedException(e.getMessage());
        }

        setNextState(this);

        return messages;
    }

    /**
     * A new turn begins.
     * If the action can be performed, the next state is MenuState
     * @param nextTurnAction the action to be executed
     * @return the list of messages to be sent to the client
     * @throws NullPointerException         if the action is null
     * @throws FSMTransitionFailedException if the action cannot be handled or failed
     */
    @Override
    public List<Message> handleAction(NextTurnAction nextTurnAction) throws FSMTransitionFailedException {
        if(nextTurnAction == null)
            throw new NullPointerException();

        List<Message> messages;
        try{
            messages = new ArrayList<>(nextTurnAction.execute(getGameContext()));
        }catch(IllegalActionException e){
            throw new FSMTransitionFailedException(e.getMessage());
        }

        setNextState(new MenuState(getGameContext()));

        return messages;
    }
}
