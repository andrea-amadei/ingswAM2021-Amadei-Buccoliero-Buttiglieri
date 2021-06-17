package it.polimi.ingsw.client.gui.nodes;

import it.polimi.ingsw.utils.ResourceLoader;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;


public class PointsBox extends StackPane {
    private final IntegerProperty points;

    public ImageView imageView;
    public Label pointsLabel;

    public PointsBox() {
        attachElements();

        this.points = new SimpleIntegerProperty(this, "points", 0);

        imageView.setImage(ResourceLoader.loadImage("assets/board/points.png"));
        update();
    }

    public PointsBox(int points) {
        attachElements();

        this.points = new SimpleIntegerProperty(this, "points", points);

        imageView.setImage(ResourceLoader.loadImage("assets/board/points.png"));
        update();
    }

    private void attachElements(){
        imageView = new ImageView();
        pointsLabel = new Label();

        imageView.setFitHeight(60d);
        imageView.setFitWidth(60d);
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);

        pointsLabel.setFont(new Font("Franklin Gothic Medium Cond", 22d));

        this.getChildren().addAll(imageView, pointsLabel);
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
