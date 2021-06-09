package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.gui.controllers.LobbyGuiController;
import it.polimi.ingsw.client.gui.updaters.LobbyPersonalDataGuiUpdater;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.utils.ResourceLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public final class GuiBuilder {

    public static void createStartScene(GuiSceneManager sceneManager, ClientModel model, ServerHandler serverHandler) {
        Pair<Parent, ?> sceneAndController = ResourceLoader.loadFXML("jfx/start.fxml");

        Scene scene = new Scene(sceneAndController.getFirst());
        LobbyGuiController lobbyController = (LobbyGuiController) sceneAndController.getSecond();

        lobbyController.init(model, serverHandler, sceneManager);

        new LobbyPersonalDataGuiUpdater(model.getPersonalData(), scene);

        sceneManager.addScene("start", scene);
        sceneManager.setActiveScene("start");
    }
}