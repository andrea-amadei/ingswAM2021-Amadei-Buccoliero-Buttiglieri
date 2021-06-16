package it.polimi.ingsw.client.gui.nodes;

import it.polimi.ingsw.utils.ResourceLoader;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class PointsBox extends StackPane {
    private final IntegerProperty points;

    @FXML
    public ImageView imageView;

    @FXML
    public Label pointsLabel;

    public PointsBox() {
        FXMLLoader fxmlLoader = new FXMLLoader(ResourceLoader.getResource("jfx/custom/PointsBox.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException("Unable to load custom element '" + getClass().getSimpleName() + "': " + exception);
        }

        this.points = new SimpleIntegerProperty(this, "points", 0);

        imageView.setImage(new Image(ResourceLoader.getStreamFromResource("assets/board/points.png")));
        update();
    }

    public PointsBox(int points) {
        FXMLLoader fxmlLoader = new FXMLLoader(ResourceLoader.getResource("jfx/custom/PointsBox.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException("Unable to load custom element '" + getClass().getSimpleName() + "': " + exception);
        }

        this.points = new SimpleIntegerProperty(this, "points", points);

        imageView.setImage(new Image(ResourceLoader.getStreamFromResource("assets/board/points.png")));
        update();
    }

    private void update() {
        pointsLabel.setText(String.valueOf(getPoints()));
    }

    public IntegerProperty pointsProperty() {
        return points;
    }

    public int getPoints() {
        return pointsProperty().get();
    }

    public void setPoints(int points) {
        this.points.set(points);
        update();
    }
}
