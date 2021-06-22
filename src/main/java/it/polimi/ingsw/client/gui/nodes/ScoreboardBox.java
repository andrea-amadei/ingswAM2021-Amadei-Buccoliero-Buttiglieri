package it.polimi.ingsw.client.gui.nodes;

import it.polimi.ingsw.gamematerials.FlagColor;
import it.polimi.ingsw.gamematerials.LevelFlag;
import it.polimi.ingsw.parser.raw.RawLevelFlag;
import it.polimi.ingsw.utils.ResourceLoader;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import org.controlsfx.glyphfont.Glyph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ScoreboardBox extends GridPane {

    public ListProperty<String> namesProperty;
    public ListProperty<Integer> pointsProperty;
    public ListProperty<List<RawLevelFlag>> flagsProperty;
    public ListProperty<Boolean> disconnectedProperty;

    @FXML
    public GridPane pane;

    private List<Label> names;
    private List<Button> buttons;
    private List<PointsBox> pointsBoxes;
    private List<GridPane> flagGrids;
    private List<FlagBox> flags;

    private final List<String> PLAYER_COLORS = new ArrayList<>(Arrays.asList("#3568AA", "#892F96", "#3AA739", "#988330"));
    private final int MAX_FLAGS = 8;

    public ScoreboardBox() {
        FXMLLoader fxmlLoader = new FXMLLoader(ResourceLoader.getResource("jfx/custom/ScoreboardBox.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException("Unable to load custom element '" + getClass().getSimpleName() + "': " + exception);
        }

        namesProperty = new SimpleListProperty<>(this, "namesProperty", FXCollections.observableList(Arrays.asList(
                "Player 1", "Player 2", "Player 3", "Player 4" )));
        pointsProperty = new SimpleListProperty<>(this, "pointsProperty", FXCollections.observableList(Arrays.asList(
                0, 0, 0, 0 )));
        flagsProperty = new SimpleListProperty<>(this, "flagsProperty", FXCollections.observableList(Arrays.asList(
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList()
        )));
        disconnectedProperty = new SimpleListProperty<>(this, "disconnectedProperty", FXCollections.observableList(Arrays.asList(
                false, false, false, false )));

        setup();
        update();
    }

    public void setup() {
        if(namesProperty.size() != pointsProperty.size() || namesProperty.size() != flagsProperty.size() || namesProperty.size() != disconnectedProperty.size())
            throw new IllegalArgumentException("List properties must have the same size");

        names = new ArrayList<>();
        buttons = new ArrayList<>();
        pointsBoxes = new ArrayList<>();
        flagGrids = new ArrayList<>();
        flags = new ArrayList<>();

        Label label;
        Button button;
        PointsBox pointsBox;
        GridPane gridPane;
        FlagBox flagBox;
        Glyph glyph;
        int i, j;

        for(i = 0; i < namesProperty.size(); i++) {
            // USERNAME
            label = new Label();
            label.setFont(new Font("Times New Roman Bold", 24.0));
            label.setText(namesProperty.get(i));
            label.setStyle("-fx-text-fill: " + PLAYER_COLORS.get(i) + ";");
            GridPane.setRowIndex(label, i);
            GridPane.setColumnIndex(label, 0);

            pane.getChildren().add(label);
            names.add(label);

            // BUTTON
            button = new Button();
            button.setPrefHeight(30d);
            button.setPrefWidth(60d);
            GridPane.setRowIndex(button, i);
            GridPane.setColumnIndex(button, 1);

            glyph = new Glyph("FontAwesome", "EYE");
            glyph.setScaleX(2);
            glyph.setScaleY(2);
            glyph.setStyle("-fx-text-fill: " + PLAYER_COLORS.get(i) + ";");
            button.setGraphic(glyph);

            pane.getChildren().add(button);
            buttons.add(button);

            // POINTS BOX
            pointsBox = new PointsBox(pointsProperty.get(i));
            pointsBox.setScaleX(0.7d);
            pointsBox.setScaleY(0.7d);
            GridPane.setRowIndex(pointsBox, i);
            GridPane.setColumnIndex(pointsBox, 2);

            pane.getChildren().add(pointsBox);
            pointsBoxes.add(pointsBox);

            // FLAG GRID
            gridPane = new GridPane();
            for(j = 0; j < MAX_FLAGS; j++) {
                gridPane.getColumnConstraints().add(new ColumnConstraints(Double.NEGATIVE_INFINITY, 18d, Double.NEGATIVE_INFINITY));
            }
            gridPane.getRowConstraints().add(new RowConstraints(Double.NEGATIVE_INFINITY, 30d, Double.NEGATIVE_INFINITY));
            GridPane.setRowIndex(gridPane, i);
            GridPane.setColumnIndex(gridPane, 3);

            pane.getChildren().add(gridPane);
            flagGrids.add(gridPane);

            // FLAGS
            for(j = 0; j < MAX_FLAGS; j++) {
                flagBox = new FlagBox("blue", 0, 1);
                GridPane.setColumnIndex(flagBox, j);
                flagBox.setScaleX(0.7);
                flagBox.setScaleY(0.7);
                flagBox.setShowAmount(false);

                gridPane.getChildren().add(flagBox);
                flags.add(flagBox);
            }
        }
    }

    public void update() {
        if(namesProperty.size() != pointsProperty.size() || namesProperty.size() != flagsProperty.size() || namesProperty.size() != disconnectedProperty.size())
            throw new IllegalArgumentException("List properties must have the same size");

        int i, j;

        for(i = 0; i < namesProperty.size(); i++) {
            names.get(i).setText(namesProperty.get(i));
            names.get(i).setDisable(disconnectedProperty.get(i));
            pointsBoxes.get(i).setPoints(pointsProperty.get(i));

            for(j = 0; j < MAX_FLAGS; j++) {
                if(j < flagsProperty.get(i).size()) {
                    flags.get(i * MAX_FLAGS + j).setFlag(flagsProperty.get(i).get(j).getColor().name().toLowerCase());
                    flags.get(i * MAX_FLAGS + j).setLevel(flagsProperty.get(i).get(j).getLevel());
                    flags.get(i * MAX_FLAGS + j).setVisible(true);
                }
                else {
                    flags.get(i * MAX_FLAGS + j).setVisible(false);
                }
            }
        }
    }

    public ObservableList<String> getNamesProperty() {
        return namesProperty.get();
    }

    public ListProperty<String> namesPropertyProperty() {
        return namesProperty;
    }

    public ObservableList<Integer> getPointsProperty() {
        return pointsProperty.get();
    }

    public ListProperty<Integer> pointsPropertyProperty() {
        return pointsProperty;
    }

    public ObservableList<List<RawLevelFlag>> getFlagsProperty() {
        return flagsProperty.get();
    }

    public ListProperty<List<RawLevelFlag>> flagsPropertyProperty() {
        return flagsProperty;
    }

    public ObservableList<Boolean> getDisconnectedProperty() {
        return disconnectedProperty.get();
    }

    public ListProperty<Boolean> disconnectedPropertyProperty() {
        return disconnectedProperty;
    }

    public void setPointsProperty(ObservableList<Integer> pointsProperty) {
        this.pointsProperty.set(pointsProperty);
        update();
    }

    public void setPlayerFlagsProperty(int playerIndex, List<RawLevelFlag> flags) {
        this.getFlagsProperty().set(playerIndex, flags);
        update();
    }

    public void setFlagsProperty(ObservableList<List<RawLevelFlag>> flagsProperty) {
        this.flagsProperty.set(flagsProperty);
        update();
    }

    public void setDisconnectedProperty(ObservableList<Boolean> disconnectedProperty) {
        this.disconnectedProperty.set(disconnectedProperty);
        update();
    }
}