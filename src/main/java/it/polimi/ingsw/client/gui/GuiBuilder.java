package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.gui.controllers.BoardGuiController;
import it.polimi.ingsw.client.gui.controllers.LobbyGuiController;
import it.polimi.ingsw.client.gui.updaters.LobbyPersonalDataGuiUpdater;
import it.polimi.ingsw.client.gui.updaters.ShopGuiUpdater;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.utils.ResourceLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public final class GuiBuilder {

    public static void createStartScene(GuiSceneManager sceneManager, ClientModel clientModel, ServerHandler serverHandler) {
        Pair<Parent, ?> sceneAndController = ResourceLoader.loadFXML("jfx/start.fxml");

        Scene scene = new Scene(sceneAndController.getFirst());
        LobbyGuiController lobbyController = (LobbyGuiController) sceneAndController.getSecond();

        lobbyController.init(clientModel, serverHandler, sceneManager);

        sceneManager.addScene("start", scene);
        sceneManager.setActiveScene("start");

        new LobbyPersonalDataGuiUpdater(clientModel.getPersonalData(), scene);
    }

    public static void createGameScene(GuiSceneManager sceneManager, ClientModel clientModel, ServerHandler serverHandler) {
        Pair<Parent, ?> sceneAndController = ResourceLoader.loadFXML("jfx/board.fxml");

        Scene scene = new Scene(sceneAndController.getFirst());
        BoardGuiController boardController = (BoardGuiController) sceneAndController.getSecond();

        boardController.init(clientModel, serverHandler, sceneManager);

        sceneManager.addScene("board", scene);
        sceneManager.setActiveScene("board");

        boardController.boardSetup();
    }
}