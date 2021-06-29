package it.polimi.ingsw.client.gui;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.LinkedHashMap;

public class GuiSceneManager {
    private final LinkedHashMap<String, Scene> scenes;
    private final Stage stage;

    public GuiSceneManager(Stage stage) {
        scenes = new LinkedHashMap<>();
        this.stage = stage;
    }

    public void addScene(String name, Scene scene) {
        if(scene == null || name == null)
            throw new NullPointerException();

        if(scenes.containsKey(name))
            throw new IllegalArgumentException("A scene with the same name already exists");

        scenes.put(name, scene);
    }

    public void setActiveScene(String name) {
        if(this.stage == null)
            throw new IllegalStateException("Stage must be set first");

        if(!scenes.containsKey(name))
            throw new IllegalArgumentException("The selected scene doesn't exists");

        stage.setScene(scenes.get(name));
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.show();
    }

    public Scene getActiveScene() {
        return stage.getScene();
    }

    public Stage getStage(){
        return stage;
    }
}
