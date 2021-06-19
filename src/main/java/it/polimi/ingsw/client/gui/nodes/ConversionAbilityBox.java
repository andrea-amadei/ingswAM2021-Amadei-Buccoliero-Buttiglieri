package it.polimi.ingsw.client.gui.nodes;

import it.polimi.ingsw.exceptions.IllegalRawConversionException;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.parser.JSONParser;
import it.polimi.ingsw.parser.raw.RawSpecialAbility;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
        marble = new MarbleBox(conversionAbility.get().getFrom().toString());

        Label label = new Label();
        label.setAlignment(Pos.CENTER);
        label.setText("=");
        label.setPrefHeight(39d);
        label.setPrefWidth(35d);
        label.setFont(new Font("Arial", 36d));

        resources = new ArrayList<>();
        for(String s : conversionAbility.get().getTo()){
            resources.add(new ResourceBox(s.toLowerCase(), 1, false, false, false));
        }

        faith = new ResourceBox("faith", conversionAbility.get().getFaithOutput(), true, false, true);

        this.getChildren().addAll(marble, label);
        this.getChildren().addAll(resources);
        this.getChildren().addAll(faith);
    }

    private void update(){
        marble.setColor(conversionAbility.get().getFrom().toString());
        resources = new ArrayList<>();
        for(String s : conversionAbility.get().getTo()){
            resources.add(new ResourceBox(s.toLowerCase(), 1, false, false, false));
        }
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
