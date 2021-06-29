package it.polimi.ingsw.configurator;

import it.polimi.ingsw.client.gui.nodes.FaithPath;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.model.FaithPathGroup;
import it.polimi.ingsw.model.FaithPathTile;
import it.polimi.ingsw.parser.JSONParser;
import it.polimi.ingsw.parser.JSONSerializer;
import it.polimi.ingsw.parser.raw.RawFaithPathGroup;
import it.polimi.ingsw.parser.raw.RawFaithPathTile;
import it.polimi.ingsw.utils.ResourceLoader;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

public class ConfiguratorController {
    @FXML
    public TabPane root;

    @FXML
    public AnchorPane faithPathPane;

    @FXML
    public VBox groupsVBox;
    private final List<HBox> groups = new ArrayList<>();
    private final List<TextField> groupPoints = new ArrayList<>();
    private Button groupAddButton, groupRemoveButton;

    @FXML
    public VBox tilesVBox;
    private final List<HBox> tiles = new ArrayList<>();
    private final List<TextField> xs = new ArrayList<>();
    private final List<TextField> ys = new ArrayList<>();
    private final List<TextField> tilePoints = new ArrayList<>();
    private final List<TextField> tileGroups = new ArrayList<>();
    private final List<CheckBox> tileChecks = new ArrayList<>();
    private final List<Label> tileErrors = new ArrayList<>();
    private Button tileAddButton, tileRemoveButton;

    private FaithPath faithPath;

    private int getIntFromTextField(TextField textField) {
        int n;
        try { n = Integer.parseInt(textField.getText()); } catch (NumberFormatException e) { n = 0; }

        return n;
    }

    private void createGroup() {
        TextField textField;

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPrefHeight(40d);
        hBox.setSpacing(20d);
        VBox.setMargin(hBox, new Insets(0, 0, 0, 20d));

        hBox.getChildren().add(new Label("Group:"));

        textField = new TextField(String.valueOf(groups.size() + 1));
        textField.setDisable(true);
        textField.setEditable(false);
        textField.setPrefHeight(25d);
        textField.setPrefWidth(50d);
        hBox.getChildren().add(textField);

        hBox.getChildren().add(new Label("Points granted:"));

        textField = new TextField(String.valueOf(groups.size() + 2));
        textField.setPrefHeight(25d);
        textField.setPrefWidth(80d);
        TextField finalTextField = textField;
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("\\d*")) {
                finalTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        hBox.getChildren().add(textField);
        groupPoints.add(textField);

        if(groups.size() != 0) {
            groups.get(groups.size() - 1).getChildren().remove(groupAddButton);
            groups.get(groups.size() - 1).getChildren().remove(groupRemoveButton);
        }

        hBox.getChildren().add(groupAddButton);
        hBox.getChildren().add(groupRemoveButton);

        groupRemoveButton.setDisable(false);

        groupsVBox.getChildren().add(hBox);
        groups.add(hBox);
    }

    private void removeGroup() {
        if(groups.size() == 1)
            return;

        groupsVBox.getChildren().remove(groups.get(groups.size() - 1));

        groups.remove(groups.size() - 1);
        groupPoints.remove(groupPoints.size() - 1);

        groups.get(groups.size() - 1).getChildren().add(groupAddButton);
        groups.get(groups.size() - 1).getChildren().add(groupRemoveButton);

        if(groups.size() == 1)
            groupRemoveButton.setDisable(true);
    }

    private void createTile() {
        Label label;
        TextField textField;
        CheckBox checkBox;

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPrefHeight(40d);
        hBox.setSpacing(20d);
        VBox.setMargin(hBox, new Insets(0, 0, 0, 20d));

        // TILE
        hBox.getChildren().add(new Label("Tile:"));

        textField = new TextField(String.valueOf(tiles.size()));
        textField.setDisable(true);
        textField.setEditable(false);
        textField.setPrefHeight(25d);
        textField.setPrefWidth(50d);
        hBox.getChildren().add(textField);

        // X
        hBox.getChildren().add(new Label("X:"));

        textField = new TextField(String.valueOf(tiles.size() + 1));
        textField.setPrefHeight(25d);
        textField.setPrefWidth(50d);
        TextField finalTextField1 = textField;
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("\\d*")) {
                finalTextField1.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        hBox.getChildren().add(textField);
        xs.add(textField);

        // Y
        hBox.getChildren().add(new Label("Y:"));

        textField = new TextField("1");
        textField.setPrefHeight(25d);
        textField.setPrefWidth(50d);
        TextField finalTextField2 = textField;
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("\\d*")) {
                finalTextField2.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        hBox.getChildren().add(textField);
        ys.add(textField);

        // POINTS
        hBox.getChildren().add(new Label("Points:"));

        textField = new TextField("0");
        textField.setPrefHeight(25d);
        textField.setPrefWidth(80d);
        TextField finalTextField3 = textField;
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("\\d*"))
                finalTextField3.setText(newValue.replaceAll("[^\\d]", ""));
        });
        hBox.getChildren().add(textField);
        tilePoints.add(textField);

        // GROUP
        hBox.getChildren().add(new Label("Group:"));

        textField = new TextField("0");
        textField.setPrefHeight(25d);
        textField.setPrefWidth(80d);
        TextField finalTextField4 = textField;
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("\\d*")) {
                finalTextField4.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        hBox.getChildren().add(textField);
        tileGroups.add(textField);

        // CHECK
        hBox.getChildren().add(new Label("Check:"));

        checkBox = new CheckBox();
        hBox.getChildren().add(checkBox);
        tileChecks.add(checkBox);

        // ERROR LABEL
        label = new Label("");
        label.setPrefWidth(400d);
        label.setStyle("-fx-text-fill: red;");
        hBox.getChildren().add(label);
        tileErrors.add(label);

        if(tiles.size() != 0) {
            tiles.get(tiles.size() - 1).getChildren().remove(tileAddButton);
            tiles.get(tiles.size() - 1).getChildren().remove(tileRemoveButton);
        }

        hBox.getChildren().add(tileAddButton);
        hBox.getChildren().add(tileRemoveButton);

        tileRemoveButton.setDisable(false);

        tilesVBox.getChildren().add(hBox);
        tiles.add(hBox);
    }

    private void removeTile() {
        if(tiles.size() == 1)
            return;

        tilesVBox.getChildren().remove(tiles.get(tiles.size() - 1));

        tiles.remove(tiles.size() - 1);
        xs.remove(xs.size() - 1);
        ys.remove(ys.size() - 1);
        tilePoints.remove(tilePoints.size() - 1);
        tileGroups.remove(tileGroups.size() - 1);
        tileChecks.remove(tileChecks.size() - 1);
        tileErrors.remove(tileErrors.size() - 1);

        tiles.get(tiles.size() - 1).getChildren().add(tileAddButton);
        tiles.get(tiles.size() - 1).getChildren().add(tileRemoveButton);

        if(tiles.size() == 1)
            tileRemoveButton.setDisable(true);
    }

    private List<RawFaithPathGroup> createGroupList() {
        List<RawFaithPathGroup> list = new ArrayList<>();

        for(int i = 0; i < groupPoints.size(); i++) {
            list.add(new RawFaithPathGroup(i + 1, getIntFromTextField(groupPoints.get(i))));
        }

        return list;
    }

    private List<RawFaithPathTile> createTileList() {
        List<RawFaithPathTile> list = new ArrayList<>();
        Map<Integer, Set<Integer>> coordinates = new HashMap<>();
        List<Boolean> checksPerGroup;
        boolean error = false;
        int i, j;

        checksPerGroup = new ArrayList<>();

        for(i = 0; i < groups.size(); i++) {
            checksPerGroup.add(false);
        }

        for(i = 0; i < tiles.size(); i++) {
            tileErrors.get(i).setText("");

            // check for duplicate tiles
            if (coordinates.containsKey(getIntFromTextField(xs.get(i)))) {
                if (!coordinates.get(getIntFromTextField(xs.get(i))).add(getIntFromTextField(ys.get(i)))) {
                    tileErrors.get(i).setText("Different tiles cannot share the same coordinates");
                    error = true;
                }
            } else {
                int finalI = i;
                coordinates.put(getIntFromTextField(xs.get(i)), new HashSet<>() {{
                    add(getIntFromTextField(ys.get(finalI)));
                }});
            }

            // check for groups
            if(getIntFromTextField(tileGroups.get(i)) > groups.size()) {
                tileErrors.get(i).setText("The selected group does not exists");
                error = true;
            }

            if(getIntFromTextField(tileGroups.get(i)) == 0 && tileChecks.get(i).isSelected()) {
                tileErrors.get(i).setText("Tiles without group cannot be a check");
                error = true;
            }

            if(getIntFromTextField(tileGroups.get(i)) != 0 && tileChecks.get(i).isSelected()) {
                if(checksPerGroup.get(getIntFromTextField(tileGroups.get(i)) - 1)) {
                    tileErrors.get(i).setText("More tiles from the same group cannot be both checks");
                    error = true;
                } else
                    checksPerGroup.set(getIntFromTextField(tileGroups.get(i)) - 1, true);
            }
        }

        for(i = 0; i < checksPerGroup.size(); i++) {
            if(!checksPerGroup.get(i))
                for(j = 0; j < tileGroups.size(); j++)
                    if(getIntFromTextField(tileGroups.get(j)) == i + 1) {
                        tileErrors.get(j).setText("One tile per group must be a check");
                        error = true;
                    }
        }

        if(error)
            throw new IllegalArgumentException();

        for(i = 0; i < tiles.size(); i++) {
            list.add(new RawFaithPathTile(
                    i,
                    getIntFromTextField(xs.get(i)),
                    getIntFromTextField(ys.get(i)),
                    getIntFromTextField(tilePoints.get(i)),
                    getIntFromTextField(tileGroups.get(i)),
                    tileChecks.get(i).isSelected())
            );
        }

        return list;
    }

    public void initialize() {
        int i;

        groupAddButton = new Button("+");
        groupAddButton.setOnAction(actionEvent -> createGroup());

        groupRemoveButton = new Button("X");
        groupRemoveButton.setOnAction(actionEvent -> removeGroup());

        tileAddButton = new Button("+");
        tileAddButton.setOnAction(actionEvent -> createTile());

        tileRemoveButton = new Button("X");
        tileRemoveButton.setOnAction(actionEvent -> removeTile());

        List<RawFaithPathTile> defaultTiles;
        List<RawFaithPathGroup> defaultGroups;

        try {
            it.polimi.ingsw.model.FaithPath fp = JSONParser.parseFaithPath(ResourceLoader.loadFile("cfg/faith.json"));
            defaultTiles = fp.getTiles().stream().map(FaithPathTile::toRaw).collect(Collectors.toList());
            defaultGroups = fp.getFaithGroupList().stream().map(FaithPathGroup::toRaw).collect(Collectors.toList());
        } catch (ParserException e) {
            throw new IllegalArgumentException("Loading of standard faith path failed unexpectedly!");
        }

        for(i = 0; i < defaultGroups.size(); i++) {
            createGroup();
            groupPoints.get(i).setText(String.valueOf(defaultGroups.get(i).getPoints()));
        }

        for(i = 0; i < defaultTiles.size(); i++) {
            createTile();
            xs.get(i).setText(String.valueOf(defaultTiles.get(i).getX()));
            ys.get(i).setText(String.valueOf(defaultTiles.get(i).getY()));
            tilePoints.get(i).setText(String.valueOf(defaultTiles.get(i).getVictoryPoints()));
            tileGroups.get(i).setText(String.valueOf(defaultTiles.get(i).getPopeGroup()));
            tileChecks.get(i).setSelected(defaultTiles.get(i).isPopeCheck());
        }
    }

    @FXML
    public boolean test() {
        faithPathPane.getChildren().remove(faithPath);

        List<RawFaithPathGroup> groups;
        List<RawFaithPathTile> tiles;

        try {
            groups = createGroupList();
        } catch (RuntimeException e) {
            return false;
        }

        try {
            tiles = createTileList();
        } catch (RuntimeException e) {
            return false;
        }

        faithPath = new FaithPath(tiles, groups);
        faithPath.getTransforms().add(new Scale(0.5d, 0.5d));
        AnchorPane.setLeftAnchor(faithPath, 460d);
        AnchorPane.setTopAnchor(faithPath, 50d);

        faithPathPane.getChildren().add(faithPath);

        return true;
    }

    @FXML
    public void generate() {
        if(!test())
            return;

        String str = "{\"tiles\":" + JSONSerializer.toJson(createTileList()) + ",\"groups\":" + JSONSerializer.toJson(createGroupList()) + "}";

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save faith.json");
        fileChooser.setInitialFileName("faith.json");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json"));
        File file = fileChooser.showSaveDialog(root.getScene().getWindow());

        if(file != null) {
            try {
                PrintWriter writer;
                writer = new PrintWriter(file);
                writer.println(str);
                writer.close();
            } catch (IOException ex) {
                System.out.println("Unable to save file");
            }
        }
    }
}
