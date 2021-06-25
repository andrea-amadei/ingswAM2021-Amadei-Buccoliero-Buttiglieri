package it.polimi.ingsw.client.gui.dialogs;

import it.polimi.ingsw.client.gui.beans.PreliminaryPickBean;
import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.*;
import java.util.function.Consumer;

public class PreliminaryPickDialog extends CustomDialog{
    private final List<String> resourceNames = new ArrayList<>(Arrays.asList("Gold", "Servant", "Shield", "Stone"));
    private final List<CheckBox> leaderCheckBoxes;
    private final List<ComboBox<Integer>> comboBoxes;
    private final Consumer<PreliminaryPickBean> consumer;
    public PreliminaryPickDialog(Stage primaryStage, Consumer<PreliminaryPickBean> consumer) {
        super(primaryStage);

        leaderCheckBoxes = new ArrayList<>();
        comboBoxes = new ArrayList<>();
        this.consumer = consumer;

        Label promptLabel = new Label("Choose the leaders to discard and the resources to pick");
        promptLabel.setFont(new Font("Arial", 14));

        AnchorPane.setLeftAnchor(promptLabel, 145d);
        AnchorPane.setTopAnchor(promptLabel, 40d);

        //left VBox
        VBox leaderList = new VBox();
        leaderList.setPrefWidth(100);
        leaderList.setPrefHeight(179);
        leaderList.setSpacing(30);
        AnchorPane.setTopAnchor(leaderList, 95d);
        AnchorPane.setLeftAnchor(leaderList, 73d);

        //adding checkBoxes
        for(int i = 0; i < 4; i++){
            CheckBox checkBox = new CheckBox("Leader " + (i+1));
            checkBox.selectedProperty().addListener((o, oldV, newV) -> {
                if(newV){
                    int amount = 0;
                    for(CheckBox c : leaderCheckBoxes){
                        if(c.isSelected())
                            amount++;
                    }

                    if(amount > 2){
                        ((BooleanProperty)o).set(false);
                    }
                }
            });
            leaderCheckBoxes.add(checkBox);
        }

        leaderList.getChildren().addAll(leaderCheckBoxes);

        //right VBox
        VBox resourceList = new VBox();
        resourceList.setPrefWidth(243);
        resourceList.setSpacing(20);
        AnchorPane.setTopAnchor(resourceList, 95d);
        AnchorPane.setLeftAnchor(resourceList, 319d);

        for(String resourceName : resourceNames){
            //add an HBox for each resource
            HBox resourceRow = new HBox();

            //add the components of the HBox
            Label resourceLabel = new Label(resourceName);

            Region region = new Region();
            HBox.setHgrow(region, Priority.ALWAYS);

            ComboBox<Integer> comboBox = new ComboBox<>();
            comboBox.setPrefWidth(150);

            for(int i = 0; i < 4; i++){
                comboBox.getItems().add(i);
            }
            comboBox.getSelectionModel().selectFirst();
            comboBoxes.add(comboBox);

            resourceRow.getChildren().setAll(resourceLabel, region, comboBox);
            resourceList.getChildren().add(resourceRow);
        }

        Button confirmBtn = new Button("Confirm");
        AnchorPane.setTopAnchor(confirmBtn, 289d);
        AnchorPane.setLeftAnchor(confirmBtn, 500d);
        confirmBtn.setOnAction(this::handleConfirm);

        getRoot().getChildren().setAll(promptLabel, leaderList, resourceList, confirmBtn);
        getRoot().setPrefSize(623, 354);
    }


    private void handleConfirm(ActionEvent e){
        if(leaderCheckBoxes.stream().filter(CheckBox::isSelected).count() == 2){
            PreliminaryPickBean bean = new PreliminaryPickBean();
            bean.setLeaderIndexes(selectedLeaders());
            bean.setSelectedResources(selectedResources());
            consumer.accept(bean);
            closeDialog();
        }
    }

    private Map<String, Integer> selectedResources(){
        Map<String, Integer> result = new HashMap<>();

        for(int i = 0; i < comboBoxes.size(); i++){
            String resource = resourceNames.get(i);
            int amount = comboBoxes.get(i).getValue();
            if(amount > 0){
                result.put(resource, amount);
            }
        }

        return result;
    }

    private List<Integer> selectedLeaders(){
        List<Integer> selectedLeadersIndexes = new ArrayList<>();
        for(int i = 0; i < leaderCheckBoxes.size(); i++){
            if(leaderCheckBoxes.get(i).isSelected()){
                selectedLeadersIndexes.add(i);
            }
        }

        return selectedLeadersIndexes;
    }


}
