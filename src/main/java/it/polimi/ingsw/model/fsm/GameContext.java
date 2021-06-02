package it.polimi.ingsw.model.fsm;

import it.polimi.ingsw.common.GameConfig;
import it.polimi.ingsw.exceptions.CountdownException;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.parser.JSONParser;
import it.polimi.ingsw.utils.ResourceReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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

    private final String configJson;
    private final String craftingJson;
    private final String faithJson;
    private final String leadersJson;
    private final GameConfig gameConfig;

    /**
     * Creates a new game context. Current player is initially null and by default the game is multiplayer
     * @param gameModel the model of the game
     * @throws NullPointerException if gameModel is null
     * @throws RuntimeException if the hardcoded paths are not valid/cannot be read
     */
    public GameContext(GameModel gameModel){
        if(gameModel == null)
            throw new NullPointerException();

        this.gameModel = gameModel;
        currentPlayer = null;
        playerMoved = false;
        countdownStarted = false;
        isSinglePlayer = false;
        isHardEndTriggered = false;

        //TODO: probably we will change these paths and is it ok to throw an exception here?
        try {
            configJson = Files.readString(ResourceReader.getPathFromResource("cfg/config.json"));
            craftingJson = Files.readString(ResourceReader.getPathFromResource("cfg/crafting.json"));
            faithJson = Files.readString(ResourceReader.getPathFromResource("cfg/faith.json"));
            leadersJson = Files.readString(ResourceReader.getPathFromResource("cfg/leaders.json"));
        }catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Wrong path to json files/cannot read default json files");
        }

        this.gameConfig = JSONParser.getGameConfig(configJson);
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
     */
    public void startCountdown() {
        countdownStarted = true;
    }

    /**
     * Returns true if the countdown already started, false otherwise
     * @return true if the countdown already started, false otherwise
     */
    public boolean hasCountdownStarted() {
        return countdownStarted;
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

    public String getConfigJson() {
        return configJson;
    }

    public String getCraftingJson() {
        return craftingJson;
    }

    public String getFaithJson() {
        return faithJson;
    }

    public String getLeadersJson() {
        return leadersJson;
    }

    public GameConfig getGameConfig() {
        return gameConfig;
    }
}
