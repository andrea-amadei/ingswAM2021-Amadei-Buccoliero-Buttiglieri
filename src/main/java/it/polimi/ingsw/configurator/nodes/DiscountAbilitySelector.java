package it.polimi.ingsw.configurator.nodes;

import it.polimi.ingsw.common.parser.raw.RawSpecialAbility;
import it.polimi.ingsw.server.Logger;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DiscountAbilitySelector extends VBox implements AbilitySelector{
    private final ComboBox<String> resourceComboBox;
    private final TextField discountTextBox;

    private final Set<String> possibleResourceTypes = Set.of("gold", "servant", "shield", "stone");

    public DiscountAbilitySelector(){
        resourceComboBox = new ComboBox<>();
        resourceComboBox.getItems().addAll("gold", "servant", "shield", "stone");
        resourceComboBox.getSelectionModel().selectFirst();
        resourceComboBox.setPrefWidth(100);

        discountTextBox = new TextField("1");
        discountTextBox.setPrefWidth(100);
        discountTextBox.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("\\d*")) {
                discountTextBox.setText(newValue.replaceAll("[^\\d]", ""));
                return;
            }

            if(newValue.equals("") || Integer.parseInt(newValue) == 0) {
                discountTextBox.setText("1");
            }
        });

        HBox hBox0 = new HBox();
        Label label0 = new Label("Resource Type");
        label0.setPrefWidth(120d);
        hBox0.getChildren().addAll(label0, resourceComboBox);
        hBox0.setAlignment(Pos.CENTER);

        HBox hBox = new HBox();
        Label label = new Label("Discount amount");
        label.setPrefWidth(120d);
        hBox.getChildren().addAll(label, discountTextBox);
        hBox.setAlignment(Pos.CENTER);

        this.setSpacing(20);
        this.setPrefWidth(250);
        this.setMaxWidth(Double.NEGATIVE_INFINITY);

        this.getChildren().setAll(hBox0, hBox);

    }

    public void setResourceType(String resource){
        if(possibleResourceTypes.contains(resource)){
            resourceComboBox.getSelectionModel().select(resource);
        }else{
            Logger.log("Wrong resource set in the StorageAbilitySelector");
        }
    }

    public void setAmount(int amount){
        discountTextBox.setText(String.valueOf(amount));
    }

    public String getResourceType(){
        return resourceComboBox.getValue().toLowerCase();
    }

    public int getDiscountAmount(){
        return Integer.parseInt(discountTextBox.getText());
    }

    public RawSpecialAbility getRawSpecialAbility() {
        return new RawSpecialAbility(getResourceType(), getDiscountAmount());
    }

    public void setRawSpecialAbility(RawSpecialAbility rawSpecialAbility) {
        if(!rawSpecialAbility.getType().equals("discount")) {
            Logger.log("tried to set an ability of type " + rawSpecialAbility.getType() + " to a DiscountAbilitySelector");
            return;
        }
        setAmount(rawSpecialAbility.getAmount());
        setResourceType(rawSpecialAbility.getResource());
    }
}
