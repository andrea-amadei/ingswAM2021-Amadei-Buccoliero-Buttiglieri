package it.polimi.ingsw.client.updates;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.client.clientmodel.ClientModel;
import it.polimi.ingsw.client.clientmodel.ConversionOption;
import it.polimi.ingsw.server.model.basetypes.MarbleColor;
import it.polimi.ingsw.server.model.basetypes.ResourceSingle;
import it.polimi.ingsw.server.model.market.ConversionActuator;
import it.polimi.ingsw.server.model.market.Marble;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        List<MarbleColor> selectedMarbleColors = selectedMarbles.stream().map(Marble::getColor).collect(Collectors.toList());


        // GOODBYE OLD MOUNTAIN, YOU WILL BE MISSED

        // List<List<ConversionOption>> conversionOptions =
        //        possibleConversions.stream()
        //                           .map(actuatorList -> actuatorList.stream()
        //                                                            .map(actuator ->
        //                                                                    new ConversionOption(
        //                                                                            actuator.getResources().stream()
        //                                                                                                   .map(ResourceType::getId)
        //                                                                                                   .collect(Collectors.toList()),
        //                                                                            actuator.getFaith())
        //                                                            )
        //                                                            .collect(Collectors.toList())
        //                           ).collect(Collectors.toList());


        //Converting the List<List<ConversionActuator>> in a List<List<ConversionOption>>
        List<List<ConversionOption>> conversionOptions = new ArrayList<>();

        for(List<ConversionActuator> actuatorList : possibleConversions){
            List<ConversionOption> conversionOptionList = new ArrayList<>();
            for(ConversionActuator actuator : actuatorList){
                List<String> strResource = actuator.getResources().stream().map(ResourceSingle::getId).collect(Collectors.toList());
                int faith = actuator.getFaith();

                conversionOptionList.add(new ConversionOption(strResource, faith));
            }

            conversionOptions.add(conversionOptionList);
        }

        client.getMarket().changePossibleConversions(selectedMarbleColors, conversionOptions);
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
