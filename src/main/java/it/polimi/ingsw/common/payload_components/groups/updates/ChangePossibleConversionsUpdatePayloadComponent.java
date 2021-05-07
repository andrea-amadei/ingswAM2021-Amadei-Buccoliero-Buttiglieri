package it.polimi.ingsw.common.payload_components.groups.updates;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.annotations.SerializedType;
import it.polimi.ingsw.common.payload_components.groups.SpecificUpdatePayloadComponent;
import it.polimi.ingsw.gamematerials.MarbleColor;
import it.polimi.ingsw.model.market.ConversionActuator;
import it.polimi.ingsw.model.market.Marble;

import java.util.List;

@SerializedType("change_possible_conversions")
public class ChangePossibleConversionsUpdatePayloadComponent extends SpecificUpdatePayloadComponent {

    @SerializedName(value = "selected_marbles", alternate = "selectedMarbles")
    private List<Marble> selectedMarbles;

    @SerializedName(value = "possible_conversions", alternate = "possibleConversions")
    private List<List<ConversionActuator>> possibleConversions;

    /**
     * This constructor shall only be used to instance payloads through reflection
     */
    public ChangePossibleConversionsUpdatePayloadComponent() { }

    public ChangePossibleConversionsUpdatePayloadComponent(String player, List<Marble> selectedMarbles, List<List<ConversionActuator>> possibleConversions) {
        super(player);

        if(selectedMarbles == null || possibleConversions == null)
            throw new NullPointerException();

        this.selectedMarbles = selectedMarbles;
        this.possibleConversions = possibleConversions;
    }

    public List<Marble> getSelectedMarbles() {
        return selectedMarbles;
    }

    public List<List<ConversionActuator>> getPossibleConversions() {
        return possibleConversions;
    }
}
