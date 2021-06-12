package it.polimi.ingsw.common.payload_components.groups.updates;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.annotations.SerializedGroup;
import it.polimi.ingsw.annotations.SerializedType;
import it.polimi.ingsw.common.payload_components.groups.UpdatePayloadComponent;

import java.util.List;

@SerializedGroup("update")
@SerializedType("end_game_results")
public class EndGameResultsUpdatePayloadComponent implements UpdatePayloadComponent {

    @SerializedName(value = "has_lorenzo_won", alternate = "hasLorenzoWon")
    private final boolean hasLorenzoWon;

    private final List<String> usernames;
    private final List<Integer> points;

    public EndGameResultsUpdatePayloadComponent(boolean hasLorenzoWon, List<String> usernames, List<Integer> points){
        this.hasLorenzoWon = hasLorenzoWon;
        this.usernames = usernames;
        this.points = points;
    }

    public boolean isHasLorenzoWon() {
        return hasLorenzoWon;
    }

    public List<String> getUsernames() {
        return usernames;
    }

    public List<Integer> getPoints() {
        return points;
    }
}
