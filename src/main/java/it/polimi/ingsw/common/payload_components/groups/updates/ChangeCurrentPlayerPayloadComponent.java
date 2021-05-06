package it.polimi.ingsw.common.payload_components.groups.updates;

import it.polimi.ingsw.annotations.SerializedType;
import it.polimi.ingsw.common.payload_components.groups.UpdatePayloadComponent;

@SerializedType("change_current_player")
public class ChangeCurrentPlayerPayloadComponent implements UpdatePayloadComponent {

    private final String newPlayer;


    /**
     * Informs the clients that the current player is changed
     * @param newPlayer the new current player
     */
    public ChangeCurrentPlayerPayloadComponent(String newPlayer){
        if(newPlayer == null)
            throw new NullPointerException();

        this.newPlayer = newPlayer;
    }

    /**
     * Return the new player
     * @return the new player
     */
    public String getNewPlayer() {
        return newPlayer;
    }

}
