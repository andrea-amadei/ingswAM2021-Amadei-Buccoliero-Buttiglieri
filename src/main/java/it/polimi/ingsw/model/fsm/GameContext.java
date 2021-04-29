package it.polimi.ingsw.model.fsm;

import it.polimi.ingsw.exceptions.CountdownException;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;

/**
 * The game context of this game. It contains the model and information about the evolution of the game
 */
public class GameContext {
    private final GameModel gameModel;
    private Player currentPlayer;
    private boolean playerMoved;
    private boolean countdownStarted;
    private boolean isSinglePlayer;
    private boolean isHardEndTriggered;
    private int turnsLeft;

    /**
     * Creates a new game context. Current player is initially null and by default the game is multiplayer
     * @param gameModel the model of the game
     * @throws NullPointerException if gameModel is null
     */
    public GameContext(GameModel gameModel){
        if(gameModel == null)
            throw new NullPointerException();

        this.gameModel = gameModel;
        currentPlayer = null;
        playerMoved = false;
        countdownStarted = false;
        turnsLeft = -1;
        isSinglePlayer = false;
        isHardEndTriggered = false;
    }
    /**
     * Creates a new game context specifying if the game is single player or multiplayer. Current player is initially null
     * @param gameModel the model of the game
     * @throws NullPointerException if gameModel is null
     */
    public GameContext(GameModel gameModel, boolean isSinglePlayer){
        this(gameModel);
        this.isSinglePlayer = isSinglePlayer;
    }

    /**
     * Returns the model of the game
     * @return the model of the game
     */
    public GameModel getGameModel() {return gameModel;}

    /**
     * Returns the current player
     * @return the current player
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Sets the current player for this turn
     * @param currentPlayer the current player of the game
     * @throws NullPointerException if currentPlayer is null
     * @throws IllegalArgumentException if the specified player is not part of the game
     */
    public void setCurrentPlayer(Player currentPlayer){
        if(currentPlayer == null)
            throw new NullPointerException();
        if(gameModel.getPlayers().stream().noneMatch(x -> x.equals(currentPlayer)))
            throw new IllegalArgumentException("The specified player is not part of the game");

        this.currentPlayer = currentPlayer;
        playerMoved = false;
    }

    /**
     * Returns true if the current player has already made a move, false otherwise
     * @return true if the current player has already made a move, false otherwise
     */
    public boolean hasPlayerMoved() {
        return playerMoved;
    }

    /**
     * Sets if the current player has already moved
     * @param playerMoved true if the current player has already made a move, false otherwise
     */
    public void setPlayerMoved(boolean playerMoved) {
        this.playerMoved = playerMoved;
    }

    /**
     * Returns true if the game is in single player mode
     * @return true if the game is in single player mode
     */
    public boolean isSinglePlayer(){ return isSinglePlayer;}

    /**
     * Starts a new turn countdown to mark the end of the game
     * @param turnsLeft the amount of turns (one for player) left to play
     * @throws IllegalArgumentException if turns left isn't positive
     * @throws CountdownException if the countdown was already running
     */
    public void startCountdown(int turnsLeft) {
        if(turnsLeft <= 0)
            throw new IllegalArgumentException("Turns left must be positive");

        if(countdownStarted)
            throw new CountdownException("Cannot start countdown while another is already in place");

        countdownStarted = true;
        this.turnsLeft = turnsLeft;
    }

    /**
     * Returns the amount of turns left
     * @return the amount of turns left
     * @throws CountdownException if no countdown is active or the countdown already ended
     */
    public int getTurnsLeft() {
        if(!countdownStarted)
            throw new CountdownException("Cannot advance countdown since no countdown is active");

        if(turnsLeft <= 0)
            throw new CountdownException("Cannot advance countdown since the countdown already ended");

        return turnsLeft;
    }

    /**
     * Returns true if the countdown already started, false otherwise
     * @return true if the countdown already started, false otherwise
     */
    public boolean hasCountdownStarted() {
        return countdownStarted;
    }

    /**
     * Returns true if the countdown already ended, false otherwise
     * @return true if the countdown already ended, false otherwise
     */
    public boolean hasCountdownEnded() {
        return countdownStarted && turnsLeft == 0;
    }

    /**
     * Advance the countdown by one
     * @return true if, after the advancement, the countdown ended; false otherwise
     * @throws CountdownException if no countdown is active or the countdown already ended
     */
    public boolean advanceCountdown() {
        if(!countdownStarted)
            throw new CountdownException("Cannot advance countdown since no countdown is active");

        if(turnsLeft <= 0)
            throw new CountdownException("Cannot advance countdown since the countdown already ended");

        turnsLeft--;
        return turnsLeft == 0;
    }

    /**
     * Returns true iff the hardEnd sequence is triggered
     * @return true iff the hardEnd sequence is triggered
     */
    public boolean isHardEndTriggered() {
        return isHardEndTriggered;
    }

    /**
     * Sets the hard end to true
     */
    public void setHardEnd(){
        isHardEndTriggered = true;
    }
}
