package it.polimi.ingsw.server.clienthandling.setupactions;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.server.clienthandling.ClientHandler;

public class JoinMatchSetupAction implements SetupAction{

    @SerializedName(value = "match_name", alternate = "matchName")
    private final String matchName;

    public JoinMatchSetupAction(String matchName) {
        this.matchName = matchName;
    }

    @Override
    public void execute(ClientHandler clientHandler) {
        clientHandler.joinMatch(matchName);
    }

    @Override
    public void checkFormat() {
        if(matchName == null)
            throw new NullPointerException("No match name has been inserted");
    }

    public String getMatchName() {
        return matchName;
    }
}
