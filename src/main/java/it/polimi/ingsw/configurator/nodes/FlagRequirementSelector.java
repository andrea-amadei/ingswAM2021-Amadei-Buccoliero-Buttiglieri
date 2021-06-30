package it.polimi.ingsw.configurator.nodes;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class FlagRequirementSelector extends HBox implements RequirementSelector {
    private ComboBox<String> color;
    private TextField amount;

    public FlagRequirementSelector() {
        Label label1, label2;

        color = new ComboBox<>();
        color.setPrefWidth(80d);
        color.setItems(FXCollections.observableArrayList("blue", "green", "purple", "yellow"));
        color.getSelectionModel().selectFirst();

        amount = new TextField("1");
        amount.setPrefWidth(80d);
        amount.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("\\d*")) {
                amount.setText(newValue.replaceAll("[^\\d]", ""));
                return;
            }

            if(newValue.equals("") || Integer.parseInt(newValue) == 0)
                amount.setText("1");
        });

        label1 = new Label("Flag:");
        label1.setPrefWidth(50d);
        label1.setPrefHeight(25d);
        label1.setAlignment(Pos.CENTER_LEFT);

        label2 = new Label("Amount:");
        label2.setPrefWidth(50d);
        label2.setPrefHeight(25d);
        label2.setAlignment(Pos.CENTER_LEFT);

        this.setSpacing(20d);
        this.getChildren().addAll(label1, color, label2, amount);
    }


}
