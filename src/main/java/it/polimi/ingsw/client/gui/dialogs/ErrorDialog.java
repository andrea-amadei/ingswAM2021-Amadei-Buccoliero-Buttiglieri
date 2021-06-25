package it.polimi.ingsw.client.gui.dialogs;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ErrorDialog extends CustomDialog{
    private final String errorMessage;
    public ErrorDialog(Stage primaryStage, String errorMessage) {
        super(primaryStage);
        this.errorMessage = errorMessage;

        Label promptLabel = new Label(errorMessage);
        promptLabel.setMaxWidth(334);
        promptLabel.setFont(new Font("Arial", 14));
        promptLabel.setWrapText(true);

        AnchorPane.setLeftAnchor(promptLabel, 145d);
        AnchorPane.setTopAnchor(promptLabel, 40d);

        Button confirm = new Button("Confirm");
        AnchorPane.setLeftAnchor(confirm, 500d);
        AnchorPane.setTopAnchor(confirm, 155d);

        confirm.setOnAction(e -> closeDialog());

        getRoot().setPrefSize(600, 200);
        getRoot().setMaxWidth(600);
        getRoot().getChildren().setAll(promptLabel, confirm);
    }
}
