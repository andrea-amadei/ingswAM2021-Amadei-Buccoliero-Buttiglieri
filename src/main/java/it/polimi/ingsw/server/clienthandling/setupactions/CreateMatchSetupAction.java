package it.polimi.ingsw.server.clienthandling.setupactions;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.common.annotations.SerializedType;
import it.polimi.ingsw.server.clienthandling.ClientHandler;

@SerializedType("create_match")
public class CreateMatchSetupAction implements SetupAction{

    @SerializedName(value = "game_name", alternate = "gameName")
    private final String gameName;

    @SerializedName(value = "player_count", alternate = "playerCount")
    private final Integer playerCount;

    @SerializedName(value = "is_single_player", alternate = "isSinglePlayer")
    private final Boolean isSinglePlayer;

    public CreateMatchSetupAction(String gameName, Integer playerCount, Boolean isSinglePlayer) {
        this.gameName = gameName;
        this.playerCount = playerCount;
        this.isSinglePlayer = isSinglePlayer;
    }

    @Override
    public void execute(ClientHandler clientHandler) {
        clientHandler.createMatch(gameName, playerCount, isSinglePlayer);
    }

    @Override
    public void checkFormat() {
        if(gameName == null)
            throw new NullPointerException("game name not inserted");
        if(playerCount == null)
            throw new NullPointerException("player count not inserted");
        if(isSinglePlayer == null)
            throw new NullPointerException("isSinglePlayer not inserted");
    }

    public String getGameName() {
        return gameName;
    }

    public Integer getPlayerCount() {
        return playerCount;
    }

    public Boolean getSinglePlayer() {
        return isSinglePlayer;
    }
}
