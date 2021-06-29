package it.polimi.ingsw.common.payload_components.groups.setup;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.common.annotations.SerializedType;

@SerializedType("create_match")
public class CreateMatchSetupPayloadComponent extends SetupPayloadComponent{
    @SerializedName(value = "game_name", alternate = "gameName")
    private final String gameName;

    @SerializedName(value = "player_count", alternate = "playerCount")
    private final Integer playerCount;

    @SerializedName(value = "is_single_player", alternate = "isSinglePlayer")
    private final Boolean isSinglePlayer;

    public CreateMatchSetupPayloadComponent(String gameName, Integer playerCount, Boolean isSinglePlayer) {
        this.gameName = gameName;
        this.playerCount = playerCount;
        this.isSinglePlayer = isSinglePlayer;
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
