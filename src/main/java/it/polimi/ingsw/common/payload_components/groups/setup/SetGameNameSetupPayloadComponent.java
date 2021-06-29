package it.polimi.ingsw.common.payload_components.groups.setup;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.common.annotations.SerializedType;

@SerializedType("set_game_name")
public class SetGameNameSetupPayloadComponent extends SetupPayloadComponent{

    @SerializedName(value = "game_name", alternate = "gameName")
    private final String gameName;

    public SetGameNameSetupPayloadComponent(String gameName){
        this.gameName = gameName;
    }

    public String getGameName() {
        return gameName;
    }
}
