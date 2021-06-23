package it.polimi.ingsw.client.gui.dialogs;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public abstract class CustomDialog extends Stage {

    private final AnchorPane rootAnchorPane;
    private final Stage primaryStage;

    public CustomDialog(Stage primaryStage) {
        this.rootAnchorPane = new AnchorPane();
        this.rootAnchorPane.setStyle("-fx-background-color: #1aadc6; -fx-border-width: 3; -fx-border-insets: 10; -fx-border-color: blue");
        this.primaryStage = primaryStage;

        this.initStyle(StageStyle.TRANSPARENT);
        this.initOwner(primaryStage);
        this.initModality(Modality.APPLICATION_MODAL);
        this.setScene(new Scene(rootAnchorPane));
    }

    public void openDialog() {
        show();
    }

    void closeDialog() {
        close();
    }

    protected Stage getPrimaryStage(){
        return primaryStage;
    }

    protected AnchorPane getRoot(){
        return rootAnchorPane;
    }
}
