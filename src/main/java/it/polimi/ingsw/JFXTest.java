package it.polimi.ingsw;

import it.polimi.ingsw.client.gui.updaters.LobbyPersonalDataGuiUpdater;
import it.polimi.ingsw.client.model.PersonalData;
import it.polimi.ingsw.utils.ResourceLoader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.io.IOException;

public class JFXTest extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(ResourceLoader.loadFXML("jfx/start.fxml").getFirst());
        scene.getStylesheets().add(JFXTest.class.getProtectionDomain().getClassLoader().getResource("jfx/style.css").toExternalForm());
        stage.setScene(scene);

        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        // TODO: KeyCombination.NO_MATCH
        stage.setFullScreenExitKeyCombination(KeyCombination.keyCombination("ESC"));

        PersonalData personalData = new PersonalData();
        personalData.setUsername("Ama1899");
        personalData.setGameName("123");
        LobbyPersonalDataGuiUpdater lobbyPersonalDataGuiUpdater = new LobbyPersonalDataGuiUpdater(personalData, scene);

        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(ResourceLoader.loadFXML(fxml).getFirst());
    }

    public static void main(String[] args) {
        launch();
    }

}