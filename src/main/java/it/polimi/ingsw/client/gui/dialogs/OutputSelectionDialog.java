package it.polimi.ingsw.client.gui.dialogs;

import it.polimi.ingsw.client.gui.beans.OutputSelectionBean;
import it.polimi.ingsw.client.gui.nodes.OutputSelectorBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class OutputSelectionDialog extends CustomDialog{
    public OutputSelectionDialog(Stage primaryStage, OutputSelectionBean bean, Consumer<OutputSelectionBean> consumer) {
        super(primaryStage);

        //label
        Label promptLabel = new Label("Select all resources for the output of the selected crafting");
        AnchorPane.setLeftAnchor(promptLabel, 164d);
        AnchorPane.setTopAnchor(promptLabel, 45d);

        //OutputSelector
        OutputSelectorBox outputBox = new OutputSelectorBox(bean.getAmount());
        AnchorPane.setLeftAnchor(outputBox, 97d);
        AnchorPane.setTopAnchor(outputBox, 194d);

        //confirm button
        Button confirmBtn = new Button("Confirm");
        AnchorPane.setRightAnchor(confirmBtn, 50d);
        AnchorPane.setBottomAnchor(confirmBtn, 30d);

        confirmBtn.setOnAction(e ->{
            bean.setSelection(outputBox.getSelectedResources());
            consumer.accept(bean);
            closeDialog();
        });

        //add elements to the root
        getRoot().setPrefSize(650, 354);
        getRoot().getChildren().setAll(promptLabel, outputBox, confirmBtn);
    }
}
