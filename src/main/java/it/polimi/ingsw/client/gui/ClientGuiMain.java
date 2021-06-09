package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.ClientDisconnectionManager;
import it.polimi.ingsw.client.InputReader;
import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.model.ClientModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ClientGuiMain extends Application {

    public static void main(String[] args) {
        launch();
    }

    public void start(Stage stage) throws Exception {
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        // TODO: KeyCombination.NO_MATCH
        stage.setFullScreenExitKeyCombination(KeyCombination.keyCombination("ESC"));

        stage.setOnCloseRequest(ignored -> {
            Platform.exit();
            System.exit(0);
        });

        GuiSceneManager sceneManager = new GuiSceneManager(stage);

        // TODO: options to connect with ip and port

        ClientModel model = new ClientModel();

        ServerHandler serverHandler = new ServerHandler("localhost", 6789, model, null);
        serverHandler.start();

        GuiBuilder.createStartScene(sceneManager, model, serverHandler);

        ClientDisconnectionManager disconnectionManager = new ClientDisconnectionManager(5000, serverHandler);
        disconnectionManager.start();

        stage.show();
    }
}
