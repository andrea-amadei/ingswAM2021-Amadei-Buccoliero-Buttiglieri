package it.polimi.ingsw.client.gui.nodes;

import it.polimi.ingsw.common.exceptions.IllegalRawConversionException;
import it.polimi.ingsw.common.exceptions.ParserException;
import it.polimi.ingsw.common.parser.JSONParser;
import it.polimi.ingsw.common.parser.raw.RawSpecialAbility;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public class ConversionAbilityBox extends HBox {

    private final StringProperty conversionAbilityJSON;
    private final SimpleObjectProperty<RawSpecialAbility> conversionAbility;

    private MarbleBox marble;
    private List<ResourceBox> resources;
    private ResourceBox faith;

    public ConversionAbilityBox(){
        String json = "{\"type\":\"conversion\",\"from\":\"WHITE\",\"to\":[\"gold\"],\"faith_output\":0}";
        try {
            conversionAbility = new SimpleObjectProperty<>(this, "conversionAbility", JSONParser.parseToRaw(json, RawSpecialAbility.class));
        } catch (ParserException | IllegalRawConversionException e) {
            throw new IllegalArgumentException("Conversion from JSON failed.");
        }
        conversionAbilityJSON = new SimpleStringProperty(this, "conversionAbilityJSON", json);
        attachElements();
    }

    public ConversionAbilityBox(RawSpecialAbility rawSpecialAbility){
        conversionAbility = new SimpleObjectProperty<>(this, "conversionAbility", rawSpecialAbility);
        conversionAbilityJSON = new SimpleStringProperty(this, "conversionAbilityJSON", rawSpecialAbility.toString());

        attachElements();
    }

    private void attachElements(){
        this.setPrefHeight(40d);

        marble = new MarbleBox(conversionAbility.get().getFrom().toString());

        Label label = new Label();
        label.setAlignment(Pos.CENTER);
        label.setText("=");
        label.setPrefHeight(40d);
        label.setPrefWidth(40d);
        label.setFont(new Font("Times new roman bold", 30d));

        resources = new ArrayList<>();
        for(String s : conversionAbility.get().getTo()) {
            ResourceBox resourceBox = new ResourceBox(s.toLowerCase(), 0, false, true, false);
            resourceBox.setPadding(new Insets(-10,0, 0, 0));
            resources.add(resourceBox);
        }

        faith = new ResourceBox("faith", conversionAbility.get().getFaithOutput(), true, false, true);

        if(conversionAbility.get().getFaithOutput() > 0)
            this.getChildren().add(faith);

        this.getChildren().addAll(marble, label);
        this.getChildren().addAll(resources);
    }

    private void update(){
        marble.setColor(conversionAbility.get().getFrom().toString());
        resources = new ArrayList<>();
        for(String s : conversionAbility.get().getTo()){
            resources.add(new ResourceBox(s.toLowerCase(), 0, false, true, false));
        }

        if(conversionAbility.get().getFaithOutput() > 0 && !this.getChildren().contains(faith))
            this.getChildren().add(faith);
        else if(conversionAbility.get().getFaithOutput() == 0)
            this.getChildren().remove(faith);

        faith.setAmount(conversionAbility.get().getFaithOutput());
    }

    public String getConversionAbilityJSON() {
        return conversionAbilityJSON.get();
    }

    public StringProperty conversionAbilityJSONProperty() {
        return conversionAbilityJSON;
    }

    public RawSpecialAbility getConversionAbility() {
        return conversionAbility.get();
    }

    public SimpleObjectProperty<RawSpecialAbility> conversionAbilityProperty() {
        return conversionAbility;
    }

    public MarbleBox getMarble() {
        return marble;
    }

    public List<ResourceBox> getResources() {
        return resources;
    }

    public ResourceBox getFaith() {
        return faith;
    }

    public void setConversionAbility(RawSpecialAbility conversionAbility) {
        this.conversionAbility.set(conversionAbility);

        if(!conversionAbility.getFrom().equals(this.conversionAbility.get().getFrom()) ||
            !conversionAbility.getTo().equals(this.conversionAbility.get().getTo()) ||
                    conversionAbility.getFaithOutput() != this.conversionAbility.get().getFaithOutput())
            update();
    }
}
