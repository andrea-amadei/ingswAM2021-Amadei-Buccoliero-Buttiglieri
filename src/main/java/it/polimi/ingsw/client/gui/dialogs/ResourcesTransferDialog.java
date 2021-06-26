package it.polimi.ingsw.client.gui.dialogs;

import it.polimi.ingsw.client.gui.beans.ResourceTransferBean;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ResourcesTransferDialog extends CustomDialog{
    ComboBox<String> destinationComboBox;
    ComboBox<String> resourceCombobox;
    ComboBox<Integer> amountComboBox;
    private final Map<String, String> leaderDictionary;

    public ResourcesTransferDialog(Stage primaryStage, List<String> destinations, Map<String, Integer> sourceStorage,
                                   ResourceTransferBean bean, Consumer<ResourceTransferBean> consumer) {
        super(primaryStage);

        leaderDictionary = new HashMap<>();

        //setting up the root anchor pane
        getRoot().setPrefSize(650, 354);

        //destination label
        Label destinationLabel = new Label("Destination storage");
        AnchorPane.setLeftAnchor(destinationLabel, 45d);
        AnchorPane.setTopAnchor(destinationLabel, 120d);

        //destination combobox
        destinationComboBox  = new ComboBox<>();
        for(String s : destinations){
            int dollarIndex = s.indexOf("$");
            String fullString = s;
            if(dollarIndex >= 0) {
                s = s.substring(dollarIndex + 1);
                String realId = fullString.substring(0, dollarIndex);
                leaderDictionary.put(s, realId);
            }
            destinationComboBox.getItems().add(s);
        }
        destinationComboBox.getSelectionModel().selectFirst();
        AnchorPane.setLeftAnchor(destinationComboBox, 45d);
        AnchorPane.setTopAnchor(destinationComboBox, 173d);
        destinationComboBox.setPrefWidth(121);

        //amount label
        Label amountLabel = new Label("Amount");
        AnchorPane.setLeftAnchor(amountLabel, 451d);
        AnchorPane.setTopAnchor(amountLabel, 120d);

        //amount combobox
        amountComboBox = new ComboBox<>();
        AnchorPane.setLeftAnchor(amountComboBox, 451d);
        AnchorPane.setTopAnchor(amountComboBox, 173d);
        amountComboBox.setDisable(true);
        amountComboBox.setPrefWidth(121);

        //resource label
        Label resourceLabel = new Label("Resource to transfer");
        AnchorPane.setLeftAnchor(resourceLabel, 248d);
        AnchorPane.setTopAnchor(resourceLabel, 120d);

        //resource combobox
        resourceCombobox = new ComboBox<>();
        AnchorPane.setLeftAnchor(resourceCombobox, 248d);
        AnchorPane.setTopAnchor(resourceCombobox, 173d);
        resourceCombobox.setPrefWidth(121);

        resourceCombobox.getItems().addAll(sourceStorage.keySet());
        resourceCombobox.valueProperty().addListener((o, oldV, newV) ->{
            if(newV != null) {
                amountComboBox.setDisable(false);
                int maxResources = sourceStorage.get(newV);
                amountComboBox.getItems().clear();
                for(int i = 1; i <= maxResources; i++){
                    amountComboBox.getItems().add(i);
                }
                amountComboBox.getSelectionModel().selectFirst();
            }
        });
        resourceCombobox.getSelectionModel().selectFirst();

        //confirmButton
        Button confirmButton = new Button("Confirm");
        AnchorPane.setRightAnchor(confirmButton, 50d);
        AnchorPane.setBottomAnchor(confirmButton, 30d);

        confirmButton.setOnAction(evt ->{
            if(destinationComboBox.getValue() != null && resourceCombobox.getValue() != null && amountComboBox.getValue() != null) {
                String currentRealDestination = destinationComboBox.getValue();
                if(leaderDictionary.containsKey(currentRealDestination))
                    currentRealDestination = leaderDictionary.get(currentRealDestination);
                bean.setTargetDestination(currentRealDestination);
                bean.setChoseResource(resourceCombobox.getValue());
                bean.setAmountToTransfer(amountComboBox.getValue());
                consumer.accept(bean);
                closeDialog();
            }
        });

        getRoot().getChildren().addAll(destinationLabel, destinationComboBox, amountLabel, amountComboBox, resourceLabel, resourceCombobox, confirmButton);
    }
}
