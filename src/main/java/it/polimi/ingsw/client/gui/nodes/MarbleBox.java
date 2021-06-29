package it.polimi.ingsw.client.gui.nodes;

import it.polimi.ingsw.common.utils.ResourceLoader;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.Map;

public class MarbleBox extends ImageView {
    private final StringProperty color;


    private static final Map<String, String> marbleImagePaths = new HashMap<>(){{
        put("blue", "assets/marbles/blue.png");
        put("grey", "assets/marbles/grey.png");
        put("purple", "assets/marbles/purple.png");
        put("red", "assets/marbles/red.png");
        put("white", "assets/marbles/white.png");
        put("yellow", "assets/marbles/yellow.png");
    }};

    public MarbleBox(){
        color = new SimpleStringProperty(this, "color", "red");
        attachElements();
    }

    public MarbleBox(String color){
        this.color = new SimpleStringProperty(this, "color", color.toLowerCase());
        attachElements();
    }

    private void attachElements(){
        setImage(ResourceLoader.loadImage(marbleImagePaths.get(color.get())));
        this.setFitWidth(40d);
        this.setFitHeight(40d);
        this.setPreserveRatio(true);
        this.setPickOnBounds(true);
    }

    private void update(){
        this.setImage(ResourceLoader.loadImage(marbleImagePaths.get(color.get())));
    }


    public String getColor() {
        return color.get();
    }

    public StringProperty colorProperty() {
        return color;
    }

    public void setColor(String color) {
        if(!color.equals(this.color.get())) {
            this.color.set(color);
            update();
        }
    }

}
