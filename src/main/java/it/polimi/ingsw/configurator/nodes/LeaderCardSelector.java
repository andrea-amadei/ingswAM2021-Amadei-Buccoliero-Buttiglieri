package it.polimi.ingsw.configurator.nodes;

import it.polimi.ingsw.client.gui.nodes.LeaderCardBox;
import it.polimi.ingsw.common.parser.raw.RawLeaderCard;
import it.polimi.ingsw.common.parser.raw.RawRequirement;
import it.polimi.ingsw.common.parser.raw.RawSpecialAbility;
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
    private VBox abilityBox;

    private final Button addFlagButton, removeFlagButton;
    private final Button addLevelFlagButton, removeLevelFlagButton;
    private final Button addResourceButton, removeResourceButton;

    private final VBox flagBox, levelFlagBox, resourceBox;

    private LeaderCardBox leaderCardBox;

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
        hBox.setSpacing(52d);
        hBox.getChildren().addAll(addFlagButton, removeFlagButton);
        flagBox.getChildren().addAll(label, hBox);

        levelFlagBox = new VBox();
        levelFlagBox.setSpacing(20d);
        label = new Label("Level Flag:");
        hBox = new HBox();
        hBox.setSpacing(52d);
        hBox.getChildren().addAll(addLevelFlagButton, removeLevelFlagButton);
        levelFlagBox.getChildren().addAll(label, hBox);

        resourceBox = new VBox();
        resourceBox.setSpacing(20d);
        label = new Label("Resource:");
        hBox = new HBox();
        hBox.setSpacing(52d);
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

        abilityBox = new VBox();
        abilityBox.setSpacing(30d);

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
                    ((StorageAbilitySelector) ability).setName("leader_" + id.getText());
                    break;
            }

            abilityBox.getChildren().set(1, (VBox) ability);
        });

        ability = new ConversionAbilitySelector();
        abilityBox.getChildren().addAll(abilityComboBox, (VBox) ability);

        leaderCardBox = new LeaderCardBox();

        this.setSpacing(20d);
        this.getChildren().addAll(vBox1, flagBox, levelFlagBox, resourceBox, abilityBox, leaderCardBox);
    }

    public RawLeaderCard getRawLeaderCard() {
        List<RawSpecialAbility> abilities = new ArrayList<>();
        List<RawRequirement > requirements = new ArrayList<>();

        for(FlagRequirementSelector req : flagRequirements)
            requirements.add(req.getRawRequirement());

        for(LevelFlagRequirementSelector req : levelFlagRequirements)
            requirements.add(req.getRawRequirement());

        for(ResourceRequirementSelector req : resourceRequirements)
            requirements.add(req.getRawRequirement());

        abilities.add(ability.getRawSpecialAbility());

        return new RawLeaderCard(
                Integer.parseInt(id.getText()),
                name.getText(),
                Integer.parseInt(points.getText()),
                abilities,
                requirements
        );
    }

    public void setRawLeaderCard(RawLeaderCard rawLeaderCard) {
        id.setText(String.valueOf(rawLeaderCard.getId()));
        name.setText(rawLeaderCard.getName());
        points.setText(String.valueOf(rawLeaderCard.getPoints()));

        for(FlagRequirementSelector req : flagRequirements)
            removeFlag();

        for(LevelFlagRequirementSelector req : levelFlagRequirements)
            removeLevelFlag();

        for(ResourceRequirementSelector req : resourceRequirements)
            removeResource();

        for(RawRequirement req : rawLeaderCard.getRequirements())
            switch(req.getType()) {
                case "flag":
                    addFlag();
                    flagRequirements.get(flagRequirements.size() - 1).setRawRequirement(req);
                    break;

                case "level_flag":
                    addLevelFlag();
                    levelFlagRequirements.get(levelFlagRequirements.size() - 1).setRawRequirement(req);
                    break;

                case "resource":
                    addResource();
                    resourceRequirements.get(resourceRequirements.size() - 1).setRawRequirement(req);
                    break;
            }

        RawSpecialAbility rawAbility = rawLeaderCard.getAbilities().get(0);

        switch(rawAbility.getType()) {
            case "conversion":
                abilityComboBox.getSelectionModel().select("conversion");
                ability = new ConversionAbilitySelector();
                ability.setRawSpecialAbility(rawAbility);
                abilityBox.getChildren().set(1, (VBox) ability);
                break;

            case "crafting":
                abilityComboBox.getSelectionModel().select("crafting");
                ability = new CraftingSelector();
                ability.setRawSpecialAbility(rawAbility);
                abilityBox.getChildren().set(1, (VBox) ability);
                break;

            case "discount":
                abilityComboBox.getSelectionModel().select("discount");
                ability = new DiscountAbilitySelector();
                ability.setRawSpecialAbility(rawAbility);
                abilityBox.getChildren().set(1, (VBox) ability);
                break;

            case "storage":
                abilityComboBox.getSelectionModel().select("storage");
                ability = new StorageAbilitySelector();
                ability.setRawSpecialAbility(rawAbility);
                abilityBox.getChildren().set(1, (VBox) ability);
                break;
        }
    }

    public void updatePreview() {
        leaderCardBox = new LeaderCardBox();
        leaderCardBox.setIsCovered(false);
        leaderCardBox.setRawLeaderCard(getRawLeaderCard());

        this.getChildren().set(this.getChildren().size() - 1, leaderCardBox);
    }

    public void setId(int id) {
        this.id.setText(String.valueOf(id));
    }
}
