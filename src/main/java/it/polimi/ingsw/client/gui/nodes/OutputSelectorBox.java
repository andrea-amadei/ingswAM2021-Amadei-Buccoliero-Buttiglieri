package it.polimi.ingsw.client.gui.nodes;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class OutputSelectorBox extends VBox {
    private final MapProperty<String, Integer> resourceMap;
    private final Set<String> acceptedResources = Set.of("gold", "servant", "shield", "stone");
    private final Map<String, ComboBox<Integer>> comboBoxes;
    private final int maxAmount;
    public OutputSelectorBox(){
        this(3);
    }

    public OutputSelectorBox(int maxAmount){
        this(Map.of("gold", maxAmount, "servant", maxAmount, "shield", maxAmount, "stone", maxAmount),  maxAmount);
    }

    public OutputSelectorBox(Map<String, Integer> resourceMap, int maxAmount){
        //remove non valid entries
        Map<String, Integer> cleanMap = new HashMap<>();
        for(String s : resourceMap.keySet()){
            if(acceptedResources.contains(s))
                cleanMap.put(s, resourceMap.get(s));
        }

        this.resourceMap = new SimpleMapProperty<>(this, "resourceMap", FXCollections.observableMap(cleanMap));
        this.comboBoxes = new HashMap<>();
        this.maxAmount = maxAmount;

        this.setPrefWidth(243);
        this.setSpacing(20);

        for(String resourceName : resourceMap.keySet()){
            //add an HBox for each resource
            HBox resourceRow = new HBox();

            //add the components of the HBox
            Label resourceLabel = new Label(resourceName);

            Region region = new Region();
            HBox.setHgrow(region, Priority.ALWAYS);

            ComboBox<Integer> comboBox = new ComboBox<>();
            comboBox.setPrefWidth(150);

            for(int i = 0; i <= resourceMap.get(resourceName); i++){
                comboBox.getItems().add(i);
            }
            comboBox.getSelectionModel().selectFirst();
            comboBox.valueProperty().addListener((o, oldV, newV) -> {
                if(oldV != null && newV != null && !oldV.equals(newV) ) {
                    int alreadySelected = 0;
                    for (String resource : comboBoxes.keySet()) {
                        alreadySelected += comboBoxes.get(resource).getValue();
                    }
                    int remainder = maxAmount - alreadySelected;
                    for (String resource : comboBoxes.keySet()) {
                        ComboBox<Integer> c = comboBoxes.get(resource);
                        int currentValue = c.getValue();
                        c.getItems().clear();
                        int i;
                        for (i = 0; i <= currentValue; i++) {
                            c.getItems().add(i);
                        }
                        for(int j = 0; j < remainder; j++){
                            c.getItems().add(i+j);
                        }
                        c.getSelectionModel().select(currentValue);
                    }
                }
            });
            comboBoxes.put(resourceName, comboBox);

            resourceRow.getChildren().setAll(resourceLabel, region, comboBox);
            this.getChildren().add(resourceRow);
        }
    }

    public Map<String, ComboBox<Integer>> getComboBoxes() {
        return comboBoxes;
    }

    public Map<String,Integer> getSelectedResources(){
        return comboBoxes.entrySet().stream().filter(e -> e.getValue().getValue() > 0).collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getValue()));
    }
}
