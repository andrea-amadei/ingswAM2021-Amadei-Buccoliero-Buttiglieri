package it.polimi.ingsw.client.updates;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.common.payload_components.groups.PossibleActions;

import java.util.Set;

public class ChangePossibleActionsUpdate implements Update{

    @SerializedName(value = "possible_actions", alternate = "possibleActions")
    private final Set<PossibleActions> possibleActions;

    public ChangePossibleActionsUpdate(Set<PossibleActions> possibleActions){
        this.possibleActions = possibleActions;
        checkFormat();
    }
    @Override
    public void apply(ClientModel client) {
        //TODO: implement possible action apply
    }

    @Override
    public void checkFormat() {
        if(possibleActions == null)
            throw new NullPointerException("Possible actions not inserted");
    }
}
