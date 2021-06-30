package it.polimi.ingsw.common.payload_components.groups.setup;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.common.annotations.SerializedType;

@SerializedType("create_custom_match")
public class CreateCustomMatchSetupPayloadComponent extends SetupPayloadComponent{
    @SerializedName(value = "game_name", alternate = "gameName")
    private final String gameName;

    @SerializedName(value = "player_count", alternate = "playerCount")
    private final Integer playerCount;

    @SerializedName(value = "is_single_player", alternate = "isSinglePlayer")
    private final Boolean isSinglePlayer;

    private final String config;

    private final String crafting;

    private final String faith;

    private final String leaders;

    public CreateCustomMatchSetupPayloadComponent(String gameName, Integer playerCount, Boolean isSinglePlayer, String config, String crafting, String faith, String leaders) {
        this.gameName = gameName;
        this.playerCount = playerCount;
        this.isSinglePlayer = isSinglePlayer;
        this.config = config;
        this.crafting = crafting;
        this.faith = faith;
        this.leaders = leaders;
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

    public String getConfig() {
        return config;
    }

    public String getCrafting() {
        return crafting;
    }

    public String getFaith() {
        return faith;
    }

    public String getLeaders() {
        return leaders;
    }
}
