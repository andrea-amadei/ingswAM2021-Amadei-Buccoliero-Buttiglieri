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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class DiscountAbilityBox extends HBox {

    private final StringProperty discountAbilityJSON;
    private final ObjectProperty<RawSpecialAbility> discountAbility;

    private Label label;
    private ResourceBox resourceBox;

    public DiscountAbilityBox(){
        String json = "{\"type\":\"discount\",\"resource\":\"servant\",\"amount\":1}";
        try {
            discountAbility = new SimpleObjectProperty<>(this, "discountAbility", JSONParser.parseToRaw(json, RawSpecialAbility.class));
        } catch (ParserException | IllegalRawConversionException e) {
            throw new IllegalArgumentException("Conversion from JSON failed.");
        }
        discountAbilityJSON = new SimpleStringProperty(this, "discountAbilityJSON", json);
        attachElements();
    }

    public DiscountAbilityBox(RawSpecialAbility rawSpecialAbility){
        discountAbility = new SimpleObjectProperty<>(this, "discountAbility", rawSpecialAbility);
        discountAbilityJSON = new SimpleStringProperty(this, "discountAbilityJSON", rawSpecialAbility.toString());

        attachElements();
    }

    private void attachElements() {
        label = new Label();
        label.setText("- " + discountAbility.get().getAmount());
        label.setFont(new Font("Times new roman bold", 35));
        label.prefHeight(40d);

        resourceBox = new ResourceBox(discountAbility.get().getResource().toLowerCase(), 0, false, true, false);

        this.getChildren().addAll(label, resourceBox);
    }

    private void update(){
        label.setText("- " + discountAbility.get().getAmount());
        resourceBox.setResource(discountAbility.get().getResource().toLowerCase());
    }


    public String getDiscountAbilityJSON() {
        return discountAbilityJSON.get();
    }

    public StringProperty discountAbilityJSONProperty() {
        return discountAbilityJSON;
    }

    public RawSpecialAbility getDiscountAbility() {
        return discountAbility.get();
    }

    public ObjectProperty<RawSpecialAbility> discountAbilityProperty() {
        return discountAbility;
    }

    public Label getLabel() {
        return label;
    }

    public ResourceBox getResourceBox() {
        return resourceBox;
    }


    public void setDiscountAbility(RawSpecialAbility discountAbility) {
        this.discountAbility.set(discountAbility);
        if(discountAbility.getAmount()!=this.discountAbility.get().getAmount() ||
                !discountAbility.getResource().equals(this.discountAbility.get().getResource()))
                    update();
    }
}
