package it.polimi.ingsw.configurator;

import it.polimi.ingsw.client.ClientDisconnectionManager;
import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.gui.GuiBuilder;
import it.polimi.ingsw.client.gui.GuiSceneManager;
import it.polimi.ingsw.client.gui.controllers.LobbyGuiController;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.utils.ResourceLoader;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

public class ConfiguratorMain extends Application {
    public static void main(String[] args) {
        launch();
    }

    public void start(Stage stage) throws Exception {
        stage.setOnCloseRequest(ignored -> {
            Platform.exit();
            System.exit(0);
        });

        Pair<Parent, ?> sceneAndController = ResourceLoader.loadFXML("jfx/configurator.fxml");
        Scene scene = new Scene(sceneAndController.getFirst());

        stage.setTitle("Configurator");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}
