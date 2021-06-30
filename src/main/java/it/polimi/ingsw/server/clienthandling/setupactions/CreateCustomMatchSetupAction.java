package it.polimi.ingsw.server.clienthandling.setupactions;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.server.clienthandling.ClientHandler;

public class CreateCustomMatchSetupAction implements SetupAction{

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

    public CreateCustomMatchSetupAction(String gameName, Integer playerCount, Boolean isSinglePlayer, String config, String crafting, String faith, String leaders) {
        this.gameName = gameName;
        this.playerCount = playerCount;
        this.isSinglePlayer = isSinglePlayer;
        this.config = config;
        this.crafting = crafting;
        this.faith = faith;
        this.leaders = leaders;
        checkFormat();
    }

    @Override
    public void execute(ClientHandler clientHandler) {
        clientHandler.createCustomMatch(gameName, playerCount, isSinglePlayer, config, crafting, faith, leaders);
    }

    @Override
    public void checkFormat() {
        if(gameName == null || playerCount == null || isSinglePlayer == null || config == null || crafting == null || faith == null || leaders == null)
            throw new IllegalArgumentException("parameters missing");
    }
}
