package it.polimi.ingsw.client.gui.nodes;

import it.polimi.ingsw.exceptions.IllegalRawConversionException;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.parser.JSONParser;
import it.polimi.ingsw.parser.raw.RawSpecialAbility;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;


public class StorageAbilityBox extends VBox {

    private final StringProperty storageAbilityJSON;
    private final ObjectProperty<RawSpecialAbility> storageAbility;

    private Label label;
    private ResourceBox resource;

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

    private void attachElements(){
        label = new Label();
        label.setText("Storage ability");
        label.setFont(new Font("Arial", 18));
        label.setAlignment(Pos.CENTER);

        resource = new ResourceBox(storageAbility.get().getAcceptedTypes(), storageAbility.get().getAmount(), true, true, true);

        this.getChildren().addAll(label, resource);
    }

    private void update(){
        resource.setResource(storageAbility.get().getAcceptedTypes());
        resource.setAmount(storageAbility.get().getAmount());
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

    public Label getLabel() {
        return label;
    }

    public ResourceBox getResource() {
        return resource;
    }

    public void setStorageAbility(RawSpecialAbility storageAbility) {
        this.storageAbility.set(storageAbility);
        if(!storageAbility.getAcceptedTypes().equals(this.storageAbility.get().getAcceptedTypes()) ||
                storageAbility.getAmount()!=this.storageAbility.get().getAmount())
            update();
    }
}
