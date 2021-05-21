package it.polimi.ingsw.common.payload_components.groups;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.annotations.SerializedGroup;
import it.polimi.ingsw.annotations.SerializedType;
import it.polimi.ingsw.common.payload_components.PayloadComponent;

import java.util.Set;

@SerializedGroup("possible_actions")
@SerializedType("possible_actions")
public class PossibleActionsPayloadComponent implements PayloadComponent {

    @SerializedName(value = "possible_actions", alternate = "possibleActions")
    private final Set<PossibleActions> possibleActions;

    /**
     * Creates a payload containing all the actions that a player can execute
     * @param possibleActions the actions that a player can execute
     * @throws NullPointerException if possibleActions is null
     */
    public PossibleActionsPayloadComponent(Set<PossibleActions> possibleActions){
        if(possibleActions == null)
            throw new NullPointerException();

        this.possibleActions = possibleActions;
    }

    /**
     * Returns all possible actions
     * @return all possible actions
     */
    public Set<PossibleActions> getPossibleActions(){
        return possibleActions;
    }
}

