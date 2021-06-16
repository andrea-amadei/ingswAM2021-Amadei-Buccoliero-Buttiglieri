package it.polimi.ingsw;

import it.polimi.ingsw.client.gui.updaters.LobbyPersonalDataGuiUpdater;
import it.polimi.ingsw.client.model.PersonalData;
import it.polimi.ingsw.utils.ResourceLoader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.IOException;

public class JFXTest extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(ResourceLoader.loadFXML("jfx/board.fxml").getFirst());
        scene.getStylesheets().add(JFXTest.class.getProtectionDomain().getClassLoader().getResource("jfx/style.css").toExternalForm());
        stage.setScene(scene);

        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.setFullScreenExitKeyCombination(KeyCombination.keyCombination("ESC"));

//        PersonalData personalData = new PersonalData();
//        personalData.setUsername("Ama1899");
//        personalData.setGameName("123");
//        LobbyPersonalDataGuiUpdater lobbyPersonalDataGuiUpdater = new LobbyPersonalDataGuiUpdater(personalData, scene);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}