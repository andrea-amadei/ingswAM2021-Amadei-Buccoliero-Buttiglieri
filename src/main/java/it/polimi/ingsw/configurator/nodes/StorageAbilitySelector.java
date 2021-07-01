package it.polimi.ingsw.configurator.nodes;

import it.polimi.ingsw.common.parser.raw.RawSpecialAbility;
import it.polimi.ingsw.server.Logger;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Set;

public class StorageAbilitySelector extends VBox implements AbilitySelector {

    private final ComboBox<String> resourceComboBox;
    private final ComboBox<Integer> amountComboBox;
    private final TextField nameTextField;

    private final Set<String> possibleResourceTypes = Set.of("gold", "servant", "shield", "stone");

    public StorageAbilitySelector(){
        resourceComboBox = new ComboBox<>();
        amountComboBox = new ComboBox<>();

        resourceComboBox.getItems().addAll("gold", "servant", "shield", "stone");
        amountComboBox.getItems().addAll(1, 2, 3);

        resourceComboBox.getSelectionModel().selectFirst();
        amountComboBox.getSelectionModel().selectFirst();

        resourceComboBox.setPrefWidth(80);
        amountComboBox.setPrefWidth(80);

        nameTextField = new TextField("leader_");
        nameTextField.setPrefWidth(80);
        nameTextField.setDisable(true);
        nameTextField.setEditable(false);

        HBox hBox0 = new HBox();
        Label label0 = new Label("Shelf name");
        label0.setPrefWidth(80d);
        hBox0.getChildren().addAll(label0, nameTextField);
        hBox0.setAlignment(Pos.CENTER);

        HBox hBox = new HBox();
        Label label = new Label("Shelf type");
        label.setPrefWidth(80d);
        hBox.getChildren().addAll(label, resourceComboBox);
        hBox.setAlignment(Pos.CENTER);

        HBox hBox2 = new HBox();
        Label label2 = new Label("Shelf size");
        label2.setPrefWidth(80d);
        hBox2.getChildren().addAll(label2, amountComboBox);
        hBox2.setAlignment(Pos.CENTER);

        this.setSpacing(20);
        this.setPrefWidth(250);
        this.setMaxWidth(Double.NEGATIVE_INFINITY);

        this.getChildren().setAll(hBox0, hBox, hBox2);
    }

    public void setResourceType(String resource){
        if(possibleResourceTypes.contains(resource)){
            resourceComboBox.getSelectionModel().select(resource);
        }else{
            Logger.log("Wrong resource set in the StorageAbilitySelector");
        }
    }

    public void setAmount(int amount){
        if(amount >= 1 && amount <= 3){
            amountComboBox.getSelectionModel().select((Integer)amount);
        }else{
            Logger.log("Wrong amount set in the StorageAbilitySelector");
        }
    }

    public void setName(String name){
        this.nameTextField.setText(name);
    }

    public String getResourceType(){
        return resourceComboBox.getValue().toLowerCase();
    }

    public int getAmount(){
        return amountComboBox.getValue();
    }

    public String getName(){
        return nameTextField.getText();
    }

    public RawSpecialAbility getRawSpecialAbility() {
        return new RawSpecialAbility(getName(), getResourceType(), getAmount());
    }

    public void setRawSpecialAbility(RawSpecialAbility rawSpecialAbility) {
        if(!rawSpecialAbility.getType().equals("storage")) {
            Logger.log("tried to set an ability of type " + rawSpecialAbility.getType() + " to a StorageAbilitySelector");
            return;
        }
        setAmount(rawSpecialAbility.getAmount());
        setResourceType(rawSpecialAbility.getAcceptedTypes());
        setName(rawSpecialAbility.getStorageName());
    }
}
