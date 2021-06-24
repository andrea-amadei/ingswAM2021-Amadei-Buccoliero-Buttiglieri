package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.gui.GuiSceneManager;
import it.polimi.ingsw.client.model.ClientModel;

public abstract class BaseController {

    private ClientModel model;
    private ServerHandler serverHandler;
    private GuiSceneManager sceneManager;

    public void init(ClientModel model, ServerHandler serverHandler, GuiSceneManager sceneManager) {
        this.model = model;
        this.serverHandler = serverHandler;
        this.sceneManager = sceneManager;
    }

    public ClientModel getModel() {
        return model;
    }

    public ServerHandler getServerHandler() {
        return serverHandler;
    }

    public GuiSceneManager getSceneManager() {
        return sceneManager;
    }
}
