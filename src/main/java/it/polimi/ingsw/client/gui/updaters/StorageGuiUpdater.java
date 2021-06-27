package it.polimi.ingsw.client.gui.updaters;

import it.polimi.ingsw.client.gui.nodes.HResourceContainer;
import it.polimi.ingsw.client.gui.nodes.ResourceContainer;
import it.polimi.ingsw.client.model.ClientBaseStorage;
import it.polimi.ingsw.client.model.ClientPlayer;
import it.polimi.ingsw.client.observables.Listener;
import it.polimi.ingsw.model.storage.BaseStorage;
import javafx.application.Platform;
import javafx.collections.FXCollections;

public class StorageGuiUpdater implements Listener<ClientBaseStorage> {

    private final ResourceContainer resourceContainer;

    public StorageGuiUpdater(ResourceContainer resourceContainer, ClientBaseStorage clientBaseStorage) {
        this.resourceContainer = resourceContainer;

        clientBaseStorage.addListener(this);
        update(clientBaseStorage);
    }

    @Override
    public void update(ClientBaseStorage clientBaseStorage) {
        Platform.runLater(() -> {
            resourceContainer.setRawStorage(clientBaseStorage.getStorage());
            resourceContainer.setSelectedResources(FXCollections.observableMap(clientBaseStorage.getSelectedResources()));
        });
    }
}
