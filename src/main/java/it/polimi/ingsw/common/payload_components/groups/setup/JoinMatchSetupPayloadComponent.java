package it.polimi.ingsw.common.payload_components.groups.setup;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.annotations.SerializedType;

@SerializedType("join_match")
public class JoinMatchSetupPayloadComponent extends SetupPayloadComponent{

    @SerializedName(value = "match_name", alternate = "matchName")
    private final String matchName;

    public String getMatchName() {
        return matchName;
    }

    public JoinMatchSetupPayloadComponent(String matchName) {
        this.matchName = matchName;
    }
}
