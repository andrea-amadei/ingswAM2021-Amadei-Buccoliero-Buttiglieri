package it.polimi.ingsw.common.payload_components.groups.updates;

import it.polimi.ingsw.annotations.SerializedType;
import it.polimi.ingsw.common.payload_components.groups.SpecificUpdatePayloadComponent;
import it.polimi.ingsw.model.market.ConversionActuator;

import java.util.List;

@SerializedType("change_possible_conversions")
public class ChangePossibleConversionPayloadComponent extends SpecificUpdatePayloadComponent {
    private final List<List<ConversionActuator>> possibleConversions;

    /**
     * This payload informs the client of the possible conversions of the selected marbles of
     * the market
     * @param possibleConversions the possible conversions of the selected marbles of the market
     * @throws NullPointerException if possibleConversions is null
     */
    public ChangePossibleConversionPayloadComponent(String player, List<List<ConversionActuator>> possibleConversions){
        super(player);
        if(possibleConversions == null)
            throw new NullPointerException();
        this.possibleConversions = possibleConversions;
    }

    /**
     * Returns the possible conversions
     * @return the possible conversions
     */
    public List<List<ConversionActuator>> getPossibleConversions() {
        return possibleConversions;
    }
}
