package it.polimi.ingsw.client.gui.updaters;

import it.polimi.ingsw.client.gui.events.ChangedCurrentPlayerEvent;
import it.polimi.ingsw.client.gui.nodes.MenuBox;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.client.observables.Listener;
import javafx.application.Platform;

public class TurnGuiUpdater implements Listener<ClientModel> {
    private final MenuBox menuBox;

    public TurnGuiUpdater(MenuBox menuBox, ClientModel clientModel){
        this.menuBox = menuBox;
        clientModel.addListener(this);
        if(clientModel.getCurrentPlayer() != null){
            update(clientModel);
        }
    }
    @Override
    public void update(ClientModel clientModel) {
        Platform.runLater(() -> menuBox.fireEvent(new ChangedCurrentPlayerEvent(clientModel.getCurrentPlayer().getUsername())));
    }
}
