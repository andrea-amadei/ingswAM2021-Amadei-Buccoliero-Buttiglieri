package it.polimi.ingsw.configurator.nodes;

import it.polimi.ingsw.common.parser.raw.RawCrafting;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Map;

public class CraftingSelector extends VBox implements AbilitySelector {
    private final ResourceSelector input;
    private final ResourceSelector output;
    private final Label faithLabel;
    private final TextField faithField;

    public CraftingSelector() {
        this.setPrefWidth(250d);
        this.setMaxWidth(Double.NEGATIVE_INFINITY);

        HBox hBox;

        input = new ResourceSelector("Input:", true);
        output = new ResourceSelector("Output:", true);

        hBox = new HBox();
        hBox.setSpacing(30d);
        hBox.getChildren().addAll(input, output);
        this.getChildren().add(hBox);

        faithLabel = new Label("Faith Output:");
        faithLabel.setAlignment(Pos.CENTER_RIGHT);

        faithField = new TextField("0");
        faithField.setPrefWidth(50d);
        faithField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("\\d*"))
                faithField.setText(newValue.replaceAll("[^\\d]", ""));
        });

        hBox = new HBox();
        hBox.setSpacing(10d);
        hBox.setPadding(new Insets(20d, 0d, 0d, 0d));
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.getChildren().addAll(faithLabel, faithField);
        this.getChildren().add(hBox);
    }

    public Map<String, Integer> getInput() {
        return input.getResourceMap();
    }

    public Map<String, Integer> getOutput() {
        return output.getResourceMap();
    }

    public void setInputMap(Map<String, Integer> resources) {
        input.setResourceMap(resources);
    }

    public void setOutputMap(Map<String, Integer> resources) {
        output.setResourceMap(resources);
    }

    public int getFaithOutput() {
        if(faithField.getText().equals(""))
            return 0;
        else
            return Integer.parseInt(faithField.getText());
    }

    public void setFaithOutput(int faithOutput) {
        faithField.setText(String.valueOf(faithOutput));
    }

    public RawCrafting getRawCrafting() {
        return new RawCrafting(getInput(), getOutput(), getFaithOutput());
    }
}
