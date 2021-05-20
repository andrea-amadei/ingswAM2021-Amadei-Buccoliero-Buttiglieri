package it.polimi.ingsw.client.updates;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.client.model.ClientModel;

public class SetGameNameUpdate implements Update{

    @SerializedName(value = "game_name", alternate = "gameName")
    private final String gameName;

    public SetGameNameUpdate(String gameName){
        this.gameName = gameName;
    }

    @Override
    public void apply(ClientModel client) {
        client.getPersonalData().setGameName(getGameName());
        client.getOutputHandler().update();
    }

    @Override
    public void checkFormat() {
        if(gameName == null)
            throw new NullPointerException("Game name is null");
    }

    public String getGameName() {
        return gameName;
    }
}
