package it.polimi.ingsw.configurator.nodes;

import it.polimi.ingsw.server.model.leader.SpecialAbility;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class LeaderCardSelector extends HBox {
    private final TextField id;
    private final TextField name;
    private final TextField points;

    private final List<FlagRequirementSelector> flagRequirements = new ArrayList<>();
    private final List<LevelFlagRequirementSelector> levelFlagRequirements = new ArrayList<>();
    private final List<ResourceRequirementSelector> resourceRequirements = new ArrayList<>();

    private final ComboBox<String> abilityComboBox;
    private AbilitySelector ability;

    private final Button addFlagButton, removeFlagButton;
    private final Button addLevelFlagButton, removeLevelFlagButton;
    private final Button addResourceButton, removeResourceButton;

    private final VBox flagBox, levelFlagBox, resourceBox;

    private void addFlag() {
        FlagRequirementSelector flagRequirement = new FlagRequirementSelector();

        flagRequirements.add(flagRequirement);
        flagBox.getChildren().add(flagBox.getChildren().size() - 1, flagRequirement);

        removeFlagButton.setDisable(false);
    }

    private void removeFlag() {
        if(flagBox.getChildren().size() == 2)
            return;
        
        flagBox.getChildren().remove(flagBox.getChildren().size() - 2);
        flagRequirements.remove(flagRequirements.size() - 1);

        if(flagBox.getChildren().size() == 2)
            removeFlagButton.setDisable(true);
    }

    private void addLevelFlag() {
        LevelFlagRequirementSelector levelFlagRequirement = new LevelFlagRequirementSelector();

        levelFlagRequirements.add(levelFlagRequirement);
        levelFlagBox.getChildren().add(levelFlagBox.getChildren().size() - 1, levelFlagRequirement);

        removeLevelFlagButton.setDisable(false);
    }

    private void removeLevelFlag() {
        if(levelFlagBox.getChildren().size() == 2)
            return;

        levelFlagBox.getChildren().remove(levelFlagBox.getChildren().size() - 2);
        levelFlagRequirements.remove(levelFlagRequirements.size() - 1);

        if(levelFlagBox.getChildren().size() == 2)
            removeLevelFlagButton.setDisable(true);
    }

    private void addResource() {
        ResourceRequirementSelector resourceRequirement = new ResourceRequirementSelector();

        resourceRequirements.add(resourceRequirement);
        resourceBox.getChildren().add(resourceBox.getChildren().size() - 1, resourceRequirement);

        removeResourceButton.setDisable(false);
    }

    private void removeResource() {
        if(resourceBox.getChildren().size() == 2)
            return;

        resourceBox.getChildren().remove(resourceBox.getChildren().size() - 2);
        resourceRequirements.remove(resourceRequirements.size() - 1);

        if(resourceBox.getChildren().size() == 2)
            removeResourceButton.setDisable(true);
    }

    public LeaderCardSelector() {
        Label label;

        addFlagButton = new Button("+");
        addFlagButton.setOnAction(actionEvent -> addFlag());
        removeFlagButton = new Button("X");
        removeFlagButton.setOnAction(actionEvent -> removeFlag());
        removeFlagButton.setDisable(true);

        addLevelFlagButton = new Button("+");
        addLevelFlagButton.setOnAction(actionEvent -> addLevelFlag());
        removeLevelFlagButton = new Button("X");
        removeLevelFlagButton.setOnAction(actionEvent -> removeLevelFlag());
        removeLevelFlagButton.setDisable(true);

        addResourceButton = new Button("+");
        addResourceButton.setOnAction(actionEvent -> addResource());
        removeResourceButton = new Button("X");
        removeResourceButton.setOnAction(actionEvent -> removeResource());
        removeResourceButton.setDisable(true);

        HBox hBox;

        flagBox = new VBox();
        flagBox.setSpacing(20d);
        label = new Label("Flag:");
        hBox = new HBox();
        hBox.setSpacing(20d);
        hBox.getChildren().addAll(addFlagButton, removeFlagButton);
        flagBox.getChildren().addAll(label, hBox);

        levelFlagBox = new VBox();
        levelFlagBox.setSpacing(20d);
        label = new Label("Level Flag:");
        hBox = new HBox();
        hBox.setSpacing(20d);
        hBox.getChildren().addAll(addLevelFlagButton, removeLevelFlagButton);
        levelFlagBox.getChildren().addAll(label, hBox);

        resourceBox = new VBox();
        resourceBox.setSpacing(20d);
        label = new Label("Resource:");
        hBox = new HBox();
        hBox.setSpacing(20d);
        hBox.getChildren().addAll(addResourceButton, removeResourceButton);
        resourceBox.getChildren().addAll(label, hBox);

        VBox vBox1, vBox2;
        HBox hBox1, hBox2, hBox3;

        id = new TextField("0");
        id.setPrefWidth(80d);
        id.setEditable(false);
        id.setDisable(true);

        name = new TextField("Name");
        name.setPrefWidth(80d);

        points = new TextField("1");
        points.setPrefWidth(80d);
        points.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("\\d*")) {
                points.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        label = new Label("Id:");
        label.setPrefWidth(80d);
        hBox1 = new HBox();
        hBox1.getChildren().addAll(label, id);

        label = new Label("Name:");
        label.setPrefWidth(80d);
        hBox2 = new HBox();
        hBox2.getChildren().addAll(label, name);

        label = new Label("Points:");
        label.setPrefWidth(80d);
        hBox3 = new HBox();
        hBox3.getChildren().addAll(label, points);

        vBox1 = new VBox();
        vBox1.setSpacing(20d);
        vBox1.getChildren().addAll(hBox1, hBox2, hBox3);

        vBox2 = new VBox();
        vBox2.setSpacing(30d);

        abilityComboBox = new ComboBox<>();
        abilityComboBox.setPrefWidth(250d);
        abilityComboBox.setItems(FXCollections.observableArrayList("conversion", "crafting", "discount", "storage"));
        abilityComboBox.getSelectionModel().selectFirst();
        abilityComboBox.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            switch(newValue) {
                case "conversion":
                    ability = new ConversionAbilitySelector();
                    break;

                case "crafting":
                    ability = new CraftingSelector();
                    break;

                case "discount":
                    ability = new DiscountAbilitySelector();
                    break;

                case "storage":
                    ability = new StorageAbilitySelector();
                    break;
            }

            vBox2.getChildren().set(1, (VBox) ability);
        });

        ability = new ConversionAbilitySelector();
        vBox2.getChildren().addAll(abilityComboBox, (VBox) ability);

        this.setSpacing(20d);
        this.getChildren().addAll(vBox1, flagBox, levelFlagBox, resourceBox, vBox2);
    }
}
