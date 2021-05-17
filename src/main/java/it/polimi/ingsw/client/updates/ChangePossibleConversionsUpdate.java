package it.polimi.ingsw.client.updates;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.model.market.ConversionActuator;
import it.polimi.ingsw.model.market.Marble;

import java.util.List;

public class ChangePossibleConversionsUpdate implements Update{

    @SerializedName(value = "selected_marbles", alternate = "selectedMarbles")
    private final List<Marble> selectedMarbles;
    @SerializedName(value = "possible_conversions", alternate = "possibleConversions")
    private final List<List<ConversionActuator>> possibleConversions;
    private final String player;

    public ChangePossibleConversionsUpdate(List<Marble> selectedMarbles, List<List<ConversionActuator>> possibleConversions,
                                           String player){
        this.selectedMarbles = selectedMarbles;
        this.possibleConversions = possibleConversions;
        this.player = player;
        checkFormat();
    }

    @Override
    public void apply(ClientModel client) {

    }

    @Override
    public void checkFormat() {
        if(selectedMarbles == null)
            throw new NullPointerException("Pointer to selectedMarbles is null");
        if(possibleConversions == null)
            throw new NullPointerException("Pointer to possibleConversions is null");
        if(player == null)
            throw new NullPointerException("Pointer to player is null");
    }

    public List<Marble> getSelectedMarbles() {
        return selectedMarbles;
    }

    public List<List<ConversionActuator>> getPossibleConversions() {
        return possibleConversions;
    }

    public String getPlayer() {
        return player;
    }
}
