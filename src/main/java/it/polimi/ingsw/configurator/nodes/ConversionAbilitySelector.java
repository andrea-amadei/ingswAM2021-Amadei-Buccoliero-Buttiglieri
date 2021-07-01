package it.polimi.ingsw.configurator.nodes;

import it.polimi.ingsw.common.parser.raw.RawSpecialAbility;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConversionAbilitySelector extends VBox implements AbilitySelector {

    private ComboBox<String> from;
    private Map<String, CheckBox> checkBoxes;
    private TextField faithOutput;

    public ConversionAbilitySelector() {
        this.setSpacing(10d);
        this.setPrefWidth(250d);
        this.setMaxWidth(Double.NEGATIVE_INFINITY);

        from = new ComboBox<>();
        from.setPrefWidth(80d);
        from.setItems(FXCollections.observableArrayList("blue", "grey", "purple", "red", "white", "yellow"));
        from.getSelectionModel().selectFirst();

        HBox hBox = new HBox();
        hBox.setSpacing(20d);
        hBox.setAlignment(Pos.CENTER);
        Label label = new Label("From:");
        label.setPrefWidth(50d);
        hBox.getChildren().addAll(label, from);
        this.getChildren().add(hBox);

        VBox vBox = new VBox();
        vBox.setSpacing(10d);

        checkBoxes = new HashMap<>();
        checkBoxes.put("gold", new CheckBox("gold"));
        checkBoxes.put("servant", new CheckBox("servant"));
        checkBoxes.put("shield", new CheckBox("shield"));
        checkBoxes.put("stone", new CheckBox("stone"));

        checkBoxes.get("gold").setSelected(true);

        for(CheckBox checkBox : checkBoxes.values()) {
            checkBox.setOnAction((e) -> {
                int tot = 0;

                for(CheckBox cb : checkBoxes.values())
                    if(cb.isSelected())
                        tot++;

                if(tot == 0 && Integer.parseInt(faithOutput.getText()) == 0)
                    checkBoxes.get("gold").setSelected(true);
            });

            checkBox.setPrefWidth(80d);

            vBox.getChildren().add(checkBox);
        }

        hBox = new HBox();
        hBox.setSpacing(20d);
        hBox.setAlignment(Pos.CENTER);
        label = new Label("To:");
        label.setPrefWidth(50d);
        hBox.getChildren().addAll(label, vBox);
        this.getChildren().add(hBox);

        faithOutput = new TextField("0");
        faithOutput.setPrefWidth(80d);
        faithOutput.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("\\d*")) {
                faithOutput.setText(newValue.replaceAll("[^\\d]", ""));
                return;
            }

            if(newValue.equals("") || Integer.parseInt(newValue) == 0) {
                int tot = 0;

                for(CheckBox checkBox : checkBoxes.values())
                    if(checkBox.isSelected())
                        tot++;

                if(tot == 0)
                    checkBoxes.get("gold").setSelected(true);
            }
        });

        hBox = new HBox();
        hBox.setSpacing(20d);
        hBox.setAlignment(Pos.CENTER);
        label = new Label("Faith:");
        label.setPrefWidth(50d);
        hBox.getChildren().addAll(label, faithOutput);
        this.getChildren().add(hBox);
    }

    public RawSpecialAbility getRawSpecialAbility() {
        List<String> selected = new ArrayList<>();

        for(CheckBox checkBox : checkBoxes.values())
            if(checkBox.isSelected())
                selected.add(checkBox.getText());

        int faith;

        if(faithOutput.getText().equals(""))
            faith = 0;
        else
            faith = Integer.parseInt(faithOutput.getText());

        return new RawSpecialAbility(
                from.getSelectionModel().getSelectedItem(),
                selected,
                faith
        );
    }

    public void setRawSpecialAbility(RawSpecialAbility rawSpecialAbility) {
        if(!rawSpecialAbility.getType().equals("conversion"))
            return;

        from.getSelectionModel().select(rawSpecialAbility.getFrom().name().toLowerCase());
        faithOutput.setText(String.valueOf(rawSpecialAbility.getFaithOutput()));

        for(String res : rawSpecialAbility.getTo())
            checkBoxes.get(res).setSelected(true);

        for(String res : checkBoxes.keySet())
            if(!rawSpecialAbility.getTo().contains(res))
                checkBoxes.get(res).setSelected(false);
    }
}
