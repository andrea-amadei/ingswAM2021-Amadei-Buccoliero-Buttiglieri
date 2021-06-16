package it.polimi.ingsw.client.gui.nodes;

import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.model.FaithPathGroup;
import it.polimi.ingsw.model.FaithPathTile;
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

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class FaithPath extends GridPane {

    public IntegerProperty player1PositionsProperty;
    public IntegerProperty player2PositionsProperty;
    public IntegerProperty player3PositionsProperty;
    public IntegerProperty player4PositionsProperty;

    private List<RawFaithPathTile> tiles;
    private List<RawFaithPathGroup> groups;

    private List<GridPane> grids;

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

    private final List<String> PLAYER_COLORS = new ArrayList<>(Arrays.asList("#214dff", "#ff2021", "#089200", "#de8a00"));

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
        int i, j;

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

            pane.getChildren().add(tile);
        }
    }

    public void update() {
        int i, j, k;
        ImageView imageView;
        List<Integer> players = new ArrayList<>();

        if(getPlayer1PositionsProperty() >= 0)
            players.add(getPlayer1PositionsProperty());
        if(getPlayer2PositionsProperty() >= 0)
            players.add(getPlayer2PositionsProperty());
        if(getPlayer3PositionsProperty() >= 0)
            players.add(getPlayer3PositionsProperty());
        if(getPlayer4PositionsProperty() >= 0)
            players.add(getPlayer4PositionsProperty());

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

                    ColorAdjust monochrome = new ColorAdjust();
                    monochrome.setSaturation(-1);

                    Blend effect1 = new Blend(
                            BlendMode.MULTIPLY,
                            monochrome,
                            new ColorInput(0, 0, 38d, 38d, Color.web(PLAYER_COLORS.get(j)))
                    );

                    imageView.setEffect(effect1);
                    imageView.setCache(true);
                    imageView.setCacheHint(CacheHint.SPEED);

                    k++;
                    grids.get(i).getChildren().add(imageView);
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
    }

    public int getPlayer2PositionsProperty() {
        return player2PositionsProperty.get();
    }

    public IntegerProperty player2PositionsPropertyProperty() {
        return player2PositionsProperty;
    }

    public void setPlayer2PositionsProperty(int player2PositionsProperty) {
        this.player2PositionsProperty.set(player2PositionsProperty);
    }

    public int getPlayer3PositionsProperty() {
        return player3PositionsProperty.get();
    }

    public IntegerProperty player3PositionsPropertyProperty() {
        return player3PositionsProperty;
    }

    public void setPlayer3PositionsProperty(int player3PositionsProperty) {
        this.player3PositionsProperty.set(player3PositionsProperty);
    }

    public int getPlayer4PositionsProperty() {
        return player4PositionsProperty.get();
    }

    public IntegerProperty player4PositionsPropertyProperty() {
        return player4PositionsProperty;
    }

    public void setPlayer4PositionsProperty(int player4PositionsProperty) {
        this.player4PositionsProperty.set(player4PositionsProperty);
    }
}
