package it.polimi.ingsw.client.gui.nodes;

import it.polimi.ingsw.common.exceptions.IllegalRawConversionException;
import it.polimi.ingsw.common.exceptions.ParserException;
import it.polimi.ingsw.common.parser.JSONParser;
import it.polimi.ingsw.common.parser.raw.RawSpecialAbility;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.controlsfx.glyphfont.Glyph;

import java.util.ArrayList;
import java.util.List;


public class StorageAbilityBox extends HBox {

    private final StringProperty storageAbilityJSON;
    private final ObjectProperty<RawSpecialAbility> storageAbility;

    private List<Label> dashes;
    private List<ResourceBox> resources;
    private Glyph dash;

    public StorageAbilityBox(){
        String json = "{\"type\":\"storage\",\"storage_name\":\"leader_8\",\"accepted_types\":\"gold\",\"amount\":2}";
        try {
            storageAbility = new SimpleObjectProperty<>(this, "storageAbility", JSONParser.parseToRaw(json, RawSpecialAbility.class));
        } catch (ParserException | IllegalRawConversionException e) {
            throw new IllegalArgumentException("Conversion from JSON failed.");
        }
        storageAbilityJSON = new SimpleStringProperty(this, "storageAbilityJSON", json);
        attachElements();
    }

    public StorageAbilityBox(RawSpecialAbility rawSpecialAbility){
        storageAbility = new SimpleObjectProperty<>(this, "storageAbility", rawSpecialAbility);
        storageAbilityJSON = new SimpleStringProperty(this, "storageAbilityJSON", rawSpecialAbility.toString());

        attachElements();
    }

    private void attachElements() {
        Label label;
        ResourceBox resourceBox;

        this.setSpacing(10d);

        dash = new Glyph("FontAwesome", "MINUS");

        dashes = new ArrayList<>();
        resources = new ArrayList<>();

        for(int i = 0; i < storageAbility.get().getAmount(); i++) {
            if(i != 0) {
                label = new Label();
                label.setPrefHeight(40d);
                label.setGraphic(dash);

                this.getChildren().add(label);
                dashes.add(label);
            }

            resourceBox = new ResourceBox(storageAbility.get().getAcceptedTypes(), 0, false, true, false);
            this.getChildren().add(resourceBox);
            resources.add(resourceBox);
        }
    }

    private void update(){
        int i;
        Label label;
        ResourceBox resourceBox;

        for(i = dashes.size(); i < storageAbility.get().getAmount() - 1; i++) {
            label = new Label();
            label.setPrefHeight(40d);
            label.setGraphic(dash);
            dashes.add(label);
        }

        for(i = resources.size(); i < storageAbility.get().getAmount(); i++) {
            resourceBox = new ResourceBox(storageAbility.get().getAcceptedTypes(), 0, false, true, false);
            resources.add(resourceBox);
        }

        for(i = 0; i < resources.size(); i++) {
            resources.get(i).setResource(storageAbility.get().getAcceptedTypes());
        }

        this.getChildren().clear();

        for(i = resources.size(); i < storageAbility.get().getAmount(); i++) {
            if(i != 0)
                this.getChildren().add(dashes.get(i - 1));

            this.getChildren().add(resources.get(i));
        }
    }

    public String getStorageAbilityJSON() {
        return storageAbilityJSON.get();
    }

    public StringProperty storageAbilityJSONProperty() {
        return storageAbilityJSON;
    }

    public RawSpecialAbility getStorageAbility() {
        return storageAbility.get();
    }

    public ObjectProperty<RawSpecialAbility> storageAbilityProperty() {
        return storageAbility;
    }

    public void setStorageAbility(RawSpecialAbility storageAbility) {
        this.storageAbility.set(storageAbility);
        if(!storageAbility.getAcceptedTypes().equals(this.storageAbility.get().getAcceptedTypes()) ||
                storageAbility.getAmount()!=this.storageAbility.get().getAmount())
            update();
    }
}
