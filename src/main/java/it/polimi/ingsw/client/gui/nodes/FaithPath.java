package it.polimi.ingsw.client.gui.nodes;

import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.model.FaithPathGroup;
import it.polimi.ingsw.model.FaithPathTile;
import it.polimi.ingsw.model.holder.FaithHolder;
import it.polimi.ingsw.parser.JSONParser;
import it.polimi.ingsw.parser.raw.RawFaithPathGroup;
import it.polimi.ingsw.parser.raw.RawFaithPathTile;
import it.polimi.ingsw.server.Logger;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.utils.ResourceLoader;
import it.polimi.ingsw.utils.Triplet;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.control.Label;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.controlsfx.control.spreadsheet.Grid;
import org.controlsfx.glyphfont.Glyph;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class FaithPath extends GridPane {

    private IntegerProperty player1PositionsProperty;
    private IntegerProperty player2PositionsProperty;
    private IntegerProperty player3PositionsProperty;
    private IntegerProperty player4PositionsProperty;
    private IntegerProperty lorenzoPositionProperty;

    private ListProperty<FaithHolder.CheckpointStatus> player1CheckpointsStatusProperty;
    private ListProperty<FaithHolder.CheckpointStatus> player2CheckpointsStatusProperty;
    private ListProperty<FaithHolder.CheckpointStatus> player3CheckpointsStatusProperty;
    private ListProperty<FaithHolder.CheckpointStatus> player4CheckpointsStatusProperty;

    private List<RawFaithPathTile> tiles;
    private List<RawFaithPathGroup> groups;

    private List<GridPane> grids;
    private List<GridPane> checkGrids;

    private final int nRows;
    private final int nCols;

    private final String BORDER_NEUTRAL_COLOR = "darkred";
    private final List<String> BORDER_POPE_COLORS = new ArrayList<>(Arrays.asList("#f4ac33", "#ec8e35", "#ec5b31"));

    private final String NEUTRAL_COLOR = "lightgrey";
    private final String POINTS_COLOR = "lightyellow";
    private final String POPE_COLOR = "red";

    private final String TEXT_NEUTRAL_COLOR = "grey";
    private final String TEXT_POINTS_COLOR = "gold";
    private final String TEXT_POPE_COLOR = "darkred";

    private final List<String> PLAYER_COLORS = new ArrayList<>(Arrays.asList("#3568AA", "#892F96", "#3AA739", "#988330", ""));
    private final List<Double> PLAYER_HUES = new ArrayList<>(Arrays.asList(-0.9d, -0.45d, 0.58d, 0.20d, 0d));
    private final List<Double> PLAYER_BRIGHTNESS = new ArrayList<>(Arrays.asList(0d, 0d, 0d, 0d, -0.3d));
    private final List<Double> PLAYER_SATURATION = new ArrayList<>(Arrays.asList(0d, 0d, 0d, 0d, -1d));

    private final List<Integer> PLAYER_ROW_INDEX = new ArrayList<>(Arrays.asList(   0, 1, 1, 0));
    private final List<Integer> PLAYER_COLUMN_INDEX = new ArrayList<>(Arrays.asList(0, 1, 0, 1));

    @FXML
    public GridPane pane;

    public FaithPath() {
        FXMLLoader fxmlLoader = new FXMLLoader(ResourceLoader.getResource("jfx/custom/FaithPath.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException("Unable to load custom element '" + getClass().getSimpleName() + "': " + exception);
        }

        try {
            it.polimi.ingsw.model.FaithPath fp = JSONParser.parseFaithPath(ResourceLoader.loadFile("cfg/faith.json"));
            tiles = fp.getTiles().stream().map(FaithPathTile::toRaw).collect(Collectors.toList());
            groups = fp.getFaithGroupList().stream().map(FaithPathGroup::toRaw).collect(Collectors.toList());
        } catch (ParserException e) {
            throw new IllegalArgumentException("Loading of standard faith path failed unexpectedly!");
        }

        nRows = tiles.stream().max(Comparator.comparing(RawFaithPathTile::getY)).orElseThrow().getY();
        nCols = tiles.stream().max(Comparator.comparing(RawFaithPathTile::getX)).orElseThrow().getX();

        player1PositionsProperty = new SimpleIntegerProperty(this, "player1PositionsProperty", 0);
        player2PositionsProperty = new SimpleIntegerProperty(this, "player2PositionsProperty", -1);
        player3PositionsProperty = new SimpleIntegerProperty(this, "player3PositionsProperty", -1);
        player4PositionsProperty = new SimpleIntegerProperty(this, "player4PositionsProperty", -1);
        lorenzoPositionProperty = new SimpleIntegerProperty(this, "lorenzoPositionProperty", -1);

        player1CheckpointsStatusProperty = new SimpleListProperty<>(this, "player1CheckpointsStatusProperty", FXCollections.observableList(
                Arrays.asList(FaithHolder.CheckpointStatus.UNREACHED, FaithHolder.CheckpointStatus.UNREACHED, FaithHolder.CheckpointStatus.UNREACHED)));
        player2CheckpointsStatusProperty = new SimpleListProperty<>(this, "player2CheckpointsStatusProperty", FXCollections.observableList(
                Arrays.asList(FaithHolder.CheckpointStatus.UNREACHED, FaithHolder.CheckpointStatus.UNREACHED, FaithHolder.CheckpointStatus.UNREACHED)));
        player3CheckpointsStatusProperty = new SimpleListProperty<>(this, "player3CheckpointsStatusProperty", FXCollections.observableList(
                Arrays.asList(FaithHolder.CheckpointStatus.UNREACHED, FaithHolder.CheckpointStatus.UNREACHED, FaithHolder.CheckpointStatus.UNREACHED)));
        player4CheckpointsStatusProperty = new SimpleListProperty<>(this, "player4CheckpointsStatusProperty", FXCollections.observableList(
                Arrays.asList(FaithHolder.CheckpointStatus.UNREACHED, FaithHolder.CheckpointStatus.UNREACHED, FaithHolder.CheckpointStatus.UNREACHED)));
        setup();
        update();
    }

    public FaithPath(List<RawFaithPathTile> tiles, List<RawFaithPathGroup> groups) {
        FXMLLoader fxmlLoader = new FXMLLoader(ResourceLoader.getResource("jfx/custom/FaithPath.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException("Unable to load custom element '" + getClass().getSimpleName() + "': " + exception);
        }

        this.tiles = tiles;
        this.groups = groups;

        this.groups = this.groups.stream().sorted(Comparator.comparingInt(RawFaithPathGroup::getGroup)).collect(Collectors.toList());
        nRows = tiles.stream().max(Comparator.comparing(RawFaithPathTile::getY)).orElseThrow().getY();
        nCols = tiles.stream().max(Comparator.comparing(RawFaithPathTile::getX)).orElseThrow().getX();

        setup();
        update();
    }

    private int getNewX(RawFaithPathTile rawTile) {
        return rawTile.getX() - 1;
    }

    private int getNewY(RawFaithPathTile rawTile) {
        return nRows - rawTile.getY();
    }

    private void setup() {
        int i;

        int[][] positions = new int[nCols][nRows];

        AnchorPane tile;
        AnchorPane content;
        PointsBox points;
        Label label;
        GridPane playerGrid;

        for(RawFaithPathTile rawTile : tiles) {
            positions[getNewX(rawTile)][getNewY(rawTile)] = -1;
        }

        for(RawFaithPathTile rawTile : tiles.stream().filter(x -> x.getVictoryPoints() > 0).collect(Collectors.toList())) {
            // LEFT
            if(getNewX(rawTile) == 0) {
                positions[getNewX(rawTile)][getNewY(rawTile)] = 2;
            }
            else if(positions[getNewX(rawTile) - 1][getNewY(rawTile)] == 0) {
                positions[getNewX(rawTile)][getNewY(rawTile)] = 2;
                positions[getNewX(rawTile) - 1][getNewY(rawTile)] = -2;
            }

            // RIGHT
            else if(getNewX(rawTile) == nCols - 1) {
                positions[getNewX(rawTile)][getNewY(rawTile)] = 3;
            }
            else if(getNewX(rawTile) < nCols - 1 && positions[getNewX(rawTile) + 1][getNewY(rawTile)] == 0) {
                positions[getNewX(rawTile)][getNewY(rawTile)] = 3;
                positions[getNewX(rawTile) + 1][getNewY(rawTile)] = -2;
            }

            // TOP
            else if(getNewY(rawTile) == 0) {
                positions[getNewX(rawTile)][getNewY(rawTile)] = 1;
            }
            else if(positions[getNewX(rawTile)][getNewY(rawTile) - 1] == 0) {
                positions[getNewX(rawTile)][getNewY(rawTile)] = 1;
                positions[getNewX(rawTile)][getNewY(rawTile) - 1] = -2;
            }

            // BOTTOM
            else if(getNewY(rawTile) == nRows - 1) {
                positions[getNewX(rawTile)][getNewY(rawTile)] = 4;
            }
            else if(positions[getNewX(rawTile)][getNewY(rawTile) + 1] == 0) {
                positions[getNewX(rawTile)][getNewY(rawTile)] = 4;
                positions[getNewX(rawTile)][getNewY(rawTile) + 1] = -2;
            }

            // TOP (if none is left)
            else {
                positions[getNewX(rawTile)][getNewY(rawTile)] = 1;
                positions[getNewX(rawTile)][getNewY(rawTile) - 1] = -2;
            }
        }

        List<Triplet<Integer, Integer, Integer>> checks = new ArrayList<>();

        for(i = 1; i <= groups.size(); i++) {
            int fi = i;

            for (RawFaithPathTile rawTile : tiles.stream().filter(x -> x.getPopeGroup() == fi && !x.isPopeCheck()).sorted((x, y) -> Integer.compare(y.getOrder(), x.getOrder())).collect(Collectors.toList())) {
                // TOP
                if (getNewY(rawTile) > 0 && positions[getNewX(rawTile)][getNewY(rawTile) - 1] == 0) {
                    checks.add(new Triplet<>(getNewX(rawTile), getNewY(rawTile) - 1, rawTile.getPopeGroup()));
                    positions[getNewX(rawTile)][getNewY(rawTile) - 1] = -3;
                    break;
                }
                // BOTTOM
                else if (getNewY(rawTile) < nRows - 1 && positions[getNewX(rawTile)][getNewY(rawTile) + 1] == 0) {
                    checks.add(new Triplet<>(getNewX(rawTile), getNewY(rawTile) + 1, rawTile.getPopeGroup()));
                    positions[getNewX(rawTile)][getNewY(rawTile) + 1] = -3;
                    break;
                }
            }
        }

        for(i = 0; i < nCols; i++)
            pane.getColumnConstraints().add(new ColumnConstraints(Double.NEGATIVE_INFINITY, 80d, Double.NEGATIVE_INFINITY));

        for(i = 0; i < nRows; i++)
            pane.getRowConstraints().add(new RowConstraints(Double.NEGATIVE_INFINITY, 80d, Double.NEGATIVE_INFINITY));

        grids = new ArrayList<>();

        for(RawFaithPathTile rawTile : tiles) {
            tile = new AnchorPane();
            tile.setPrefHeight(80d);
            tile.setPrefWidth(80d);
            GridPane.setColumnIndex(tile, getNewX(rawTile));
            GridPane.setRowIndex(tile, getNewY(rawTile));
            tile.setMaxHeight(Double.NEGATIVE_INFINITY);
            tile.setMaxWidth(Double.NEGATIVE_INFINITY);
            tile.setMinHeight(Double.NEGATIVE_INFINITY);
            tile.setMinWidth(Double.NEGATIVE_INFINITY);

            if(rawTile.getPopeGroup() == 0)
                tile.setStyle("-fx-background-color: " + BORDER_NEUTRAL_COLOR + ";");
            else
                tile.setStyle("-fx-background-color: " + BORDER_POPE_COLORS.get(rawTile.getPopeGroup() - 1) + ";");

            content = new AnchorPane();

            if(rawTile.getPopeGroup() == 0) {
                content.setPrefWidth(76d);
                content.setPrefHeight(76d);
                AnchorPane.setLeftAnchor(content, 2d);
                AnchorPane.setTopAnchor(content, 2d);
            }
            else {
                content.setPrefWidth(74d);
                content.setPrefHeight(74d);
                AnchorPane.setLeftAnchor(content, 3d);
                AnchorPane.setTopAnchor(content, 3d);
            }

            if(rawTile.isPopeCheck())
                content.setStyle("-fx-background-color: " + POPE_COLOR + ";");
            else if(rawTile.getVictoryPoints() > 0)
                content.setStyle("-fx-background-color: " + POINTS_COLOR + ";");
            else
                content.setStyle("-fx-background-color: " + NEUTRAL_COLOR + ";");

            tile.getChildren().add(content);

            if(rawTile.getVictoryPoints() > 0) {

                points = new PointsBox(rawTile.getVictoryPoints());

                switch(positions[getNewX(rawTile)][getNewY(rawTile)]) {
                    case 1:
                        AnchorPane.setTopAnchor(points, -40d);
                        AnchorPane.setLeftAnchor(points, 10d);
                        break;

                    case 2:
                        AnchorPane.setTopAnchor(points, 10d);
                        AnchorPane.setLeftAnchor(points, -30d);
                        break;

                    case 3:
                        AnchorPane.setTopAnchor(points, 10d);
                        AnchorPane.setLeftAnchor(points, 50d);
                        break;

                    case 4:
                        AnchorPane.setTopAnchor(points, 50d);
                        AnchorPane.setLeftAnchor(points, 10d);
                        break;
                }

                tile.getChildren().add(points);
            }

            label = new Label(String.valueOf(rawTile.getOrder()));
            label.setFont(new Font("Times New Roman Bold", 40d));

            if(rawTile.getOrder() < 10) {
                AnchorPane.setTopAnchor(label, 18d);
                AnchorPane.setLeftAnchor(label, 30d);
            }
            else {
                AnchorPane.setTopAnchor(label, 18d);
                AnchorPane.setLeftAnchor(label, 20d);
            }

            if(rawTile.isPopeCheck())
                label.setStyle("-fx-text-fill: " + TEXT_POPE_COLOR + ";");
            else if(rawTile.getVictoryPoints() > 0)
                label.setStyle("-fx-text-fill: " + TEXT_POINTS_COLOR + ";");
            else
                label.setStyle("-fx-text-fill: " + TEXT_NEUTRAL_COLOR + ";");
            tile.getChildren().add(label);

            playerGrid = new GridPane();

            for(i = 0; i < 2; i++)
                playerGrid.getColumnConstraints().add(new ColumnConstraints(Double.NEGATIVE_INFINITY, 38d, Double.NEGATIVE_INFINITY));

            for(i = 0; i < 2; i++)
                playerGrid.getRowConstraints().add(new RowConstraints(Double.NEGATIVE_INFINITY, 38d, Double.NEGATIVE_INFINITY));

            AnchorPane.setTopAnchor(playerGrid, 2d);
            AnchorPane.setLeftAnchor(playerGrid, 2d);

            grids.add(playerGrid);
            tile.getChildren().add(playerGrid);

            pane.getChildren().add(tile);
        }

        checkGrids = new ArrayList<>();

        for(Triplet<Integer, Integer, Integer> coord : checks) {
            tile = new AnchorPane();
            tile.setPrefHeight(80d);
            tile.setPrefWidth(80d);
            GridPane.setColumnIndex(tile, coord.getFirst());
            GridPane.setRowIndex(tile, coord.getSecond());
            tile.setMaxHeight(Double.NEGATIVE_INFINITY);
            tile.setMaxWidth(Double.NEGATIVE_INFINITY);
            tile.setMinHeight(Double.NEGATIVE_INFINITY);
            tile.setMinWidth(Double.NEGATIVE_INFINITY);
            tile.setStyle("-fx-background-color: " + BORDER_POPE_COLORS.get(coord.getThird() - 1) + ";");

            points = new PointsBox(groups.get(checks.indexOf(coord)).getPoints());
            points.setScaleX(1.2d);
            points.setScaleY(1.2d);
            AnchorPane.setTopAnchor(points, 10d);
            AnchorPane.setLeftAnchor(points, 10d);
            tile.getChildren().add(points);

            playerGrid = new GridPane();
            for(i = 0; i < 2; i++)
                playerGrid.getColumnConstraints().add(new ColumnConstraints(Double.NEGATIVE_INFINITY, 38d, Double.NEGATIVE_INFINITY));

            for(i = 0; i < 2; i++)
                playerGrid.getRowConstraints().add(new RowConstraints(Double.NEGATIVE_INFINITY, 38d, Double.NEGATIVE_INFINITY));

            AnchorPane.setTopAnchor(playerGrid, 2d);
            AnchorPane.setLeftAnchor(playerGrid, 2d);

            checkGrids.add(playerGrid);
            tile.getChildren().add(playerGrid);

            pane.getChildren().add(tile);
        }
    }

    public void update() {
        int i, j, k;
        ImageView imageView;
        Label label;
        List<Integer> players = new ArrayList<>();
        List<ListProperty<FaithHolder.CheckpointStatus>> playerChecks = new ArrayList<>();
        Glyph glyph;

        if(getPlayer1PositionsProperty() >= 0)
            players.add(getPlayer1PositionsProperty());
        if(getPlayer2PositionsProperty() >= 0)
            players.add(getPlayer2PositionsProperty());
        if(getPlayer3PositionsProperty() >= 0)
            players.add(getPlayer3PositionsProperty());
        if(getPlayer4PositionsProperty() >= 0)
            players.add(getPlayer4PositionsProperty());
        if(getLorenzoPositionProperty() >= 0)
            players.add(getLorenzoPositionProperty());

        playerChecks.add(player1CheckpointsStatusProperty);
        playerChecks.add(player2CheckpointsStatusProperty);
        playerChecks.add(player3CheckpointsStatusProperty);
        playerChecks.add(player4CheckpointsStatusProperty);

        for(i = 0; i < grids.size(); i++) {
            grids.get(i).getChildren().clear();
            k = 0;

            for(j = 0; j < players.size(); j++) {

                if(players.get(j) == i) {
                    imageView = new ImageView();
                    Image image = new Image(ResourceLoader.getStreamFromResource("assets/resources/faith.png"));
                    imageView.setImage(image);
                    imageView.setFitHeight(38d);
                    imageView.setFitWidth(38d);

                    GridPane.setRowIndex(imageView, PLAYER_ROW_INDEX.get(k));
                    GridPane.setColumnIndex(imageView, PLAYER_COLUMN_INDEX.get(k));

                    if(j == 1 && getLorenzoPositionProperty() >= 0)
                        j = 4;

                    ColorAdjust filter = new ColorAdjust();
                    filter.setHue(PLAYER_HUES.get(j));
                    filter.setBrightness(PLAYER_BRIGHTNESS.get(j));
                    filter.setSaturation(PLAYER_SATURATION.get(j));

                    imageView.setEffect(filter);
                    imageView.setCache(true);
                    imageView.setCacheHint(CacheHint.SPEED);

                    k++;
                    grids.get(i).getChildren().add(imageView);
                }
            }
        }

        for(i = 0; i < checkGrids.size(); i++) {
            checkGrids.get(i).getChildren().clear();
            k = 0;

            for(j = 0; j < playerChecks.size(); j++) {
                if(playerChecks.get(j).get(i) != FaithHolder.CheckpointStatus.UNREACHED) {
                    label = new Label();
                    GridPane.setRowIndex(label, PLAYER_ROW_INDEX.get(k));
                    GridPane.setColumnIndex(label, PLAYER_COLUMN_INDEX.get(k));
                    label.setPrefWidth(38d);
                    label.setAlignment(Pos.CENTER);

                    if (playerChecks.get(j).get(i) == FaithHolder.CheckpointStatus.INACTIVE) {
                        glyph = new Glyph("FontAwesome", "TIMES");
                    } else {
                        glyph = new Glyph("FontAwesome", "CHECK");
                    }

                    glyph.setScaleX(3);
                    glyph.setScaleY(3);
                    glyph.setStyle("-fx-text-fill: " + PLAYER_COLORS.get(j) + ";");
                    label.setGraphic(glyph);

                    k++;
                    checkGrids.get(i).getChildren().add(label);
                }
            }
        }
    }

    public int getPlayer1PositionsProperty() {
        return player1PositionsProperty.get();
    }

    public IntegerProperty player1PositionsPropertyProperty() {
        return player1PositionsProperty;
    }

    public void setPlayer1PositionsProperty(int player1PositionsProperty) {
        this.player1PositionsProperty.set(player1PositionsProperty);
        update();
    }

    public int getPlayer2PositionsProperty() {
        return player2PositionsProperty.get();
    }

    public IntegerProperty player2PositionsPropertyProperty() {
        return player2PositionsProperty;
    }

    public void setPlayer2PositionsProperty(int player2PositionsProperty) {
        this.player2PositionsProperty.set(player2PositionsProperty);
        update();
    }

    public int getPlayer3PositionsProperty() {
        return player3PositionsProperty.get();
    }

    public IntegerProperty player3PositionsPropertyProperty() {
        return player3PositionsProperty;
    }

    public void setPlayer3PositionsProperty(int player3PositionsProperty) {
        this.player3PositionsProperty.set(player3PositionsProperty);
        update();
    }

    public int getPlayer4PositionsProperty() {
        return player4PositionsProperty.get();
    }

    public IntegerProperty player4PositionsPropertyProperty() {
        return player4PositionsProperty;
    }

    public void setPlayer4PositionsProperty(int player4PositionsProperty) {
        this.player4PositionsProperty.set(player4PositionsProperty);
        update();
    }

    public int getLorenzoPositionProperty() {
        return lorenzoPositionProperty.get();
    }

    public IntegerProperty lorenzoPositionPropertyProperty() {
        return lorenzoPositionProperty;
    }

    public void setLorenzoPositionProperty(int lorenzoPositionProperty) {
        this.lorenzoPositionProperty.set(lorenzoPositionProperty);
        update();
    }

    public ObservableList<FaithHolder.CheckpointStatus> getPlayer1CheckpointsStatusProperty() {
        return player1CheckpointsStatusProperty.get();
    }

    public ListProperty<FaithHolder.CheckpointStatus> player1CheckpointsStatusPropertyProperty() {
        return player1CheckpointsStatusProperty;
    }

    public void setPlayer1CheckpointsStatusProperty(ObservableList<FaithHolder.CheckpointStatus> player1CheckpointsStatusProperty) {
        this.player1CheckpointsStatusProperty.set(player1CheckpointsStatusProperty);
        update();
    }

    public ObservableList<FaithHolder.CheckpointStatus> getPlayer2CheckpointsStatusProperty() {
        return player2CheckpointsStatusProperty.get();
    }

    public ListProperty<FaithHolder.CheckpointStatus> player2CheckpointsStatusPropertyProperty() {
        return player2CheckpointsStatusProperty;
    }

    public void setPlayer2CheckpointsStatusProperty(ObservableList<FaithHolder.CheckpointStatus> player2CheckpointsStatusProperty) {
        this.player2CheckpointsStatusProperty.set(player2CheckpointsStatusProperty);
        update();
    }

    public ObservableList<FaithHolder.CheckpointStatus> getPlayer3CheckpointsStatusProperty() {
        return player3CheckpointsStatusProperty.get();
    }

    public ListProperty<FaithHolder.CheckpointStatus> player3CheckpointsStatusPropertyProperty() {
        return player3CheckpointsStatusProperty;
    }

    public void setPlayer3CheckpointsStatusProperty(ObservableList<FaithHolder.CheckpointStatus> player3CheckpointsStatusProperty) {
        this.player3CheckpointsStatusProperty.set(player3CheckpointsStatusProperty);
        update();
    }

    public ObservableList<FaithHolder.CheckpointStatus> getPlayer4CheckpointsStatusProperty() {
        return player4CheckpointsStatusProperty.get();
    }

    public ListProperty<FaithHolder.CheckpointStatus> player4CheckpointsStatusPropertyProperty() {
        return player4CheckpointsStatusProperty;
    }

    public void setPlayer4CheckpointsStatusProperty(ObservableList<FaithHolder.CheckpointStatus> player4CheckpointsStatusProperty) {
        this.player4CheckpointsStatusProperty.set(player4CheckpointsStatusProperty);
        update();
    }
}

