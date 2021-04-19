package it.polimi.ingsw.model.fsm;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;

/**
 * The game context of this game. It contains the model and information about the evolution of the game
 */
public class GameContext {
    private final GameModel gameModel;
    private Player currentPlayer;
    private boolean playerMoved;

    /**
     * Creates a new game context. Current player is initially null
     * @param gameModel the model of the game
     * @throws NullPointerException if gameModel is null
     */
    public GameContext(GameModel gameModel){
        if(gameModel == null)
            throw new NullPointerException();
        this.gameModel = gameModel;
        currentPlayer = null;
        playerMoved = false;
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
    }

    public boolean hasPlayerMoved() {
        return playerMoved;
    }

    public void setPlayerMoved(boolean playerMoved) {
        this.playerMoved = playerMoved;
    }
}
