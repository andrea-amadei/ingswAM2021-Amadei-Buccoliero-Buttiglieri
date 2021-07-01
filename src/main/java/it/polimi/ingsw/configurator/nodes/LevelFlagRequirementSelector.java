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

public class LevelFlagRequirementSelector extends VBox implements RequirementSelector {
    private ComboBox<String> color;
    private ComboBox<Integer> level;
    private TextField amount;

    public LevelFlagRequirementSelector() {
        Label label1, label2, label3;

        color = new ComboBox<>();
        color.setPrefWidth(100d);
        color.setItems(FXCollections.observableArrayList("blue", "green", "purple", "yellow"));
        color.getSelectionModel().selectFirst();

        level = new ComboBox<>();
        level.setPrefWidth(100d);
        level.setItems(FXCollections.observableArrayList(1, 2, 3));
        level.getSelectionModel().selectFirst();

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

        label1 = new Label("Flag:");
        label1.setPrefHeight(25d);
        label1.setAlignment(Pos.CENTER_LEFT);

        label2 = new Label("Level:");
        label2.setPrefHeight(25d);
        label2.setAlignment(Pos.CENTER_LEFT);

        label3 = new Label("Amount:");
        label3.setPrefHeight(25d);
        label3.setAlignment(Pos.CENTER_LEFT);

        this.setSpacing(5d);
        this.getChildren().addAll(label1, color, label2, level, label3, amount);
    }

    public RawRequirement getRawRequirement() {
        return new RawRequirement(
                FlagColor.valueOf(color.getSelectionModel().getSelectedItem().toUpperCase()),
                level.getSelectionModel().getSelectedItem(),
                Integer.parseInt(amount.getText())
        );
    }

    public void setRawRequirement(RawRequirement rawRequirement) {
        if(!rawRequirement.getType().equals("level_flag"))
            return;

        color.getSelectionModel().select(rawRequirement.getFlag().name().toLowerCase());
        level.getSelectionModel().select(rawRequirement.getLevel() - 1);
        amount.setText(String.valueOf(rawRequirement.getAmount()));
    }
}
