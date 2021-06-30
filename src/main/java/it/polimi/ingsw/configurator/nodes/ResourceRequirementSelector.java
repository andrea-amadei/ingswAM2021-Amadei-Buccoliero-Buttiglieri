package it.polimi.ingsw.configurator.nodes;

import it.polimi.ingsw.common.parser.raw.RawRequirement;
import it.polimi.ingsw.server.model.basetypes.FlagColor;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ResourceRequirementSelector extends VBox implements RequirementSelector{
    private ComboBox<String> resourceComboBox;
    private TextField amount;

    public ResourceRequirementSelector(){
        Label label1, label2;

        resourceComboBox = new ComboBox<>();
        resourceComboBox.setPrefWidth(70d);
        resourceComboBox.setItems(FXCollections.observableArrayList("gold", "servant", "shield", "stone"));
        resourceComboBox.getSelectionModel().selectFirst();

        amount = new TextField("1");
        amount.setPrefWidth(30d);
        amount.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("\\d*")) {
                amount.setText(newValue.replaceAll("[^\\d]", ""));
                return;
            }

            if(newValue.equals("") || Integer.parseInt(newValue) == 0)
                amount.setText("1");
        });

        label1 = new Label("Resource:");
        label1.setPrefHeight(25d);
        label1.setAlignment(Pos.CENTER_LEFT);

        label2 = new Label("Amount:");
        label2.setPrefHeight(25d);
        label2.setAlignment(Pos.CENTER_LEFT);

        this.setSpacing(5d);
        this.setPrefHeight(175d);
        this.getChildren().addAll(label1, resourceComboBox, label2, amount);
    }

    public RawRequirement getRawRequirement() {
        return new RawRequirement(
                resourceComboBox.getValue(),
                Integer.parseInt(amount.getText()
                ));
    }

    public void setRawRequirement(RawRequirement rawRequirement) {
        if(!rawRequirement.getType().equals("resource"))
            return;

        resourceComboBox.getSelectionModel().select(rawRequirement.getResource());
        amount.setText(String.valueOf(rawRequirement.getAmount()));
    }
}
