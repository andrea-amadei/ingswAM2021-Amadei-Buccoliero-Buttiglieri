package it.polimi.ingsw.client.gui.nodes;

import it.polimi.ingsw.client.gui.events.SwitchPlayerEvent;
import it.polimi.ingsw.parser.raw.RawLevelFlag;
import it.polimi.ingsw.utils.ResourceLoader;
import javafx.beans.property.ListProperty;
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
import java.util.*;
import java.util.stream.Collectors;

public class ScoreboardBox extends GridPane {

    private ListProperty<String> names;
    private ListProperty<Integer> points;
    private ListProperty<List<RawLevelFlag>> flags;
    private ListProperty<Boolean> disconnected;

    @FXML
    private GridPane pane;

    private List<Label> nameLabels;
    private List<Button> buttons;
    private List<PointsBox> pointsBoxes;
    private List<GridPane> flagGrids;
    private List<FlagBox> flagBoxes;

    private final List<String> PLAYER_COLORS = new ArrayList<>(Arrays.asList("#3568AA", "#892F96", "#3AA739", "#988330"));
    private final int MAX_FLAGS = 8;

    private int oldPlayerNumber;

    public ScoreboardBox() {
        this(Arrays.asList("Player 1", "Player 2", "Player 3", "Player 4"));
    }

    public ScoreboardBox(List<String> playerNames) {
        FXMLLoader fxmlLoader = new FXMLLoader(ResourceLoader.getResource("jfx/custom/ScoreboardBox.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException("Unable to load custom element '" + getClass().getSimpleName() + "': " + exception);
        }

        names = new SimpleListProperty<>(this, "namesProperty", FXCollections.observableList(playerNames));
        points = new SimpleListProperty<>(this, "pointsProperty", FXCollections.observableList(Arrays.asList(
                0, 0, 0, 0 )));
        flags = new SimpleListProperty<>(this, "flagsProperty", FXCollections.observableList(Arrays.asList(
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList()
        )));
        disconnected = new SimpleListProperty<>(this, "disconnectedProperty", FXCollections.observableList(Arrays.asList(
                false, false, false, false )));

        setup();
        update();
    }

    private void setup() {
        nameLabels = new ArrayList<>();
        buttons = new ArrayList<>();
        pointsBoxes = new ArrayList<>();
        flagGrids = new ArrayList<>();
        flagBoxes = new ArrayList<>();

        Label label;
        Button button;
        PointsBox pointsBox;
        GridPane gridPane;
        FlagBox flagBox;
        Glyph glyph;
        int i, j;

        for(i = 0; i < names.size(); i++) {
            // USERNAME
            label = new Label();
            label.setFont(new Font("Times New Roman Bold", 24.0));
            label.setText(names.get(i));
            label.setStyle("-fx-text-fill: " + PLAYER_COLORS.get(i) + ";");
            GridPane.setRowIndex(label, i);
            GridPane.setColumnIndex(label, 0);

            pane.getChildren().add(label);
            nameLabels.add(label);

            // BUTTON
            button = new Button();
            button.setPrefHeight(30d);
            button.setPrefWidth(60d);
            GridPane.setRowIndex(button, i);
            GridPane.setColumnIndex(button, 1);

            int finalI = i;
            button.setOnAction(e ->{
                fireEvent(new SwitchPlayerEvent(names.get(finalI)));
            });

            glyph = new Glyph("FontAwesome", "EYE");
            glyph.setScaleX(2);
            glyph.setScaleY(2);
            glyph.setStyle("-fx-text-fill: " + PLAYER_COLORS.get(i) + ";");
            button.setGraphic(glyph);

            pane.getChildren().add(button);
            buttons.add(button);

            // POINTS BOX
            pointsBox = new PointsBox(points.get(i));
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
                flagBoxes.add(flagBox);
            }
        }

        oldPlayerNumber = getNames().size();
    }

    private void update() {
        if(names.size() != oldPlayerNumber) {
            pane.getChildren().clear();
            setup();
        }

        int i, j;

        for(i = 0; i < names.size(); i++) {
            nameLabels.get(i).setText(names.get(i));
            nameLabels.get(i).setDisable(disconnected.get(i));
            pointsBoxes.get(i).setPoints(points.get(i));

            for(j = 0; j < MAX_FLAGS; j++) {
                if(j < flags.get(i).size()) {
                    flagBoxes.get(i * MAX_FLAGS + j).setFlag(flags.get(i).get(j).getColor().name().toLowerCase());
                    flagBoxes.get(i * MAX_FLAGS + j).setLevel(flags.get(i).get(j).getLevel());
                    flagBoxes.get(i * MAX_FLAGS + j).setVisible(true);
                }
                else {
                    flagBoxes.get(i * MAX_FLAGS + j).setVisible(false);
                }
            }
        }
    }

    private List<RawLevelFlag> sortFlags(List<RawLevelFlag> flags) {
        return flags.stream()
                .sorted(
                        Comparator.comparingInt(RawLevelFlag::getLevel)
                                .thenComparing(x -> x.getColor().name())
                )
                .collect(Collectors.toList());
    }

    public ObservableList<String> getNames() {
        return names.get();
    }

    public ListProperty<String> namesProperty() {
        return names;
    }

    public void setNames(ObservableList<String> names) {
        this.names.set(names);
        update();
    }

    public ObservableList<Integer> getPoints() {
        return points.get();
    }

    public ListProperty<Integer> pointsProperty() {
        return points;
    }

    public void setPoints(ObservableList<Integer> points) {
        this.points.set(points);
        update();
    }

    public ObservableList<List<RawLevelFlag>> getFlags() {
        return flags.get();
    }

    public ListProperty<List<RawLevelFlag>> flagsProperty() {
        return flags;
    }

    public void setFlags(ObservableList<List<RawLevelFlag>> flags) {
        for(int i = 0; i < flags.size(); i++)
            setPlayerFlagsProperty(i, sortFlags(flags.get(i)));

        update();
    }

    public ObservableList<Boolean> getDisconnected() {
        return disconnected.get();
    }

    public ListProperty<Boolean> disconnectedProperty() {
        return disconnected;
    }

    public void setDisconnected(ObservableList<Boolean> disconnected) {
        this.disconnected.set(disconnected);
        update();
    }

    public void setPlayerFlagsProperty(int playerIndex, List<RawLevelFlag> flags) {
        this.getFlags().set(playerIndex, sortFlags(flags));
        update();
    }

    public void setPlayerPointsProperty(int playerIndex, int points) {
        this.getPoints().set(playerIndex, points);
        update();
    }
}