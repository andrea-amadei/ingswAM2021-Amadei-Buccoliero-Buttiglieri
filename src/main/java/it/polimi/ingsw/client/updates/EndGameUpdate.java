package it.polimi.ingsw.client.updates;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.client.clientmodel.ClientModel;

import java.util.List;

public class EndGameUpdate implements Update{

    @SerializedName(value = "has_lorenzo_won", alternate = "hasLorenzoWon")
    private final Boolean hasLorenzoWon;

    private final List<String> usernames;
    private final List<Integer> points;

    public EndGameUpdate(boolean hasLorenzoWon, List<String> usernames, List<Integer> points) {
        this.hasLorenzoWon = hasLorenzoWon;
        this.usernames = usernames;
        this.points = points;
        checkFormat();
    }

    @Override
    public void apply(ClientModel client) {
        client.getEndGameResults().endGame(hasLorenzoWon, usernames, points);
    }

    @Override
    public void checkFormat() {
        if(hasLorenzoWon == null)
            throw new NullPointerException("Missing 'hasLorenzoWon' attribute");
        if(usernames == null)
            throw new NullPointerException("Missing 'usernames' attribute");
        if(points == null)
            throw new NullPointerException("Missing 'points' attribute");
    }
}
