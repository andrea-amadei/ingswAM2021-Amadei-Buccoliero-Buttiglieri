package it.polimi.ingsw.client.gui.dialogs;

import it.polimi.ingsw.client.gui.beans.ShopSelectionBean;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class ChooseCraftingDialog extends CustomDialog{

    public ChooseCraftingDialog(Stage primaryStage, ShopSelectionBean bean, Consumer<ShopSelectionBean> confirmHandler){
        super(primaryStage);
        Label label = new Label("Choose slot of the production for the selected card");
        label.setFont(new Font("Arial", 14));
        AnchorPane.setTopAnchor(label, 40d);
        AnchorPane.setLeftAnchor(label, 145d);

        ComboBox<Integer> comboBox = new ComboBox<>();
        comboBox.setPromptText("Select slot index");
        for(int i = 0; i < bean.getUpgradableSlotsCount(); i++){
            comboBox.getItems().add(i+1);
        }

        AnchorPane.setTopAnchor(comboBox, 80d);
        AnchorPane.setLeftAnchor(comboBox, 210d);

        Button btn = new Button("Confirm");
        AnchorPane.setTopAnchor(btn, 155d);
        AnchorPane.setLeftAnchor(btn, 500d);

        btn.setOnAction(e -> {
            if(comboBox.getValue() != null) {
                bean.setUpgradableIndex(comboBox.getValue());
                confirmHandler.accept(bean);
                closeDialog();
            }
        });

        getRoot().setPrefSize(600, 230);
        getRoot().getChildren().setAll(label, comboBox, btn);
    }
}
