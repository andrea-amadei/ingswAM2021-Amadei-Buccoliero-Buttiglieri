package it.polimi.ingsw.client.gui.updaters;

import it.polimi.ingsw.client.gui.nodes.ShelfBox;
import it.polimi.ingsw.client.model.ClientShelf;
import it.polimi.ingsw.client.observables.Listener;
import javafx.application.Platform;

import java.util.List;

public class ShelfGuiUpdater implements Listener<ClientShelf> {

    private final ShelfBox shelfBox;

    public ShelfGuiUpdater(ShelfBox shelfBox, ClientShelf clientShelf) {
        this.shelfBox = shelfBox;

        clientShelf.addListener(this);
        update(clientShelf);
    }

    @Override
    public void update(ClientShelf clientShelf) {
        String resource;

        try {
            resource = List.copyOf(clientShelf.getStorage().getResources().keySet()).get(0);
        } catch (IndexOutOfBoundsException e) {
            Platform.runLater(() -> {
                shelfBox.setResource1("none");
                shelfBox.setResource2("none");
                shelfBox.setResource3("none");
            });

            return;
        }

        int n = clientShelf.getStorage().getResources().get(resource);

        if(n >= 1)
            Platform.runLater(() -> shelfBox.setResource1(resource));
        else
            Platform.runLater(() -> shelfBox.setResource1("none"));

        if(n >= 2)
            Platform.runLater(() -> shelfBox.setResource2(resource));
        else
            Platform.runLater(() -> shelfBox.setResource2("none"));

        if(n >= 3)
            Platform.runLater(() -> shelfBox.setResource3(resource));
        else
            Platform.runLater(() -> shelfBox.setResource3("none"));

        Platform.runLater(() -> shelfBox.setSelectedResources(clientShelf.getSelectedResources().values().stream().reduce(0, Integer::sum)));
    }
}
